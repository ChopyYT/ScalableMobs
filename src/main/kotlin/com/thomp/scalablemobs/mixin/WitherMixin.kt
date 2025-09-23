package com.thomp.scalablemobs.mixin

import com.thomp.scalablemobs.api.ScalableMobsAPI.setMobLevel
import com.thomp.scalablemobs.config.ConfigManager
import net.minecraft.world.level.Level
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.boss.wither.WitherBoss
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject

@Mixin(WitherBoss::class)
abstract class WitherMixin {

    @Inject(method = ["<init>"], at = [At("TAIL")])
    private fun onInit(entityType: EntityType<*>, level: Level, ci: CallbackInfo) {
        val wither = (this as WitherBoss)
        setMobLevel(wither, ConfigManager.data.witherBossLevel)
    }

    @Inject(method = ["customServerAiStep"], at = [At("HEAD")])
    private fun onTick(ci: CallbackInfo) {
        val wither = this as WitherBoss
        val health = wither.maxHealth
        if (wither.invulnerableTicks == 1) {
            wither.health = health
        }
    }
}