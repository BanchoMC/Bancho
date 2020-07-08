package bancho.services

import bancho.BanchoPlugin
import com.bertramlabs.plugins.hcl4j.HCLParser
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.file.Paths
import java.util.stream.Collectors

class ConfigService: Service() {
    private val parser = HCLParser()

    fun load(file: File): Config {
        val base = Paths.get(BanchoPlugin.instance.dataFolder.path)
        val resourcePath = base.relativize(Paths.get(file.path))

        val resourceContent = try {
            BufferedReader(InputStreamReader(BanchoPlugin.instance.getResource(
                resourcePath.toString().replace('\\', '/') // in case of windows
            ))).lines().collect(Collectors.joining("\n"))
        } catch (e: FileNotFoundException) {
            error("Attempted to get invalid configuration file $resourcePath (not in jar)")
        }

        if (!file.exists()) {
            if (!BanchoPlugin.instance.dataFolder.exists())
                BanchoPlugin.instance.dataFolder.mkdir()

            file.writeText(resourceContent)
        }

        return try {
            Config(resourcePath.toString(), parser.parse(file, "UTF-8"))
        } catch (e: Exception) {
            BanchoPlugin.instance.logger.warning("Failed to load config file $resourcePath:")
            BanchoPlugin.instance.logger.warning(e.toString())
            Config(resourcePath.toString(), parser.parse(resourceContent))
        }
    }

    class Config(val file: String, val configData: Map<String, Any>) {
        inline fun <reified T> get(path: String, default: T): T {
            val splitPath = path.split('.')
            var currentMap = configData

            splitPath.forEach breakout@{
                if (currentMap.containsKey(it)) {
                    val mapKey = it
                    when (val currentVal = currentMap[mapKey] ?: error("what.")) {
                        // the requested type
                        is T -> return currentVal
                        // another layer
                        is Map<*, *> -> {
                            @Suppress("UNCHECKED_CAST") // can suppress here because we know it'll be this type or a string
                            currentMap = currentVal as Map<String, Any>
                        }
                        // invalid type, just return the default value and warn
                        else -> {
                            BanchoPlugin.instance.logger.warning(
                                "$file: Attempted to get `$path` but got wrong type (expected ${T::class.simpleName}, got ${currentVal::class.simpleName}), returning default")
                            return default
                        }
                    }
                } else return@breakout
            }

            BanchoPlugin.instance.logger.warning(
                "$file: Attempted to get `$path` but hit a dead end, returning default (did you remove a config option?)")
            return default
        }
    }
}