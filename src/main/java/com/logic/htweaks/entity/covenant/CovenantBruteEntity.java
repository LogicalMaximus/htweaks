package com.logic.htweaks.entity.covenant;

import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.entity.ICharge;
import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class CovenantBruteEntity extends BaseCovenantTrooperEntity implements ICharge {

    public static final String LOOT_TABLE = "brute";

    public CovenantBruteEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_BRUTE_ARMOR_MINOR_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.COVENANT_BRUTE_ARMOR_MINOR_CHESTPLATE.get()));
    }

    public SoundEvent getAmbientSound() {
        return (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("halo_mde:brute_ambient"));
    }

    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound((SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.step")), 0.15F, 1.0F);
    }

    public SoundEvent getHurtSound(DamageSource ds) {
        return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.hurt"));
    }

    public SoundEvent getDeathSound() {
        return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("halo_mde:brute_death"));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.23);
        builder = builder.add(Attributes.MAX_HEALTH, 50.0);
        builder = builder.add(Attributes.ARMOR, 15.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 15.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 7.0);
        return builder;
    }
}
