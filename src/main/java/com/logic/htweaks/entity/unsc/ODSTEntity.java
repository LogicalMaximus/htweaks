package com.logic.htweaks.entity.unsc;

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

public class ODSTEntity extends BaseTrooperEntity {
    public static final String LOOT_TABLE = "marine";

    public ODSTEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike) HaloMdeModItems.ODST_ARMOR_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.ODST_ARMOR_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)HaloMdeModItems.ODST_ARMOR_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)HaloMdeModItems.ODST_ARMOR_BOOTS.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
        builder = builder.add(Attributes.MAX_HEALTH, 30.0);
        builder = builder.add(Attributes.ARMOR, 0.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.0);
        return builder;
    }
}
