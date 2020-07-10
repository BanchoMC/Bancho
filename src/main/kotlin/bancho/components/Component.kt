package bancho.components

import bancho.BanchoPlugin
import org.bukkit.event.Listener

abstract class Component(
    private val configKey: String,
    private val listenerClasses: List<Listener>? = null
) {
    fun enableComponent() {
        val shouldEnable = BanchoPlugin.configuration.get("component.$configKey.enabled", false)

        if (shouldEnable) {
            registerListeners()
            registerCommands()
        }
    }

    open fun registerCommands() {} // no-op, components need to add this themselves

    private fun registerListeners() {
        listenerClasses?.forEach {
            BanchoPlugin.instance.server.pluginManager.registerEvents(it, BanchoPlugin.instance)
        }
    }
}