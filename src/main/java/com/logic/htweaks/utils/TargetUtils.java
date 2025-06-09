package com.logic.htweaks.utils;

import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.registries.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Random;

public class TargetUtils {
    private static Random random = new Random();

    private static final double SPREAD_MULTIPLIER = 2.5;

    public static double getInaccuracy(LivingEntity entity) {
        //AttributeInstance skillLevelAttribute = entity.getAttribute(ModAttributes.SKILL_LEVEL.get());
        double skillLevel = HTServerConfig.AI_ACCURACY.get();
        double inaccuracy = random.nextDouble();

        //if(skillLevelAttribute != null) {
            //skillLevel = skillLevelAttribute.getBaseValue();
        //}

        inaccuracy = inaccuracy - skillLevel;

        double spread = 0;

        if(inaccuracy > 0) {
            spread = random.nextDouble(-inaccuracy, inaccuracy) * SPREAD_MULTIPLIER;
        }

        return spread;
    }
}
