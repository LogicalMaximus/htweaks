package com.logic.htweaks.mixin;

import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements IScavFaction {

    @Unique
    private Faction faction = Factions.UNSC;

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public Faction htweaks$getFaction() {
        return faction;
    }

    @Override
    public void htweaks$setFaction(Faction faction) {
        this.faction = faction;
    }
}
