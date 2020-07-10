package bancho.services

import bancho.BanchoPlugin
import com.google.gson.JsonParser
import com.jayway.jsonpath.JsonPath
import org.bukkit.ChatColor
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.stream.Collectors

class LocaleService : Service() {
    private val parser = JsonParser()
    private lateinit var locale: String
    private var localeFromFile: String? = null

    fun getStringPrefixed(path: String): String = ChatColor.translateAlternateColorCodes(
        '&',
        BanchoPlugin.configuration.get(
            "core.prefix", "&d&lBancho // &r"
        )
    ) + getString(path)

    fun getString(path: String): String = ChatColor.translateAlternateColorCodes(
        '&', getStringByPath(path)
    )

    fun reloadLocale() {
        val resourceContent = try {
            BufferedReader(
                InputStreamReader(
                    BanchoPlugin.instance.getResource(
                        "locale.json"
                    )
                )
            ).lines().collect(Collectors.joining("\n"))
        } catch (e: FileNotFoundException) {
            error("Failed to get locale from jar.")
        }

        locale = resourceContent

        val localeFile = File(BanchoPlugin.instance.dataFolder, "locale.json")
        if (localeFile.exists())
            localeFromFile = localeFile.readText(Charset.defaultCharset())
    }

    private fun getStringByPath(path: String, forceBuiltInLocale: Boolean = false): String {
        // use built in else try to use file and fall back to built in otherwise
        val localeToUse = if (forceBuiltInLocale) locale else localeFromFile ?: locale

        try {
            return JsonPath.read(localeToUse, "$.$path") as String
        } catch (e: Exception) {
            if (localeFromFile != null && !forceBuiltInLocale)
                return getStringByPath(path, true)

            BanchoPlugin.instance.logger.warning(
                "LocaleService: Attempted to get `$path` and hit a dead end, returning path."
            )
            return path
        }
    }
}