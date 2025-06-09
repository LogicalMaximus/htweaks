package com.logic.htweaks.entity.covenant;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.entity.behavior.TaczUseShield;
import com.logic.htweaks.registries.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;

public class CovenantJackalEntity extends BaseCovenantTrooperEntity {
    private static final String LOOT_TABLE = "jackal";

    public CovenantJackalEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {
    }

    public BrainActivityGroup<? extends AbstractScavEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks((new TaczUseShield()).stopIf((e) -> this.getTarget() == null));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.28);
        builder = builder.add(Attributes.MAX_HEALTH, 28.0);
        builder = builder.add(Attributes.ARMOR, 15.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.0);
        return builder;
    }
}
