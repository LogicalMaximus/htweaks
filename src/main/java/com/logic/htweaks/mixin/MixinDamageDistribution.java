package com.logic.htweaks.mixin;

import com.logic.htweaks.bridge.IDamageDistribution;
import com.logic.htweaks.bridge.IDamageablePart;
import com.logic.htweaks.utils.FirstAidUtils;
import ichttt.mods.firstaid.FirstAid;
import ichttt.mods.firstaid.FirstAidConfig;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.distribution.IDamageDistributionAlgorithm;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.common.RegistryObjects;
import ichttt.mods.firstaid.common.damagesystem.distribution.DamageDistribution;
import ichttt.mods.firstaid.common.network.MessageUpdatePart;
import ichttt.mods.firstaid.common.util.LoggingMarkers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Stream;

@Mixin(DamageDistribution.class)
public abstract class MixinDamageDistribution implements IDamageDistributionAlgorithm, IDamageDistribution {

    @Shadow(remap=false)
    protected abstract List<Pair<EquipmentSlot, EnumPlayerPart[]>> getPartList();

    @Override
    public float htweaks$distributeDamage(float damage, @Nonnull LivingEntity livingEntity, @Nonnull DamageSource source, boolean addStat) {
        if (damage <= 0.0F) {
            return 0.0F;
        } else {
            AbstractPlayerDamageModel damageModel = FirstAidUtils.getDamageModel(livingEntity);
            if (damageModel == null) {
                return 0.0F;
            } else {
                if ((Boolean) FirstAidConfig.GENERAL.debug.get()) {
                    FirstAid.LOGGER.info(LoggingMarkers.DAMAGE_DISTRIBUTION, "Starting distribution of {} damage...", damage);
                }

                Iterator var6 = this.getPartList().iterator();

                while(true) {
                    if (var6.hasNext()) {
                        Pair<EquipmentSlot, EnumPlayerPart[]> pair = (Pair)var6.next();
                        EquipmentSlot slot = (EquipmentSlot)pair.getLeft();
                        EnumPlayerPart[] parts = (EnumPlayerPart[])pair.getRight();
                        Stream<EnumPlayerPart> var10000 = Arrays.stream(parts);
                        Objects.requireNonNull(damageModel);
                        if (!var10000.map(damageModel::getFromEnum).anyMatch((part) -> {
                            return part.currentHealth > this.htweaks$minHealth(livingEntity, part);
                        })) {
                            if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                                FirstAid.LOGGER.info(LoggingMarkers.DAMAGE_DISTRIBUTION, "Skipping {}, no health > min in parts!", slot);
                            }
                            continue;
                        }

                        float originalDamage = damage;
                        damage = FirstAidUtils.applyArmor(livingEntity, livingEntity.getItemBySlot(slot), source, damage, slot);
                        if (damage <= 0.0F) {
                            return 0.0F;
                        }

                        damage = FirstAidUtils.applyEnchantmentModifiers(livingEntity, slot, source, damage);
                        if (damage <= 0.0F) {
                            return 0.0F;
                        }

                        damage = ForgeHooks.onLivingDamage(livingEntity, source, damage);
                        if (damage <= 0.0F) {
                            return 0.0F;
                        }

                        float dmgAfterReduce = damage;
                        damage = this.htweaks$distributeDamageOnParts(damage, damageModel, parts, livingEntity, addStat);
                        if (damage != 0.0F) {
                            float absorbFactor = originalDamage / dmgAfterReduce;
                            float damageDistributed = dmgAfterReduce - damage;
                            damage = originalDamage - damageDistributed * absorbFactor;
                            if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                                FirstAid.LOGGER.info(LoggingMarkers.DAMAGE_DISTRIBUTION, "Distribution round: Not done yet, going to next round. Needed to distribute {} damage (reduced to {}) to {}, but only distributed {}. New damage to be distributed is {}, based on absorb factor {}", originalDamage, dmgAfterReduce, slot, damageDistributed, damage, absorbFactor);
                            }
                            continue;
                        }
                    }

                    return damage;
                }
            }
        }
    }

    @Unique
    private float htweaks$minHealth(@Nonnull Entity player, @Nonnull AbstractDamageablePart part) {
        return 0.0F;
    }

    @Unique
    protected float htweaks$distributeDamageOnParts(float damage, @Nonnull AbstractPlayerDamageModel damageModel, @Nonnull EnumPlayerPart[] enumParts, @Nonnull LivingEntity livingEntity, boolean addStat) {
        ArrayList<AbstractDamageablePart> damageableParts = new ArrayList(enumParts.length);
        EnumPlayerPart[] var7 = enumParts;
        int var8 = enumParts.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            EnumPlayerPart part = var7[var9];
            damageableParts.add(damageModel.getFromEnum(part));
        }

        Collections.shuffle(damageableParts);
        Iterator var12 = damageableParts.iterator();

        while(var12.hasNext()) {
            AbstractDamageablePart part = (AbstractDamageablePart)var12.next();
            float minHealth = this.htweaks$minHealth(livingEntity, part);
            float dmgDone = damage - ((IDamageablePart)part).htweaks$damage(damage, livingEntity, !livingEntity.hasEffect((MobEffect) RegistryObjects.MORPHINE_EFFECT.get()), minHealth);
            FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> {
                return (ServerPlayer)livingEntity;
            }), new MessageUpdatePart(part));

            damage -= dmgDone;
            if (damage == 0.0F) {
                break;
            }

            if (damage < 0.0F) {
                FirstAid.LOGGER.error(LoggingMarkers.DAMAGE_DISTRIBUTION, "Got negative damage {} left? Logic error? ", damage);
                break;
            }
        }

        return damage;
    }

}
