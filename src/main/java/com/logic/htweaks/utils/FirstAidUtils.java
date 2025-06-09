package com.logic.htweaks.utils;

import com.google.common.math.DoubleMath;
import com.logic.htweaks.bridge.IDamageDistribution;
import com.logic.htweaks.bridge.IEntityDamageModel;
import ichttt.mods.firstaid.FirstAid;
import ichttt.mods.firstaid.FirstAidConfig;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.distribution.IDamageDistributionAlgorithm;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.common.AABBAlignedBoundingBox;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import ichttt.mods.firstaid.common.damagesystem.distribution.RandomDamageDistributionAlgorithm;
import ichttt.mods.firstaid.common.damagesystem.distribution.StandardDamageDistributionAlgorithm;
import ichttt.mods.firstaid.common.util.ArmorUtils;
import ichttt.mods.firstaid.common.util.CommonUtils;
import ichttt.mods.firstaid.common.util.LoggingMarkers;
import ichttt.mods.firstaid.common.util.PlayerSizeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FirstAidUtils {


    @Nullable
    public static AbstractPlayerDamageModel getDamageModel(Entity entity) {
        LazyOptional<AbstractPlayerDamageModel> optionalDamageModel = getOptionalDamageModel(entity);

            return (AbstractPlayerDamageModel)optionalDamageModel.orElse(null);
    }

    @Nonnull
    public static LazyOptional<AbstractPlayerDamageModel> getOptionalDamageModel(Entity entity) {
        return entity.getCapability(CapabilityExtendedHealthSystem.INSTANCE);
    }

    public static EquipmentSlot getSlotTypeForProjectileHit(Entity hittingObject, Entity toTest) {
        Map<EquipmentSlot, AABBAlignedBoundingBox> toUse = PlayerSizeHelper.getBoxes(toTest);
        Vec3 oldPosition = hittingObject.position();
        Vec3 newPosition = oldPosition.add(hittingObject.getDeltaMovement());
        float[] inflationSteps = new float[]{0.01F, 0.1F, 0.2F, 0.3F};
        float[] var6 = inflationSteps;
        int var7 = inflationSteps.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            float inflation = var6[var8];
            EquipmentSlot bestSlot = null;
            double bestValue = Double.MAX_VALUE;
            Iterator var13 = toUse.entrySet().iterator();

            while(var13.hasNext()) {
                Map.Entry<EquipmentSlot, AABBAlignedBoundingBox> entry = (Map.Entry)var13.next();
                AABB axisalignedbb = ((AABBAlignedBoundingBox)entry.getValue()).createAABB(toTest.getBoundingBox()).inflate((double)inflation);
                Optional<Vec3> optional = axisalignedbb.clip(oldPosition, newPosition);
                if (optional.isPresent()) {
                    double d1 = oldPosition.distanceToSqr((Vec3)optional.get());
                    double d2 = 0.0;
                    if (d1 + d2 < bestValue) {
                        bestSlot = (EquipmentSlot)entry.getKey();
                        bestValue = d1 + d2;
                    }
                }
            }

            if (bestSlot != null) {
                if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                    FirstAid.LOGGER.info("getSlotTypeForProjectileHit: Inflation: " + inflation + " best slot: " + bestSlot);
                }

                return bestSlot;
            }
        }

        if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
            FirstAid.LOGGER.info("getSlotTypeForProjectileHit: Not found!");
        }

        return null;
    }

    public static IDamageDistributionAlgorithm getMeleeDistribution(Entity player, DamageSource source) {
        Entity causingEntity = source.getEntity();
        if (causingEntity != null && causingEntity == source.getDirectEntity() && causingEntity instanceof Mob mobEntity) {
            if (mobEntity.getTarget() == player && mobEntity.goalSelector.getRunningGoals().anyMatch((prioritizedGoal) -> {
                return prioritizedGoal.getGoal() instanceof MeleeAttackGoal;
            })) {
                Map<EquipmentSlot, AABBAlignedBoundingBox> boxes = PlayerSizeHelper.getBoxes(player);
                if (!boxes.isEmpty()) {
                    List<EquipmentSlot> allowedParts = new ArrayList();
                    AABB modAABB = mobEntity.getBoundingBox().inflate((double)(mobEntity.getBbWidth() * 2.0F + player.getBbWidth()), 0.0, (double)(mobEntity.getBbWidth() * 2.0F + player.getBbWidth()));
                    Iterator var7 = boxes.entrySet().iterator();

                    while(var7.hasNext()) {
                        Map.Entry<EquipmentSlot, AABBAlignedBoundingBox> entry = (Map.Entry)var7.next();
                        AABB partAABB = ((AABBAlignedBoundingBox)entry.getValue()).createAABB(player.getBoundingBox());
                        if (modAABB.intersects(partAABB)) {
                            allowedParts.add((EquipmentSlot)entry.getKey());
                        }
                    }

                    if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                        FirstAid.LOGGER.info("getMeleeDistribution: Has distribution with {}", allowedParts);
                    }

                    if (allowedParts.isEmpty() && player.getY() > mobEntity.getY() && player.getY() - mobEntity.getY() < (double)(mobEntity.getBbHeight() * 2.0F)) {
                        if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                            FirstAid.LOGGER.info("Hack adding feet");
                        }

                        allowedParts.add(EquipmentSlot.FEET);
                    }

                    if (!allowedParts.isEmpty() && !allowedParts.containsAll(Arrays.asList(CommonUtils.ARMOR_SLOTS))) {
                        Map<EquipmentSlot, List<EnumPlayerPart>> list = new LinkedHashMap();
                        Iterator var11 = allowedParts.iterator();

                        while(var11.hasNext()) {
                            EquipmentSlot allowedPart = (EquipmentSlot)var11.next();
                            list.put(allowedPart, CommonUtils.getPartListForSlot(allowedPart));
                        }

                        return new StandardDamageDistributionAlgorithm(list, true, true);
                    }
                }
            }
        }

        return null;
    }

    public static float handleDamageTaken(IDamageDistributionAlgorithm damageDistribution, AbstractPlayerDamageModel damageModel, float damage, @Nonnull LivingEntity player, @Nonnull DamageSource source, boolean addStat, boolean redistributeIfLeft) {
        if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
            FirstAid.LOGGER.info(LoggingMarkers.DAMAGE_DISTRIBUTION, "--- Damaging {} using {} for dmg source {}, redistribute {}, addStat {} ---", damage, damageDistribution.toString(), source.type().msgId(), redistributeIfLeft, addStat);
        }

        CompoundTag beforeCache = (CompoundTag)damageModel.serializeNBT();
        if (!damageDistribution.skipGlobalPotionModifiers()) {
            damage = FirstAidUtils.applyGlobalPotionModifiers(player, source, damage);
        }

        float left = ((IDamageDistribution)damageDistribution).htweaks$distributeDamage(damage, player, source, addStat);
        if (left > 0.0F && redistributeIfLeft) {
            boolean hasTriedNoKill = damageDistribution == RandomDamageDistributionAlgorithm.NEAREST_NOKILL || damageDistribution == RandomDamageDistributionAlgorithm.ANY_NOKILL;
            IDamageDistributionAlgorithm damageDistribution2 = hasTriedNoKill ? RandomDamageDistributionAlgorithm.NEAREST_KILL : RandomDamageDistributionAlgorithm.getDefault();
            left = ((IDamageDistribution)damageDistribution2).htweaks$distributeDamage(left, player, source, addStat);
            if (left > 0.0F && !hasTriedNoKill) {
                damageDistribution2 = RandomDamageDistributionAlgorithm.NEAREST_KILL;
                left = ((IDamageDistribution)damageDistribution2).htweaks$distributeDamage(left, player, source, addStat);
            }
        }

        PlayerDamageModel before = new PlayerDamageModel();
        before.deserializeNBT(beforeCache);

        if (((IEntityDamageModel)damageModel).isDead(player)) {
            player.die(source);
        }

        if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
            FirstAid.LOGGER.info(LoggingMarkers.DAMAGE_DISTRIBUTION, "--- DONE! {} still left ---", left);
        }

        return left;

    }

    public static float applyArmor(@Nonnull LivingEntity entity, @Nonnull ItemStack itemStack, @Nonnull DamageSource source, float damage, @Nonnull EquipmentSlot slot) {
        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            return damage;
        } else {
            Item item = itemStack.getItem();
            float totalArmor = 0.0F;
            float totalToughness = 0.0F;
            if (item instanceof ArmorItem) {
                totalArmor = (float)FirstAidUtils.getValueFromAttributes(Attributes.ARMOR, slot, itemStack);
                totalToughness = (float)FirstAidUtils.getValueFromAttributes(Attributes.ARMOR_TOUGHNESS, slot, itemStack);
                totalArmor = (float)ArmorUtils.applyArmorModifier(slot, (double)totalArmor);
                totalToughness = (float)ArmorUtils.applyToughnessModifier(slot, (double)totalToughness);
            }

            totalArmor = (float)((double)totalArmor + FirstAidUtils.getGlobalRestAttribute(entity, Attributes.ARMOR));
            totalToughness = (float)((double)totalToughness + FirstAidUtils.getGlobalRestAttribute(entity, Attributes.ARMOR_TOUGHNESS));
            if (damage > 0.0F && (totalArmor > 0.0F || totalToughness > 0.0F)) {
                if (item instanceof ArmorItem && (!source.is(DamageTypeTags.IS_FIRE) || !item.isFireResistant())) {
                    int itemDamage = Math.max((int)damage, 1);
                    itemStack.hurtAndBreak(itemDamage, entity, (player) -> {
                        player.broadcastBreakEvent(slot);
                    });
                }

                damage = CombatRules.getDamageAfterAbsorb(damage, totalArmor, totalToughness);
            }

            return damage;
        }
    }

    private static double getValueFromAttributes(Attribute attribute, EquipmentSlot slot, ItemStack stack) {
        return stack.getAttributeModifiers(slot).get(attribute).stream().mapToDouble(AttributeModifier::getAmount).sum();
    }

    private static double getGlobalRestAttribute(LivingEntity player, Attribute attribute) {
        double sumOfAllAttributes = 0.0;
        EquipmentSlot[] var4 = CommonUtils.ARMOR_SLOTS;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            EquipmentSlot slot = var4[var6];
            ItemStack otherStack = player.getItemBySlot(slot);
            sumOfAllAttributes += getValueFromAttributes(attribute, slot, otherStack);
        }

        double all = player.getAttributeValue(attribute);
        if (!DoubleMath.fuzzyEquals(sumOfAllAttributes, all, 0.001)) {
            double diff = all - sumOfAllAttributes;
            if ((Boolean)FirstAidConfig.GENERAL.debug.get()) {
                FirstAid.LOGGER.info("Attribute value for {} does not match sum! Diff is {}, distributing to all!", ForgeRegistries.ATTRIBUTES.getKey(attribute), diff);
            }

            return diff;
        } else {
            return 0.0;
        }
    }

    public static float applyEnchantmentModifiers(LivingEntity livingEntity, EquipmentSlot slot, DamageSource source, float damage) {
        FirstAidConfig.Server.ArmorEnchantmentMode enchantmentMode = (FirstAidConfig.Server.ArmorEnchantmentMode)FirstAidConfig.SERVER.armorEnchantmentMode.get();
        int k;
        if (enchantmentMode == FirstAidConfig.Server.ArmorEnchantmentMode.LOCAL_ENCHANTMENTS) {
            ItemStack itemStackFromSlot = livingEntity.getItemBySlot(slot);
            MutableInt mutableInt = new MutableInt();
            EnchantmentHelper.runIterationOnItem((enchantment, level) -> {
                int val = enchantment.getDamageProtection(level, source);
                List<? extends String> resourceLocation = (List)FirstAidConfig.SERVER.enchMulOverrideResourceLocations.get();
                List<? extends Integer> multiplierOverride = (List)FirstAidConfig.SERVER.enchMulOverrideMultiplier.get();
                String enchantRlAsString = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
                int multiplier = (Integer)FirstAidConfig.SERVER.enchantmentMultiplier.get();
                boolean debug = (Boolean)FirstAidConfig.GENERAL.debug.get();
                if (debug) {
                    FirstAid.LOGGER.info("Searching for enchantment multiplier override for {}, base is {}", enchantRlAsString, multiplier);
                }

                for(int i = 0; i < Math.min(resourceLocation.size(), multiplierOverride.size()); ++i) {
                    String s = (String)resourceLocation.get(i);
                    if (s.equals(enchantRlAsString)) {
                        multiplier = (Integer)multiplierOverride.get(i);
                        if (debug) {
                            FirstAid.LOGGER.info("Found enchantment multiplier override for {}, new value is {}", enchantRlAsString, multiplier);
                        }
                        break;
                    }
                }

                mutableInt.add(val * multiplier);
            }, itemStackFromSlot);
            k = mutableInt.getValue();
        } else {
            if (enchantmentMode != FirstAidConfig.Server.ArmorEnchantmentMode.GLOBAL_ENCHANTMENTS) {
                throw new RuntimeException("What dark magic is " + enchantmentMode);
            }

            k = EnchantmentHelper.getDamageProtection(livingEntity.getArmorSlots(), source);
        }

        if (k > 0) {
            damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
        }

        return damage;
    }

    public static float applyGlobalPotionModifiers(LivingEntity livingEntity, DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            return damage;
        } else {
            if (livingEntity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && source != livingEntity.damageSources().fellOutOfWorld()) {
                int i = (livingEntity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * (Integer)FirstAidConfig.SERVER.resistanceReductionPercentPerLevel.get();
                int j = 100 - i;
                float f = damage * (float)j;
                float f1 = damage;
                damage = Math.max(f / 100.0F, 0.0F);
            }

            return damage;
        }
    }
}
