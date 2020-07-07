package bancho

import bancho.services.ConfigService
import bancho.services.LocaleService
import bancho.services.ServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BanchoPlugin: JavaPlugin() {
    companion object {
        lateinit var instance: BanchoPlugin
        lateinit var configuration: ConfigService.Config
    }

    val serviceProvider = ServiceProvider()

    override fun onEnable() {
        instance = this
        val configService = serviceProvider.get<ConfigService>()

        configuration = configService.load(File(dataFolder, "config.hcl"))

        serviceProvider.get<LocaleService>().reloadLocale()

        server.consoleSender.sendMessage(
            serviceProvider.get<LocaleService>().getStringPrefixed("core.init.success"))
        if (configuration.get("firstRun", true)) {
            server.consoleSender.sendMessage(
                serviceProvider.get<LocaleService>().getStringPrefixed("core.init.firstRun"))
        }
    }
}