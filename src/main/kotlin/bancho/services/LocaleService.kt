package bancho.services

import bancho.BanchoPlugin
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.bukkit.ChatColor
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.stream.Collectors

class LocaleService : Service() {
    private val parser = JsonParser()
    private lateinit var locale: JsonElement
    private var localeFromFile: JsonElement? = null

    fun getStringPrefixed(path: String): String = BanchoPlugin.configuration.get(
        "core.prefix", "&d&lBancho // &r"
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

        locale = parser.parse(resourceContent)

        val localeFile = File(BanchoPlugin.instance.dataFolder, "locale.json")
        if (localeFile.exists())
            localeFromFile = parser.parse(localeFile.readText(Charset.defaultCharset()))
    }

    private fun getStringByPath(path: String, forceBuiltInLocale: Boolean = false): String {
        val splitPath = path.split('.')
        var currentJsonElement = localeFromFile ?: locale
        if (forceBuiltInLocale) currentJsonElement = locale

        splitPath.forEach {
            when {
                currentJsonElement.isJsonObject -> currentJsonElement = (currentJsonElement as JsonObject).get(it)
                currentJsonElement.isJsonPrimitive -> return currentJsonElement.asString

                else -> {
                    if (localeFromFile != null && !forceBuiltInLocale)
                        return getStringByPath(path, true)
                }
            }
        }

        BanchoPlugin.instance.logger.warning(
            "LocaleService: Tried to resolve $path and hit a dead end, returning path."
        )
        return path
    }
}