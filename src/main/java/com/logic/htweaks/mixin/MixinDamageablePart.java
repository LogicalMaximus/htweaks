package com.logic.htweaks.mixin;

import com.logic.htweaks.bridge.IDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.debuff.IDebuff;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.common.damagesystem.DamageablePart;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(DamageablePart.class)
public abstract class MixinDamageablePart extends AbstractDamageablePart implements IDamageablePart {

    @Shadow(remap=false)
    private int maxHealth;

    @Shadow(remap=false)
    @Nullable
    private IDebuff[] debuffs;

    @Shadow(remap=false)
    private float absorption;

    public MixinDamageablePart(int maxHealth, boolean canCauseDeath, @NotNull EnumPlayerPart playerPart) {
        super(maxHealth, canCauseDeath, playerPart);
    }

    @Override
    public float htweaks$damage(float amount, @Nullable LivingEntity livingEntity, boolean applyDebuff, float minHealth) {
        if (amount <= 0.0F) {
            return 0.0F;
        } else if (minHealth > (float)this.maxHealth) {
            throw new IllegalArgumentException("Cannot damage part with minHealth " + minHealth + " while he has more max health (" + this.maxHealth + ")");
        } else {
            float origAmount = amount;
            if (this.absorption > 0.0F) {
                amount = Math.abs(Math.min(0.0F, this.absorption - amount));
                this.absorption = Math.max(0.0F, this.absorption - origAmount);
            }

            float notFitting = Math.abs(Math.min(minHealth, this.currentHealth - amount) - minHealth);
            this.currentHealth = Math.max(minHealth, this.currentHealth - amount);
            if (applyDebuff && this.debuffs != null) {
                Objects.requireNonNull(livingEntity, "Got null player with applyDebuff = true");
                IDebuff[] var7 = this.debuffs;
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    IDebuff debuff = var7[var9];
                    debuff.handleDamageTaken(origAmount - notFitting, this.currentHealth / (float)this.maxHealth, (ServerPlayer)livingEntity);
                }
            }

            return notFitting;
        }
    }
}
