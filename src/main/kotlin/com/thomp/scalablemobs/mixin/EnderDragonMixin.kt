package com.thomp.scalablemobs.mixin

import com.thomp.scalablemobs.api.ScalableMobsAPI.setMobLevel
import com.thomp.scalablemobs.config.ConfigManager
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.level.Level
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Mob::class)
abstract class EnderDragonMixin {

    val mob = this as Mob

    @Inject(method = ["<init>"], at = [At("TAIL")])
    private fun onInit(entityType: EntityType<*>, level: Level, ci: CallbackInfo) {
        if (mob !is EnderDragon) return
        val dragon = mob as EnderDragon
        setMobLevel(dragon, ConfigManager.data.enderDragonLevel)
    }
}

