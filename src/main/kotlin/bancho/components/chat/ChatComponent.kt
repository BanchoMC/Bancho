package bancho.components.chat

import bancho.components.Component

class ChatComponent : Component(
    "chat",
    listOf(
        ChatComponentListener()
    )
)