package com.logic.htweaks.entity.flood;

import com.logic.htweaks.entity.BaseTaczFactionEntity;
import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class FloodEliteEntity extends AbstractFloodTrooper {

    public static final String LOOT_TABLE = "elite";

    public FloodEliteEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike) HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_BOOTS.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.28);
        builder = builder.add(Attributes.MAX_HEALTH, 50.0);
        builder = builder.add(Attributes.ARMOR, 15.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 8.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 5.0);
        return builder;
    }
}
