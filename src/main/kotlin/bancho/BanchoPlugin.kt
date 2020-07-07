package bancho

import bancho.services.ConfigService
import bancho.services.LocaleService
import bancho.services.ServiceProvider
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BanchoPlugin: JavaPlugin() {
    companion object {
        lateinit var instance: BanchoPlugin
        lateinit var configuration: ConfigService.Config
        lateinit var locale: LocaleService
    }

    val serviceProvider = ServiceProvider()

    override fun onEnable() {
        instance = this
        val configService = serviceProvider.get<ConfigService>()

        configuration = configService.load(File(dataFolder, "config.hcl"))
        locale = serviceProvider.get()
        locale.reloadLocale()

        registerRootCommand()

        server.consoleSender.sendMessage(
            serviceProvider.get<LocaleService>().getStringPrefixed("core.init.success"))
        if (configuration.get("firstRun", true)) {
            server.consoleSender.sendMessage(
                serviceProvider.get<LocaleService>().getStringPrefixed("core.init.firstRun"))
        }
    }

    private fun registerRootCommand() {
        CommandAPICommand("bancho")
            .executes(CommandExecutor { sender, _ ->
                sender.sendMessage(locale.getStringPrefixed("core.version"))
            })
            .register()
    }
}