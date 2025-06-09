package com.logic.htweaks.items;

import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class EnergyShieldUnit extends ShieldItem {
    public EnergyShieldUnit(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack p_43091_, ItemStack p_43092_) {
        return p_43092_.is(HaloMdeModItems.PLASMA_ENERGY_CELL.get()) || super.isValidRepairItem(p_43091_, p_43092_);
    }
}
