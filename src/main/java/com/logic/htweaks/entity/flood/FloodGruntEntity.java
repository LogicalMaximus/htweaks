package com.logic.htweaks.entity.flood;

import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.entity.BaseTaczFactionEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class FloodGruntEntity extends AbstractFloodTrooper {
    public static final String LOOT_TABLE = "grunt";

    public FloodGruntEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public String getLootTableName() {
        return LOOT_TABLE;
    }

    @Override
    protected void applyArmor() {

    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        double random = this.random.nextDouble();

        if(random <= HTServerConfig.GRUNT_EXPLODE_CHANCE.get()) {
            this.explode();
        }
    }

    private void explode() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)HTServerConfig.GRUNT_EXPLOSION_RADIUS.get(), Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = BaseTaczFactionEntity.createAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
        builder = builder.add(Attributes.MAX_HEALTH, 35.0);
        builder = builder.add(Attributes.ARMOR, 20.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 6.0);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.0);
        return builder;
    }
}
