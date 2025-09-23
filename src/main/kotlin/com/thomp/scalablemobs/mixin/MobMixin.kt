package com.thomp.scalablemobs.mixin

import com.thomp.scalablemobs.config.ConfigManager
import net.minecraft.network.chat.TextColor
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.boss.wither.WitherBoss
import net.minecraft.world.entity.monster.Enemy
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Mob::class)
class MobMixin {

    @Unique
    private var scalableLevel: Int = -1

    @Unique
    private var scalableBaseName: String = ""
    //this affects to spawned mobs or /summon
    @Inject(method = ["finalizeSpawn"], at = [At("RETURN")])
    fun onSpawnInject(ci: CallbackInfoReturnable<SpawnGroupData?>) {
        val mob = this as Mob
        val mobId = net.minecraft.world.entity.EntityType.getKey(mob.type)?.toString() ?: ""
        if (ConfigManager.data.excludeMobs.contains(mobId)) {
            scalableLevel = 1
        } else {
            val level = mob.level()
            if (level is ServerLevel) {
                val referenceY = ConfigManager.data.baseHeight
                val dy = referenceY - mob.blockY
                if (dy > 0) {
                    val levelFromDepth = (dy / ConfigManager.data.distancePerLevel) + 1
                    scalableLevel = Mth.clamp(levelFromDepth, 1, ConfigManager.data.maxMobLevel)
                } else {
                    scalableLevel = 1
                    }
                }
            }
        if (mob is WitherBoss) {
            scalableLevel = ConfigManager.data.witherBossLevel
        } else if (mob is EnderDragon){
            scalableLevel = ConfigManager.data.enderDragonLevel
        }
        if (mob is Enemy || mob is WitherBoss) {
            scalableBaseName = mob.type.description.string
            mob.persistentData.putInt("scalablemobs:level", scalableLevel)
            mob.persistentData.putString("scalablemobs:base_name", scalableBaseName)
            mob.customName = Component.literal("$scalableBaseName [")
                .append(Component.literal("Lv$scalableLevel").withStyle { it.withColor(TextColor.fromRgb(0xFFFF55)) })
                .append(Component.literal("]"))

            val healthModifier = AttributeModifier(
                ResourceLocation.tryParse("scalablemobs:level_health")!!,
                scalableLevel * ConfigManager.data.healthMultiplier,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
            val damageModifier = AttributeModifier(
                ResourceLocation.tryParse("scalablemobs:level_damage")!!,
                scalableLevel * ConfigManager.data.damageMultiplier,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
            mob.getAttribute(Attributes.MAX_HEALTH)?.addOrReplacePermanentModifier(healthModifier)
            mob.getAttribute(Attributes.ATTACK_DAMAGE)?.addOrReplacePermanentModifier(damageModifier)
            mob.health = mob.getAttribute(Attributes.MAX_HEALTH)?.value?.toFloat() ?: mob.health
            }
        }


    @Inject(method = ["addAdditionalSaveData"], at = [At("HEAD")])
    fun saveData(tag: CompoundTag, ci: CallbackInfo) {
        tag.putInt("scalablemobs:level", scalableLevel)
        tag.putString("scalablemobs:base_name", scalableBaseName)
    }

    @Inject(method = ["readAdditionalSaveData"], at = [At("HEAD")])
    fun readData(tag: CompoundTag, ci: CallbackInfo) {
        if (tag.contains("scalablemobs:level")) {
            scalableLevel = tag.getInt("scalablemobs:level")
        }
        if (tag.contains("scalablemobs:base_name")) {
            scalableBaseName = tag.getString("scalablemobs:base_name")
        }
    }
    @Inject(method = ["tick"], at = [At("HEAD")])
    fun onTickInject(ci: CallbackInfo) {
        val mob = this as Mob

        if (mob.tickCount % 3 != 0) return
        scalableLevel = mob.persistentData.getInt("scalablemobs:level")
        scalableBaseName = mob.persistentData.getString("scalablemobs:base_name")
        if (scalableLevel <= 0) return

        val maxHealth = mob.getAttribute(Attributes.MAX_HEALTH)?.value ?: mob.health.toDouble()
        val currentHealth = mob.health.toInt()
        val levelText = "Lv$scalableLevel"
        val name = if (!scalableBaseName.isEmpty()) scalableBaseName else mob.type.description.string
        val currentHealthText = currentHealth.toString()
        val maxHealthText = maxHealth.toInt().toString()

        if (ConfigManager.data.reactiveMobName) {
            mob.customName = Component.literal("$name [")
                .append(Component.literal(levelText).withStyle { it.withColor(TextColor.fromRgb(0xFFFF55)) })
                .append(Component.literal("] ["))
                .append(Component.literal(currentHealthText).withStyle { it.withColor(TextColor.fromRgb(0xFF5555)) })
                .append(Component.literal("/"))
                .append(Component.literal(maxHealthText).withStyle { it.withColor(TextColor.fromRgb(0xFF5555)) })
                .append(Component.literal("]"))
        }
    }
}

