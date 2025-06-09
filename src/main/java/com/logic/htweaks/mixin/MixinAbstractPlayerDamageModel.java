package com.logic.htweaks.mixin;

import com.logic.htweaks.bridge.IPlayerDamageModel;
import ichttt.mods.firstaid.FirstAid;
import ichttt.mods.firstaid.FirstAidConfig;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.common.CapProvider;
import ichttt.mods.firstaid.common.RegistryObjects;
import ichttt.mods.firstaid.common.SynchedEntityDataWrapper;
import ichttt.mods.firstaid.common.compat.playerrevive.PRCompatManager;
import ichttt.mods.firstaid.common.damagesystem.debuff.SharedDebuff;
import ichttt.mods.firstaid.common.network.MessageSyncDamageModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.*;

public abstract class MixinAbstractPlayerDamageModel implements Iterable<AbstractDamageablePart>, INBTSerializable<CompoundTag>, IPlayerDamageModel {

    private final Set<SharedDebuff> htweaks$sharedDebuffs = new HashSet();

    @Unique
    private boolean htweaks$noCritical = !(Boolean)FirstAidConfig.SERVER.causeDeathBody.get() && !(Boolean)FirstAidConfig.SERVER.causeDeathHead.get();
    private float htweaks$prevHealthCurrent = 1.0F;

    private float htweaks$prevScaleFactor;

    public void tick(Level world, LivingEntity livingEntity) {
        if (this.htweaks$isDead(livingEntity)) {
            world.getProfiler().push("FirstAidPlayerModel");

            float newCurrentHealth = this.htweaks$calculateNewCurrentHealth(livingEntity);
            if (Float.isNaN(newCurrentHealth)) {
                FirstAid.LOGGER.warn("New current health is not a number, setting it to 0!");
                newCurrentHealth = 0.0F;
            }

            if (newCurrentHealth <= 0.0F) {
                FirstAid.LOGGER.error("Got {} health left, but isn't marked as dead!", newCurrentHealth);
                world.getProfiler().pop();
            } else {

                if (Float.isInfinite(newCurrentHealth)) {
                    FirstAid.LOGGER.error("Error calculating current health: Value was infinite");
                } else {
                    if (newCurrentHealth != this.htweaks$prevHealthCurrent) {
                        //((SynchedEntityDataWrapper)livingEntity.entityData).set_impl(LivingEntity.DATA_HEALTH_ID, newCurrentHealth);
                    }

                    this.htweaks$prevHealthCurrent = newCurrentHealth;
                }

                this.htweaks$runScaleLogic(livingEntity);

                world.getProfiler().push("PartDebuffs");
                this.forEach((part) -> {
                    //part.tick(world, livingEntity, false);
                });
                if (!world.isClientSide) {
                    this.htweaks$sharedDebuffs.forEach((sharedDebuff) -> {
                        //sharedDebuff.tick(livingEntity);
                    });
                }

                world.getProfiler().pop();
                world.getProfiler().pop();
            }
        }
    }

    public void htweaks$runScaleLogic(LivingEntity livingEntity) {
        if ((Boolean)FirstAidConfig.SERVER.scaleMaxHealth.get()) {
            livingEntity.level().getProfiler().push("healthscaling");
            float globalFactor = livingEntity.getMaxHealth() / 20.0F;
            if (this.htweaks$prevScaleFactor != globalFactor) {
                if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                    FirstAid.LOGGER.info("Starting health scaling factor {} -> {} (max health {})", this.htweaks$prevScaleFactor, globalFactor, livingEntity.getMaxHealth());
                }

                livingEntity.level().getProfiler().push("distribution");
                int reduced = 0;
                int added = 0;
                float expectedNewMaxHealth = 0.0F;
                int newMaxHealth = 0;
                Iterator var7 = this.iterator();

                while(true) {
                    int maxHealth;
                    if (!var7.hasNext()) {
                        livingEntity.level().getProfiler().popPush("correcting");
                        if (Math.abs(expectedNewMaxHealth - (float)newMaxHealth) >= 2.0F) {
                            if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                                FirstAid.LOGGER.info("Entering second stage - diff {}", Math.abs(expectedNewMaxHealth - (float)newMaxHealth));
                            }

                            List<AbstractDamageablePart> prioList = new ArrayList();
                            Iterator var13 = this.iterator();

                            AbstractDamageablePart part;
                            while(var13.hasNext()) {
                                part = (AbstractDamageablePart)var13.next();
                                prioList.add(part);
                            }

                            prioList.sort(Comparator.comparingInt(AbstractDamageablePart::getMaxHealth));
                            var13 = prioList.iterator();

                            while(var13.hasNext()) {
                                part = (AbstractDamageablePart)var13.next();
                                maxHealth = part.getMaxHealth();
                                if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                                    FirstAid.LOGGER.info("Part {}: Second stage with total diff {}", part.part.name(), Math.abs(expectedNewMaxHealth - (float)newMaxHealth));
                                }

                                if (expectedNewMaxHealth > (float)newMaxHealth) {
                                    part.setMaxHealth(maxHealth + 2);
                                    newMaxHealth += part.getMaxHealth() - maxHealth;
                                } else if (expectedNewMaxHealth < (float)newMaxHealth) {
                                    part.setMaxHealth(maxHealth - 2);
                                    newMaxHealth -= maxHealth - part.getMaxHealth();
                                }

                                if (Math.abs(expectedNewMaxHealth - (float)newMaxHealth) < 2.0F) {
                                    break;
                                }
                            }
                        }

                        livingEntity.level().getProfiler().pop();
                        break;
                    }

                    AbstractDamageablePart part = (AbstractDamageablePart)var7.next();
                    float floatResult = (float)part.initialMaxHealth * globalFactor;
                    expectedNewMaxHealth += floatResult;
                    maxHealth = (int)floatResult;
                    if (maxHealth % 2 == 1) {
                        int partMaxHealth = part.getMaxHealth();
                        if (part.currentHealth < (float)partMaxHealth && reduced < 4) {
                            --maxHealth;
                            ++reduced;
                        } else if (part.currentHealth > (float)partMaxHealth && added < 4) {
                            ++maxHealth;
                            ++added;
                        } else if (reduced > added) {
                            ++maxHealth;
                            ++added;
                        } else {
                            --maxHealth;
                            ++reduced;
                        }
                    }

                    newMaxHealth += maxHealth;
                    if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                        FirstAid.LOGGER.info("Part {} max health: {} initial; {} old; {} new", part.part.name(), part.initialMaxHealth, part.getMaxHealth(), maxHealth);
                    }

                    part.setMaxHealth(maxHealth);
                }
            }

            this.htweaks$prevScaleFactor = globalFactor;
            livingEntity.level().getProfiler().pop();
        }

    }

    @Unique
    private float htweaks$calculateNewCurrentHealth(LivingEntity livingEntity) {
        float currentHealth = 0.0F;
        FirstAidConfig.Server.VanillaHealthCalculationMode mode = (FirstAidConfig.Server.VanillaHealthCalculationMode)FirstAidConfig.SERVER.vanillaHealthCalculation.get();
        if (this.htweaks$noCritical) {
            mode = FirstAidConfig.Server.VanillaHealthCalculationMode.AVERAGE_ALL;
        }

        float partCurrentHealth;
        Iterator var15;
        AbstractDamageablePart part;
        switch (mode) {
            case AVERAGE_CRITICAL:
                int maxHealth = 0;
                Iterator var13 = this.iterator();

                while(var13.hasNext()) {
                    AbstractDamageablePart next = (AbstractDamageablePart)var13.next();
                    if (next.canCauseDeath) {
                        currentHealth += next.currentHealth;
                        maxHealth += next.getMaxHealth();
                    }
                }

                currentHealth /= (float)maxHealth;
                break;
            case MIN_CRITICAL:
                AbstractDamageablePart minimal = null;
                float lowest = Float.MAX_VALUE;
                var15 = this.iterator();

                while(var15.hasNext()) {
                    part = (AbstractDamageablePart)var15.next();
                    if (part.canCauseDeath) {
                        partCurrentHealth = part.currentHealth;
                        if (partCurrentHealth < lowest) {
                            minimal = part;
                            lowest = partCurrentHealth;
                        }
                    }
                }

                Objects.requireNonNull(minimal);
                currentHealth = minimal.currentHealth / (float)minimal.getMaxHealth();
                break;
            case AVERAGE_ALL:
                for(var15 = this.iterator(); var15.hasNext(); currentHealth += part.currentHealth) {
                    part = (AbstractDamageablePart)var15.next();
                }

                currentHealth /= (float)this.htweaks$getCurrentMaxHealth();
                break;
            case CRITICAL_50_PERCENT_OTHER_50_PERCENT:
                float currentNormal = 0.0F;
                int maxNormal = 0;
                partCurrentHealth = 0.0F;
                int maxCritical = 0;
                Iterator var11 = this.iterator();

                while(var11.hasNext()) {
                    AbstractDamageablePart next = (AbstractDamageablePart)var11.next();
                    if (!next.canCauseDeath) {
                        currentNormal += next.currentHealth;
                        maxNormal += next.getMaxHealth();
                    } else {
                        partCurrentHealth += next.currentHealth;
                        maxCritical += next.getMaxHealth();
                    }
                }

                float avgNormal = currentNormal / (float)maxNormal;
                float avgCritical = partCurrentHealth / (float)maxCritical;
                currentHealth = (avgCritical + avgNormal) / 2.0F;
                break;
            default:
                throw new RuntimeException("Unknown constant " + mode);
        }

        return currentHealth * livingEntity.getMaxHealth();
    }

    public int htweaks$getCurrentMaxHealth() {
        int maxHealth = 0;

        AbstractDamageablePart part;
        for(Iterator var2 = this.iterator(); var2.hasNext(); maxHealth += part.getMaxHealth()) {
            part = (AbstractDamageablePart)var2.next();
        }

        return maxHealth;
    }

    public boolean htweaks$isDead(@Nullable LivingEntity livingEntity) {
        if (livingEntity != null && !livingEntity.isAlive()) {
            return true;
        } else if (!this.htweaks$noCritical) {
            Iterator var6 = this.iterator();

            AbstractDamageablePart part;
            do {
                if (!var6.hasNext()) {
                    return false;
                }

                part = (AbstractDamageablePart)var6.next();
            } while(!part.canCauseDeath || !(part.currentHealth <= 0.0F));

            return true;
        } else {
            boolean dead = true;
            Iterator var4 = this.iterator();

            while(var4.hasNext()) {
                AbstractDamageablePart part = (AbstractDamageablePart)var4.next();
                if (part.currentHealth > 0.0F) {
                    dead = false;
                    break;
                }
            }

            return dead;
        }
    }
}
