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
import com.logic.htweaks.entity.unsc.UNSCMarineEntity;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModEntities;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class HumanSquadSpawnerUtils {

    private static final Random random = new Random();

    public static AbstractScavEntity spawnTank(Patrol patrol, int factionID, BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Yx100Entity> vehicleType = com.atsuishio.superbwarfare.init.ModEntities.YX_100.get();

        Yx100Entity vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity tankCommander = createPatrolLeader(patrol, factionID, pos, level, MemberType.ENGINEER, leader, vehicle);
        createPatrolMember(patrol, factionID, tankCommander, pos, MemberType.ENGINEER, vehicle);

        return tankCommander;
    }

    public static AbstractScavEntity spawnWarthog(Patrol patrol, int factionID, BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Lav150Entity> vehicleType = com.atsuishio.superbwarfare.init.ModEntities.LAV_150.get();

        Lav150Entity vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity warthogCommander = createPatrolLeader(patrol, factionID, pos, level, MemberType.ENGINEER, leader, vehicle);
        createPatrolMember(patrol,  factionID, warthogCommander, pos, MemberType.ENGINEER, vehicle);

        return warthogCommander;
    }

    public static AbstractScavEntity spawnAPC(Patrol patrol, int factionID, BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Bmp2Entity> vehicleType = com.atsuishio.superbwarfare.init.ModEntities.BMP_2.get();

        Bmp2Entity vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity apcCommander = createPatrolLeader(patrol, factionID, pos, level, MemberType.ENGINEER, leader, vehicle);
        createPatrolMember(patrol, factionID, apcCommander, pos, MemberType.ENGINEER, vehicle);

        return apcCommander;
    }

    public static AbstractScavEntity spawnMortar(Patrol patrol, int factionID, BlockPos pos, Level level, @Nullable AbstractScavEntity leader) {
        EntityType<Pig> vehicleType;

        if(factionID == 1 || factionID == 2) {
            vehicleType = EntityType.PIG;
        }
        else {
            //YES IK
            vehicleType = EntityType.PIG;
        }

        Pig vehicle = vehicleType.create(level);

        vehicle.setPos(pos.getCenter());
        level.addFreshEntity(vehicle);

        AbstractScavEntity mortarOperator = createPatrolLeader(patrol, factionID, pos, level, MemberType.RIFLEMAN, leader, vehicle);
        createPatrolMember(patrol, factionID, mortarOperator, pos, MemberType.RIFLEMAN, vehicle);

        return mortarOperator;
    }

    public static void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level, @Nullable Entity vehicle) {
        AbstractScavEntity squadLeader = createPatrolLeader(patrol, factionID, pos, level, MemberType.SCOUT,null, vehicle);
        spawnSquadLeaderTeam(patrol, factionID, squadLeader, vehicle, level, false);

        spawnFireteam(patrol, factionID, pos, level, squadLeader,vehicle, true );
        spawnFireteam(patrol, factionID, pos, level, squadLeader, vehicle,false );

    }

    public static void spawnSquadLeaderTeam(Patrol patrol, int factionID, AbstractScavEntity squadLeader, @Nullable Entity vehicle, Level level, boolean isAT) {
        createPatrolMember(patrol, factionID, squadLeader, squadLeader.blockPosition(), MemberType.RIFLEMAN, vehicle);
        createPatrolMember(patrol, factionID, squadLeader, squadLeader.blockPosition(), MemberType.MARKSMAN, vehicle);

        if(isAT) {
            createPatrolMember(patrol, factionID, squadLeader, squadLeader.blockPosition(), MemberType.AT, vehicle);
        }
        else {
            createPatrolMember(patrol, factionID, squadLeader, squadLeader.blockPosition(), MemberType.MG, vehicle);
        }
    }

    public static void spawnFireteam(Patrol patrol, int factionID, BlockPos pos, Level level, AbstractScavEntity commander, @Nullable Entity vehicle, boolean isAT) {
        AbstractScavEntity teamLeader = createPatrolLeader(patrol, factionID, pos, level, MemberType.SCOUT, commander, vehicle);
        createPatrolMember(patrol, factionID, teamLeader, pos,MemberType.RIFLEMAN, vehicle);
        createPatrolMember(patrol, factionID, teamLeader, pos, MemberType.MARKSMAN, vehicle);

        if(isAT) {
            createPatrolMember(patrol, factionID, teamLeader, pos, MemberType.AT, vehicle);
        }
        else {
            createPatrolMember(patrol, factionID, teamLeader, pos, MemberType.MG, vehicle);
        }
    }

    public static void spawnSniperTeam(Patrol patrol, int factionID, BlockPos pos, Level level, AbstractScavEntity commander, @Nullable Entity vehicle) {
        AbstractScavEntity teamLeader = createPatrolLeader(patrol, factionID, pos, level, MemberType.SNIPER, commander, vehicle);
        createPatrolMember(patrol, factionID, teamLeader, pos, MemberType.SNIPER, vehicle);
    }

    public static void spawnMGTeam(Patrol patrol, int factionID, BlockPos pos, Level level, AbstractScavEntity commander, @Nullable Entity vehicle) {
        AbstractScavEntity teamLeader = createPatrolLeader(patrol, factionID, pos, level, MemberType.MG, commander, vehicle);
        createPatrolMember(patrol, factionID, teamLeader, pos, MemberType.RIFLEMAN, vehicle);
    }

    public static void spawnATTeam(Patrol patrol, int factionID, BlockPos pos, Level level, AbstractScavEntity commander, @Nullable Entity vehicle) {
        AbstractScavEntity teamLeader = createPatrolLeader(patrol, factionID, pos, level, MemberType.AT, commander, vehicle);
        createPatrolMember(patrol, factionID, teamLeader, pos, MemberType.RIFLEMAN, vehicle);
    }

    public static AbstractScavEntity createPatrolLeader(Patrol patrol, int factionID, BlockPos pos, Level level, MemberType type, @Nullable AbstractScavEntity commander, @Nullable Entity vehicle) {
        AbstractScavEntity patrolLeader = null;

        if(factionID == 1) {
            patrolLeader = ModEntities.UNSC_MARINE.get().create(level);
        }
        else if(factionID == 2) {
            patrolLeader = DutyEntity.DUTY.create(level);
        }


        if(patrolLeader != null) {
            if(type == MemberType.AT) {
                setInventory(patrolLeader, MemberType.RIFLEMAN.getName());
            }

            patrolLeader.setPersistenceRequired();

            setInventory(patrolLeader, type.getName());
            patrolLeader.setPos(pos.getCenter());

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

    public static void createPatrolMember(Patrol patrol, int factionID, AbstractScavEntity leader, BlockPos pos, MemberType type, @Nullable Entity vehicle) {
        if (factionID == 1) {
            UNSCMarineEntity patrolMember = ModEntities.UNSC_MARINE.get().create(leader.level());

            patrolMember.setPersistenceRequired();

            if(type == MemberType.AT) {
                setInventory(patrolMember, MemberType.RIFLEMAN.getName());
            }

            setInventory(patrolMember, type.getName());

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
        if(factionID == 2) {
            DutyEntity patrolMember = DutyEntity.DUTY.create(leader.level());

            patrolMember.setPersistenceRequired();

            if(type == MemberType.AT) {
                setInventory(patrolMember, MemberType.RIFLEMAN.getName());
            }

            setInventory(patrolMember, type.getName());

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
                ItemStack stack = AmmoItemBuilder.create().setId(new ResourceLocation("halo", "762x51")).build();

                stack.setCount(random.nextInt(1, 60));

                entity.inventory.addItem(stack);
            }
        }
    }


    public enum MemberType {
        MG("marine_mg"),
        RIFLEMAN("marine_rifleman"),
        AT("marine_at"),
        SNIPER("marine_sniper"),
        SHOTGUNNER("marine_shotgunner"),
        SCOUT("marine_scout"),
        MARKSMAN("marine_marksman"),
        ENGINEER("marine_engineer");

        private final String name;

        MemberType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
