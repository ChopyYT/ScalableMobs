package com.thomp.scalablemobs.config

import net.neoforged.fml.loading.FMLPaths
import java.io.File
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object ConfigManager {
    val LOGGER: Logger = LogManager.getLogger()
    private val configFile: File = File(FMLPaths.CONFIGDIR.get().toFile(), "scalablemobs.toml")
    var data: ConfigData = ConfigData()

    fun load() {
        if (!configFile.exists()) {
            save()
            return
        }

        try {
            val lines = configFile.readLines()
            for (line in lines) {
                val trimmed = line.trim()
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue

                val parts = trimmed.split("=", limit = 2)
                if (parts.size != 2) continue

                val key = parts[0].trim()
                val value = parts[1].trim()

                when (key) {
                    "maxMobLevel" -> data.maxMobLevel = value.toIntOrNull() ?: data.maxMobLevel
                    "distancePerLevel" -> data.distancePerLevel = value.toIntOrNull() ?: data.distancePerLevel
                    "healthMultiplier" -> data.healthMultiplier = value.toDoubleOrNull() ?: data.healthMultiplier
                    "damageMultiplier" -> data.damageMultiplier = value.toDoubleOrNull() ?: data.damageMultiplier
                    "baseHeight" -> data.baseHeight = value.toIntOrNull() ?: data.baseHeight
                    "reactiveMobName" -> data.reactiveMobName = value.toBooleanStrictOrNull() ?: data.reactiveMobName
                    "excludeMobs" -> {
                        val clean = value.removePrefix("[").removeSuffix("]").split(",")
                        data.excludeMobs = clean.map { it.trim().removeSurrounding("\"") }.toMutableList()
                    }
                    "enderDragonLevel" -> data.enderDragonLevel = value.toIntOrNull() ?: data.enderDragonLevel
                }
            }

            LOGGER.info("[ScalableMobs] Config Loaded")
        } catch (e: Exception) {
            LOGGER.error("[ScalableMobs] Failed to load config", e)
            save()
        }
    }

    fun save() {
        try {
            val sb = StringBuilder()
            sb.appendLine("# ScalableMobs Configuration")
            sb.appendLine("")
            sb.appendLine("#baseHeight: The start height where the mobs can spawn with level, ex: at y=60 the mob will be lv 1, but at y=44 will be lv 2")
            sb.appendLine("baseHeight = ${data.baseHeight}")
            sb.appendLine("#distancePerLevel: The distance(in blocks) between levels")
            sb.appendLine("distancePerLevel = ${data.distancePerLevel}")
            sb.appendLine("#maxMobLevel: Defines the max level mobs can be")
            sb.appendLine("maxMobLevel = ${data.maxMobLevel}")
            sb.appendLine("#healthMultiplier: The multiplier of the health on each level")
            sb.appendLine("healthMultiplier = ${data.healthMultiplier}")
            sb.appendLine("#damageMultiplier: The multiplier of the damage on each level")
            sb.appendLine("damageMultiplier = ${data.damageMultiplier}")
            sb.appendLine("#reactiveMobName: Allows to you turn on/off the reactiveNames mechanic (the health changes if they get damaged), this allows to put nametags on mobs")
            sb.appendLine("reactiveMobName = ${data.reactiveMobName}")
            sb.appendLine("#excludeMobs: List of mobs who would not have level (or will be lv 1)")
            sb.appendLine("excludeMobs = [${data.excludeMobs.joinToString(", ") { "\"$it\"" }}]")
            sb.appendLine("#enderDragonLevel: Allows to put an specific level to the Ender Dragon")
            sb.appendLine("enderDragonLevel = ${data.enderDragonLevel}")
            sb.appendLine("#enderDragonLevel: Allows to put an specific level to the Wither")
            sb.appendLine("witherBossLevel = ${data.witherBossLevel}")

            configFile.parentFile.mkdirs()
            configFile.writeText(sb.toString())

            LOGGER.info("[ScalableMobs] Config Saved")
        } catch (e: Exception) {
            LOGGER.error("[ScalableMobs] Failed to save config", e)
        }
    }
}
