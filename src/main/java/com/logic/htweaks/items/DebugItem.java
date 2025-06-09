package com.logic.htweaks.items;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;

import com.logic.htweaks.bridge.INPCSPatrol;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.waypoints.Waypoint;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity;
import com.atsuishio.superbwarfare.init.ModEntities;

import java.util.List;

public class DebugItem extends Item {

    public DebugItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide) {

            Bmp2Entity bmp = ModEntities.BMP_2.get().create(level);

            bmp.setPos(player.position());

            level.addFreshEntity(bmp);
        }

        return super.use(level, player, hand);
    }

}
