package bancho.util.interaction.vault

import bancho.BanchoPlugin
import net.milkbowl.vault.chat.Chat
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider

class VaultInteraction: IVaultInteraction {
    private val chat: RegisteredServiceProvider<Chat>? =
        BanchoPlugin.instance.server.servicesManager.getRegistration(Chat::class.java)

    override fun getPlayerPrefix(player: Player): String = chat?.provider?.getPlayerPrefix(player) ?: ""
    override fun getPlayerSuffix(player: Player): String = chat?.provider?.getPlayerSuffix(player) ?: ""
}