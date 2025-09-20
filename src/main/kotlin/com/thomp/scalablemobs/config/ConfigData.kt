package com.thomp.scalablemobs.config

data class ConfigData(
    var maxMobLevel: Int = 30,
    var distancePerLevel: Int = 16,
    var healthMultiplier: Double = 0.2,
    var damageMultiplier: Double = 0.04,
    var baseHeight: Int = 60,
    var reactiveMobName: Boolean = true,
    var excludeMobs: MutableList<String> = mutableListOf("minecraft:warden", "minecraft:ender_dragon", "minecraft:wither")
)
