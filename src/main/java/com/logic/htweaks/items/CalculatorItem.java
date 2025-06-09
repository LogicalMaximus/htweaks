package com.logic.htweaks.items;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.MiltaryCommanderManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CalculatorItem extends Item {
    public CalculatorItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide) {
            MiltaryCommanderManager miltaryCommanderManager = Htweaks.getMiltaryCommanderManager();

            if(miltaryCommanderManager != null) {
                for(MiltaryAiCommander commander : miltaryCommanderManager.commanders) {
                    player.sendSystemMessage(Component.literal(commander.getFaction().getDisplayName() + " Miltary Commander Has " + commander.getResources()));
                }
            }
        }

        return super.use(level, player, hand);
    }
}
