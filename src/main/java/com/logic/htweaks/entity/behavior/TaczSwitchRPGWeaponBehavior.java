package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.mojang.datafixers.util.Pair;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.resource.index.CommonGunIndex;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.HashMap;
import java.util.List;

public class TaczSwitchRPGWeaponBehavior<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {

    private HashMap<GunTabType, ItemStack> guns;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        guns = new HashMap<>();

        SimpleContainer inventory = entity.getInventory();

        for(int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            GunTabType type = this.heldGunType(itemStack);

            if(type != null) {
                guns.put(type, itemStack);
            }
        }

        if(guns.size() < 2)
            return false;

        if(BrainUtils.getTargetOfEntity(entity).getVehicle() != null && entity.heldGunType() != GunTabType.RPG) {
            return true;
        }
        else if(BrainUtils.getTargetOfEntity(entity).getVehicle() == null && entity.heldGunType() == GunTabType.RPG) {
            return true;
        }

        return false;
    }

    public void start(E entity) {
        if(BrainUtils.getTargetOfEntity(entity).getVehicle() != null && guns.containsKey(GunTabType.RPG) && entity.heldGunType() != GunTabType.RPG) {
            ItemStack switchGun = guns.keySet().stream().filter(type -> type == GunTabType.RPG).findFirst().map(type -> guns.get(type)).orElse(null);

            if(switchGun != null && entity.getInventory().hasAnyMatching((e) -> e == switchGun)) {
                entity.setItemInHand(InteractionHand.MAIN_HAND, switchGun);
            }
        }
        else if(BrainUtils.getTargetOfEntity(entity).getVehicle() == null && entity.heldGunType() == GunTabType.RPG && guns.keySet().stream().anyMatch((e) -> e != GunTabType.RPG)) {
            ItemStack switchGun = guns.keySet().stream().filter(type -> type != GunTabType.RPG).findFirst().map(type -> guns.get(type)).orElse(null);

            if(switchGun != null && entity.getInventory().hasAnyMatching((e) -> e == switchGun)) {
                entity.setItemInHand(InteractionHand.MAIN_HAND, switchGun);
            }
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)});
    }

    public GunTabType heldGunType(ItemStack stack) {
        Item var2 = stack.getItem();
        if (var2 instanceof ModernKineticGunItem gun) {
            if (TimelessAPI.getCommonGunIndex(gun.getGunId(stack)).isPresent()) {
                GunTabType var10000;
                switch (((CommonGunIndex)TimelessAPI.getCommonGunIndex(gun.getGunId(stack)).get()).getType()) {
                    case "pistol":
                        var10000 = GunTabType.PISTOL;
                        break;
                    case "rifle":
                        var10000 = GunTabType.RIFLE;
                        break;
                    case "sniper":
                        var10000 = GunTabType.SNIPER;
                        break;
                    case "smg":
                        var10000 = GunTabType.SMG;
                        break;
                    case "rpg":
                        var10000 = GunTabType.RPG;
                        break;
                    case "shotgun":
                        var10000 = GunTabType.SHOTGUN;
                        break;
                    case "mg":
                        var10000 = GunTabType.MG;
                        break;
                    default:
                        ResourceLocation var10002 = gun.getGunId(stack);
                        throw new IllegalStateException("Unexpected value: " + ((CommonGunIndex)TimelessAPI.getCommonGunIndex(var10002).get()).getType());
                }

                return var10000;
            }
        }

        return null;
    }
}
