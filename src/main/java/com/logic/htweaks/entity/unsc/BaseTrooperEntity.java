package com.logic.htweaks.entity.unsc;

import com.corrinedev.tacznpcs.Config;
import com.corrinedev.tacznpcs.common.entity.PatchItem;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.registries.ModAttributes;
import com.tacz.guns.item.ModernKineticGunItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraftforge.fml.ModList;


public abstract class BaseTrooperEntity extends BaseTaczFactionEntity {

    protected BaseTrooperEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);

        ((IScavFaction)this).htweaks$setFaction(Factions.UNSC);
    }
}
