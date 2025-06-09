package com.logic.htweaks.utils;

import com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity;
import com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.DutyEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.INPCSPatrol;
import com.logic.htweaks.bridge.IPatrolLeader;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.entity.covenant.CovenantBruteEntity;
import com.logic.htweaks.entity.covenant.CovenantEliteEntity;
import com.logic.htweaks.entity.covenant.CovenantGruntEntity;
import com.logic.htweaks.entity.covenant.CovenantJackalEntity;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModEntities;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CovenantSquadSpawnerUtils {
    private static Random random = new Random();

    public static AbstractScavEntity spawnTank(Patrol patrol, BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Yx100Entity> vehicleType = com.atsuishio.superbwarfare.init.ModEntities.YX_100.get();

        Yx100Entity vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity tankCommander = createPatrolLeader(patrol, pos, level, leader, vehicle);
        createPatrolMember(patrol, tankCommander, pos, vehicle);

        return tankCommander;
    }

    public static AbstractScavEntity spawnWarthog(Patrol patrol,  BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Lav150Entity> vehicleType = com.atsuishio.superbwarfare.init.ModEntities.LAV_150.get();

        Lav150Entity vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity warthogCommander = createPatrolLeader(patrol, pos, level, leader, vehicle);
        createPatrolMember(patrol, warthogCommander, pos, vehicle);

        return warthogCommander;
    }

    public static AbstractScavEntity spawnAPC(Patrol patrol, BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Bmp2Entity> vehicleType = com.atsuishio.superbwarfare.init.ModEntities.BMP_2.get();

        Bmp2Entity vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity apcCommander = createPatrolLeader(patrol, pos, level, leader, vehicle);
        createPatrolMember(patrol, apcCommander, pos, vehicle);

        return apcCommander;
    }

    public static void spawnSquad(Patrol patrol, BlockPos pos, Level level, @Nullable Entity vehicle) {
        AbstractScavEntity squadLeader = createPatrolLeader(patrol, pos, level, null, vehicle);
        spawnSquadLeaderTeam(patrol, squadLeader, vehicle);

        spawnFireteam(patrol, pos, level, squadLeader,vehicle);
        spawnFireteam(patrol, pos, level, squadLeader, vehicle);
        spawnFireteam(patrol, pos, level, squadLeader,vehicle);
        spawnFireteam(patrol, pos, level, squadLeader, vehicle);
    }

    public static void spawnSquadLeaderTeam(Patrol patrol, AbstractScavEntity squadLeader, @Nullable Entity vehicle) {
        createPatrolMember(patrol, squadLeader, squadLeader.blockPosition(), vehicle);
        createPatrolMember(patrol, squadLeader, squadLeader.blockPosition(), vehicle);
        createPatrolMember(patrol, squadLeader, squadLeader.blockPosition(), vehicle);
        createPatrolMember(patrol, squadLeader, squadLeader.blockPosition(), vehicle);
        createPatrolMember(patrol, squadLeader, squadLeader.blockPosition(), vehicle);
    }

    public static void spawnFireteam(Patrol patrol, BlockPos pos, Level level, AbstractScavEntity commander, @Nullable Entity vehicle) {
        AbstractScavEntity teamLeader = createPatrolLeader(patrol, pos, level, commander, vehicle);
        createPatrolMember(patrol, teamLeader, pos, vehicle);
        createPatrolMember(patrol, teamLeader, pos, vehicle);
        createPatrolMember(patrol, teamLeader, pos, vehicle);
        createPatrolMember(patrol, teamLeader, pos, vehicle);
        createPatrolMember(patrol, teamLeader, pos, vehicle);
    }

    public static AbstractScavEntity createPatrolLeader(Patrol patrol, BlockPos pos, Level level, @Nullable AbstractScavEntity commander, @Nullable Entity vehicle) {
        BaseTaczFactionEntity patrolLeader = null;



        double chance = random.nextDouble();

        if(chance > 0.5) {
            patrolLeader = ModEntities.COVENANT_ELITE_MAJOR.get().create(level);
        }
        else {
            patrolLeader = ModEntities.COVENANT_BRUTE_MAJOR.get().create(level);
        }


        if(patrolLeader != null) {
            patrolLeader.setPos(pos.getCenter());

            patrolLeader.setPersistenceRequired();

            setInventory(patrolLeader, patrolLeader.getLootTableName());

            if(commander != null) {
                ((IPatrolLeader)patrolLeader).htweaks$setPatrolLeader(commander);
            }

            ((INPCSPatrol)patrolLeader).htweaks$setPatrol(patrol);

            level.addFreshEntity(patrolLeader);

            if(vehicle != null) {
                patrolLeader.startRiding(vehicle);
            }
        }

        return patrolLeader;
    }

    public static void createPatrolMember(Patrol patrol, AbstractScavEntity leader, BlockPos pos, @Nullable Entity vehicle) {
        double chance = random.nextDouble();

        if (chance > 0.75) {
            CovenantEliteEntity patrolMember = ModEntities.COVENANT_ELITE.get().create(leader.level());

            patrolMember.setPersistenceRequired();

            setInventory(patrolMember, patrolMember.getLootTableName());

            ((IPatrolLeader)patrolMember).htweaks$setPatrolLeader(leader);

            ((INPCSPatrol)patrolMember).htweaks$setPatrol(patrol);

            patrolMember.setPos(pos.getCenter());

            leader.level().addFreshEntity(patrolMember);

            if(vehicle != null) {
                patrolMember.startRiding(vehicle);
            }
        }
        else if (chance > 0.60) {
            CovenantBruteEntity patrolMember = ModEntities.COVENANT_BRUTE.get().create(leader.level());

            patrolMember.setPersistenceRequired();

            setInventory(patrolMember, patrolMember.getLootTableName());

            ((IPatrolLeader)patrolMember).htweaks$setPatrolLeader(leader);

            ((INPCSPatrol)patrolMember).htweaks$setPatrol(patrol);

            patrolMember.setPos(pos.getCenter());

            leader.level().addFreshEntity(patrolMember);

            if(vehicle != null) {
                patrolMember.startRiding(vehicle);
            }
        }
        else if(chance > 0.45) {
            CovenantJackalEntity patrolMember = ModEntities.COVENANT_JACKAL.get().create(leader.level());

            patrolMember.setPersistenceRequired();

            setInventory(patrolMember, patrolMember.getLootTableName());

            ((IPatrolLeader)patrolMember).htweaks$setPatrolLeader(leader);

            ((INPCSPatrol)patrolMember).htweaks$setPatrol(patrol);

            patrolMember.setPos(pos.getCenter());

            leader.level().addFreshEntity(patrolMember);

            if(vehicle != null) {
                patrolMember.startRiding(vehicle);
            }
        }
        else {
            CovenantGruntEntity patrolMember = ModEntities.COVENANT_GRUNT.get().create(leader.level());

            patrolMember.setPersistenceRequired();

            setInventory(patrolMember, patrolMember.getLootTableName());

            if(leader != null) {
                ((IPatrolLeader)patrolMember).htweaks$setPatrolLeader(leader);
            }

            ((INPCSPatrol)patrolMember).htweaks$setPatrol(patrol);

            patrolMember.setPos(pos.getCenter());

            leader.level().addFreshEntity(patrolMember);

            if(vehicle != null) {
                patrolMember.startRiding(vehicle);
            }
        }
    }

    public static void setInventory(AbstractScavEntity entity, String lootTableName) {
        if (entity.getServer() != null) {
            ObjectArrayList<ItemStack> stacks = entity.getServer().getLootData().getLootTable(new ResourceLocation(Htweaks.MODID, lootTableName)).getRandomItems((new LootParams.Builder(entity.getServer().getLevel(entity.level().dimension()))).create(LootContextParamSet.builder().build()));
            stacks.forEach((stack) -> {
                if (stack.getMaxDamage() != 0) {
                    stack.setDamageValue(RandomSource.create().nextInt(stack.getMaxDamage() / 2, stack.getMaxDamage()));
                }

                entity.inventory.addItem(stack);
            });

            int randAmount = random.nextInt(1,10);

            for(int i = 0; i < randAmount; i++) {
                ItemStack stack = AmmoItemBuilder.create().setId(new ResourceLocation("halo", "plasma")).build();

                stack.setCount(random.nextInt(1, 128));

                entity.inventory.addItem(stack);
            }
        }
    }

}
