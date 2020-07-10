package bancho.util

open class Provider<C> {
    open val toProvide: List<C> = listOf()

    inline fun <reified T : C> get(): T = toProvide.find { it is T } as T
}