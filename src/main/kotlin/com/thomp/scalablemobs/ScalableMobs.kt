package com.thomp.scalablemobs

import com.thomp.scalablemobs.config.ConfigManager
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val MODID = "scalablemobs"

@net.neoforged.fml.common.Mod(MODID)
class ScalableMobs {
    val LOGGER: Logger = LogManager.getLogger()

    init {
        LOGGER.info("[ScalableMobs] Adding some levels....")
        ConfigManager.load()
        FORGE_BUS.register(this)
    }

    @SubscribeEvent
    fun onServerStarted(event: ServerStartedEvent) {
        ConfigManager.load()
    }
}
