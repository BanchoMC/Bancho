package bancho.services

import bancho.BanchoPlugin

class ServiceProvider() {
    val services = listOf(
        ConfigService(),
        LocaleService()
    )

    inline fun <reified T: Service> get(): T = services.find { it is T } as T
}