package com.logic.htweaks.items;

import com.logic.htweaks.client.ClientHooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public class SquadSpawner extends Item{

    public SquadSpawner(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()) {
            int factionID = 0;

            CompoundTag tag = player.getMainHandItem().getOrCreateTag();

            if(tag.contains("factionID")) {
                factionID = tag.getInt("factionID");
            }
            else {
                factionID = 1;
                tag.putInt("factionID", factionID);
            }

            if(player.isCrouching()) {
                factionID++;

                if(factionID > 3) {
                    factionID = 1;

                    tag.putInt("factionID", factionID);
                }
                else {
                    tag.putInt("factionID", factionID);
                }

                player.sendSystemMessage(Component.literal("Faction Is Now: " + factionID));
            }
        }
        else {
            if(!player.isCrouching()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHooks::openSquadSpawnerScreen);
            }
        }

        return super.use(level, player, hand);
    }

    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide) {
            int factionID = 0;

            CompoundTag tag = player.getMainHandItem().getOrCreateTag();

            if(tag.contains("factionID")) {
                factionID = tag.getInt("factionID");
            }
            else {
                factionID = 1;
                tag.putInt("factionID", factionID);
            }

            if(player.isCrouching()) {
                factionID++;

                if(factionID > 2) {
                    factionID = 1;

                    tag.putInt("factionID", factionID);
                }
                else {
                    tag.putInt("factionID", factionID);
                }

                player.sendSystemMessage(Component.literal("Faction Is Now: " + factionID));
            }
            else {
                spawnSquad(factionID, player.blockPosition(), level);
            }
        }

        return super.use(level, player, hand);
    }

     */

}
