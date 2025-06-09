package com.logic.htweaks.entity.flood;

import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.registries.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FloodJackalEntity extends AbstractFloodTrooper {

    private static final String LOOT_TABLE = "jackal";

    public FloodJackalEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
        this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ModItems.ENERGY_SHIELD_UNIT.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.30);
        builder = builder.add(Attributes.MAX_HEALTH, 40.0);
        builder = builder.add(Attributes.ARMOR, 18.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 8.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.0);
        return builder;
    }
}
