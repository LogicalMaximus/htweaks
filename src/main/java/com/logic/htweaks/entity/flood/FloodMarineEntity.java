package com.logic.htweaks.entity.flood;

import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class FloodMarineEntity extends AbstractFloodTrooper {
    public static final String LOOT_TABLE = "marine";

    public FloodMarineEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike) HaloMdeModItems.MARINE_ARMOR_GREEN_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.MARINE_ARMOR_GREEN_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)HaloMdeModItems.MARINE_ARMOR_GREEN_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)HaloMdeModItems.MARINE_ARMOR_GREEN_BOOTS.get()));
    }
}
