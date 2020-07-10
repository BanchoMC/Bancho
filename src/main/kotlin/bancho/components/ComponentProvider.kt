package bancho.components

import bancho.components.chat.ChatComponent
import bancho.util.Provider

class ComponentProvider : Provider<Component>() {
    override val toProvide = listOf(
        ChatComponent()
    )
}