package bancho

import bancho.components.ComponentProvider
import bancho.services.ConfigService
import bancho.services.LocaleService
import bancho.services.ServiceProvider
import bancho.util.interaction.NoOpInteraction
import bancho.util.interaction.vault.IVaultInteraction
import bancho.util.interaction.vault.VaultInteraction
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BanchoPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: BanchoPlugin
        lateinit var configuration: ConfigService.Config
        lateinit var locale: LocaleService
        var vaultInteraction: IVaultInteraction = NoOpInteraction()
    }

    private val serviceProvider = ServiceProvider()
    private val componentProvider = ComponentProvider()

    override fun onEnable() {
        instance = this
        val configService = serviceProvider.get<ConfigService>()
        val issueList = ArrayList<String>()

        configuration = configService.load(File(dataFolder, "config.hcl"))
        locale = serviceProvider.get()
        locale.reloadLocale()

        if (server.pluginManager.isPluginEnabled("Vault")) {
            server.consoleSender.sendMessage(
                String.format(locale.getStringPrefixed("core.init.pluginFound"), "Vault")
            )
            vaultInteraction = VaultInteraction()
        }

        registerRootCommand()

        componentProvider.toProvide.forEach {
            it.enableComponent()
        }

        // reload guard
        if (System.getProperty("bancho:reloadGuard", "n") == "y") {
            issueList.add(locale.getString("core.init.issues.reload"))
        } else {
            System.setProperty("bancho:reloadGuard", "y")
        }

        if (issueList.size > 0) {
            server.consoleSender.sendMessage(
                locale.getStringPrefixed("core.init.issue")
            )

            issueList.forEach {
                server.consoleSender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&c- $it")
                )
            }
        } else {
            server.consoleSender.sendMessage(
                locale.getStringPrefixed("core.init.success")
            )
        }

        if (configuration.get("firstRun", true)) {
            server.consoleSender.sendMessage(
                locale.getStringPrefixed("core.init.firstRun")
            )
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