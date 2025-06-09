package com.logic.htweaks.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.teamabyssalofficial.dotf.config.DawnOfTheFloodConfig;
import net.teamabyssalofficial.util.WorldDataUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DestructionDevice extends Item {

    public DestructionDevice(Properties p_41383_) {
        super(p_41383_);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND && pLevel instanceof ServerLevel world) {
            WorldDataUtils worldDataRegistry = WorldDataUtils.getWorldDataRegistry(world);
            int currentScore = worldDataRegistry.getScore();
            worldDataRegistry.setScore(currentScore - Integer.MAX_VALUE);
            pPlayer.sendSystemMessage(Component.literal("What Have You Done?"));
            pLevel.playSound(null, pPlayer.blockPosition(), SoundEvent.createFixedRangeEvent(new ResourceLocation("dotf", "ambient/flood_horde_spawn9"), 1.0F), SoundSource.MASTER);
            pPlayer.getCooldowns().addCooldown(this, 20);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.htweaks.destruction_device").setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
    }
}
