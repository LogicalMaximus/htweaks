package com.logic.htweaks.entity.unsc;

import com.corrinedev.tacznpcs.Config;
import com.corrinedev.tacznpcs.common.entity.PatchItem;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.registries.ModAttributes;
import com.tacz.guns.item.ModernKineticGunItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class UNSCMarineEntity extends BaseTrooperEntity {

    public static final String LOOT_TABLE = "marine";

    public UNSCMarineEntity(EntityType<? extends PathfinderMob> mob, Level level) {
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
