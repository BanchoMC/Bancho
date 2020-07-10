package bancho.components

import bancho.util.Provider

class ComponentProvider: Provider<Component>() {
    override val toProvide: List<Component> = listOf()
}