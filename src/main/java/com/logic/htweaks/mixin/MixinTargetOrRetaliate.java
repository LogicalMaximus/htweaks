package com.logic.htweaks.mixin;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.MiltaryCommanderManager;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.registries.ModMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TargetOrRetaliate.class)
public abstract class MixinTargetOrRetaliate<E extends Mob> extends ExtendedBehaviour<E> {

    @Inject(method = "start(Lnet/minecraft/world/entity/Mob;)V", at = @At("TAIL"), remap = false)
    protected void start(E entity, CallbackInfo ci) {
        if(entity instanceof AbstractScavEntity scav) {
            MiltaryCommanderManager manager = Htweaks.getMiltaryCommanderManager();

            if(manager != null) {
                MiltaryAiCommander commander = manager.getCommander(((IScavFaction)scav).htweaks$getFaction());

                if(commander != null) {
                    List<BlockPos> enemyPositions = commander.getMemory(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get()).orElse(null);

                    LivingEntity target = BrainUtils.getTargetOfEntity(entity);

                    if(target != null) {
                        BlockPos pos = target.blockPosition();

                        if(enemyPositions != null && !enemyPositions.contains(pos)) {
                            enemyPositions.add(pos);

                            commander.setMemoryWithExpiry(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get(), enemyPositions, HTServerConfig.COMMANDER_REMEMBER_TIME.get());
                        }
                        else {
                            List<BlockPos> newEnemyPositions = new ArrayList<>();

                            newEnemyPositions.add(pos);

                            commander.setMemoryWithExpiry(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get(), newEnemyPositions, HTServerConfig.COMMANDER_REMEMBER_TIME.get());
                        }
                    }
                }
            }
        }
    }
}
