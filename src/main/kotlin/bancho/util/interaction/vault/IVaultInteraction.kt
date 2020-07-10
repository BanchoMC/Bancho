package bancho.util.interaction.vault

import org.bukkit.entity.Player

interface IVaultInteraction {
    fun getPlayerPrefix(player: Player): String
    fun getPlayerSuffix(player: Player): String
}