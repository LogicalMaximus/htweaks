package com.logic.htweaks.compat.enhancedvisuals;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.client.VisualManager;

import java.util.List;
import java.util.function.Predicate;

public class SuppressionHandler extends VisualHandler {
    public VisualType suppressed = new VisualTypeBlur("suppressed");

    public Visual suppressedVisual;

    public double defaultIntensity = (double)0.0F;

    public double mediumIntensity = 0.2;

    public double maxIntensity = 0.3;

    public double fadeFactor = 0.005;

    public void tick(@Nullable Player player) {
        if(this.suppressedVisual == null) {
            this.suppressedVisual = new Visual(suppressed, this, 0);

            this.suppressedVisual.setOpacityInternal(0.0F);
            VisualManager.add(this.suppressedVisual);

        }

        boolean isSuppressed = false;
        double aimedSuppressed = this.defaultIntensity;

            if(player != null) {
                List<Projectile> projectiles = EntityRetrievalUtil.getEntities(player.level(), player.getBoundingBox().inflate((double)7.0F), (target) -> {
                    boolean var10000;
                    if (target instanceof Projectile projectile) {
                        if (!projectile.onGround() && !projectile.horizontalCollision && !projectile.verticalCollision && player.getBoundingBox().clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement().multiply((double) 3.0F, (double) 3.0F, (double) 3.0F))).isPresent()) {
                            var10000 = true;
                            return var10000;
                        }
                    }

                    var10000 = false;
                    return var10000;
                });

                if(!projectiles.isEmpty()) {
                    isSuppressed = true;

                    for(Projectile projectile : projectiles) {
                        aimedSuppressed += 0.2;
                    }
                }
            }

            if(isSuppressed) {
                if ((double)this.suppressedVisual.getOpacityInternal() < aimedSuppressed) {
                    this.suppressedVisual.setOpacityInternal((float)Math.min((double)this.suppressedVisual.getOpacityInternal() + this.fadeFactor, aimedSuppressed));
                } else if ((double)this.suppressedVisual.getOpacityInternal() > aimedSuppressed) {
                    this.suppressedVisual.setOpacityInternal((float)Math.max((double)this.suppressedVisual.getOpacityInternal() - this.fadeFactor, aimedSuppressed));
                }
            }

        }
}
