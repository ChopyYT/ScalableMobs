package com.thomp.scalablemobs.config

data class ConfigData(
    var maxMobLevel: Int = 30,
    var distancePerLevel: Int = 8,
    var healthMultiplier: Double = 0.13,
    var damageMultiplier: Double = 0.024,
    var baseHeight: Int = 60,
    var reactiveMobName: Boolean = true,
    var excludeMobs: MutableList<String> = mutableListOf(),
    var enderDragonLevel: Int = 50,
    var witherBossLevel: Int = 20,
)
