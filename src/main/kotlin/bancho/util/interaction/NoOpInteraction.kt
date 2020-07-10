package bancho.util.interaction

import bancho.util.interaction.vault.IVaultInteraction
import org.bukkit.entity.Player

class NoOpInteraction: IVaultInteraction {
    override fun getPlayerPrefix(player: Player): String = ""
    override fun getPlayerSuffix(player: Player): String = ""
}