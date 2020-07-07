package bancho.services

import bancho.BanchoPlugin
import org.bukkit.ChatColor
import java.io.File

class LocaleService: Service() {
    private lateinit var locales: ConfigService.Config

    fun getStringPrefixed(path: String): String = getString("core.prefix") + getString(path)
    fun getString(path: String): String = ChatColor.translateAlternateColorCodes(
        '&', locales.get(path, path))

    fun reloadLocale() {
        locales = BanchoPlugin.instance.serviceProvider.get<ConfigService>().load(
            File(BanchoPlugin.instance.dataFolder, "locale.hcl")
        )
    }
}