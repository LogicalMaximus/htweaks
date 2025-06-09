package com.logic.htweaks.entity.covenant;

import com.logic.htweaks.entity.BaseTaczFactionEntity;
import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class CovenantEliteEntity extends BaseCovenantTrooperEntity {
    public static final String LOOT_TABLE = "elite";


    public CovenantEliteEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_ELITE_ARMOR_MINOR_BOOTS.get()));
    }

    public SoundEvent getAmbientSound() {
        return (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("halo_mde:elite_ambient"));
    }

    public SoundEvent getHurtSound(DamageSource ds) {
        return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.hurt"));
    }

    public SoundEvent getDeathSound() {
        return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("halo_mde:elite_death"));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
        builder = builder.add(Attributes.MAX_HEALTH, 35.0);
        builder = builder.add(Attributes.ARMOR, 12.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.0);
        return builder;
    }
}
