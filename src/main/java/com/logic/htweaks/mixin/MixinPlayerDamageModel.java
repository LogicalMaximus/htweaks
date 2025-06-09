package com.logic.htweaks.mixin;

import com.logic.htweaks.bridge.IEntityDamageModel;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.common.damagesystem.PlayerDamageModel;
import ichttt.mods.firstaid.common.registries.LookupReloadListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Iterator;

@Mixin(PlayerDamageModel.class)
public abstract class MixinPlayerDamageModel extends AbstractPlayerDamageModel implements LookupReloadListener, IEntityDamageModel {

    @Final
    @Shadow(remap=false)
    private boolean noCritical;

    public MixinPlayerDamageModel(AbstractDamageablePart head, AbstractDamageablePart leftArm, AbstractDamageablePart leftLeg, AbstractDamageablePart leftFoot, AbstractDamageablePart body, AbstractDamageablePart rightArm, AbstractDamageablePart rightLeg, AbstractDamageablePart rightFoot) {
        super(head, leftArm, leftLeg, leftFoot, body, rightArm, rightLeg, rightFoot);
    }

    @Override
    public boolean isDead(@Nullable Entity entity) {
         if (entity != null && !entity.isAlive()) {
            return true;
        } else if (!this.noCritical) {
            Iterator var6 = this.iterator();

            AbstractDamageablePart part;
            do {
                if (!var6.hasNext()) {
                    return false;
                }

                part = (AbstractDamageablePart)var6.next();
            } while(!part.canCauseDeath || !(part.currentHealth <= 0.0F));

            return true;
        } else {
            boolean dead = true;
            Iterator var4 = this.iterator();

            while(var4.hasNext()) {
                AbstractDamageablePart part = (AbstractDamageablePart)var4.next();
                if (part.currentHealth > 0.0F) {
                    dead = false;
                    break;
                }
            }

            return dead;
        }
    }
}
