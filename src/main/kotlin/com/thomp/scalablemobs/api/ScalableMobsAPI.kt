package com.thomp.scalablemobs.api

import com.thomp.scalablemobs.config.ConfigManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

object ScalableMobsAPI {

        @JvmStatic
        fun getMobLevel(mob: Mob): Int = mob.persistentData.getInt("scalablemobs:level")

        @JvmStatic
        fun setMobLevel(mob: Mob, level: Int) {
            mob.persistentData.putInt("scalablemobs:level", level)

            mob.getAttribute(Attributes.MAX_HEALTH)
                ?.removeModifier(ResourceLocation.tryParse("scalablemobs:level_health")!!)
            mob.getAttribute(Attributes.ATTACK_DAMAGE)
                ?.removeModifier(ResourceLocation.tryParse("scalablemobs:level_damage")!!)


            val healthModifier = AttributeModifier(
                ResourceLocation.tryParse("scalablemobs:level_health")!!,
                ConfigManager.data.healthMultiplier * level,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
            val damageModifier = AttributeModifier(
                ResourceLocation.tryParse("scalablemobs:level_damage")!!,
                 ConfigManager.data.damageMultiplier * level,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
            mob.getAttribute(Attributes.MAX_HEALTH)?.addOrReplacePermanentModifier(healthModifier)
            mob.getAttribute(Attributes.ATTACK_DAMAGE)?.addOrReplacePermanentModifier(damageModifier)

            mob.health = mob.getAttribute(Attributes.MAX_HEALTH)?.value?.toFloat() ?: mob.health
        }


        @JvmStatic
        fun getMobName(mob: Mob): String = mob.persistentData.getString("scalablemobs:base_name")

        @JvmStatic
        fun setMobName(mob: Mob, name: String) {
        mob.persistentData.putString("scalablemobs:base_name", name)
    }

        @JvmStatic
        fun isExcluded(mob: Mob): Boolean = ConfigManager.data.excludeMobs.contains(mob.type.toString())
    }

