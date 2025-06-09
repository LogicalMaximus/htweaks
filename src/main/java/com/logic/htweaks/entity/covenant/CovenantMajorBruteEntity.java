package com.logic.htweaks.entity.covenant;

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

public class CovenantMajorBruteEntity extends CovenantBruteEntity{
    public CovenantMajorBruteEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public void applyArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_BRUTE_ARMOR_MAJOR_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_BRUTE_ARMOR_MAJOR_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_BRUTE_ARMOR_MAJOR_BOOTS.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.20);
        builder = builder.add(Attributes.MAX_HEALTH, 65.0);
        builder = builder.add(Attributes.ARMOR, 20.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 18.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 10.0);
        return builder;
    }
}
