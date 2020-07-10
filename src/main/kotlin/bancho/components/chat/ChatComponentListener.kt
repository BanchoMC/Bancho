package bancho.components.chat

import bancho.BanchoPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatComponentListener : Listener {
    @EventHandler
    fun chatFormatting(e: AsyncPlayerChatEvent) {
        if (BanchoPlugin.configuration.get("component.chat.formatting.enabled", true)) {
            e.format = BanchoPlugin.configuration.get(
                "component.chat.formatting.format",
                "<\$PREFIX\$PLAYER\$SUFFIX> \$MESSAGE"
            )
                .replace("\$PREFIX", BanchoPlugin.vaultInteraction.getPlayerPrefix(e.player), false)
                .replace("\$PREFIX", BanchoPlugin.vaultInteraction.getPlayerSuffix(e.player), false)
        }
    }
}