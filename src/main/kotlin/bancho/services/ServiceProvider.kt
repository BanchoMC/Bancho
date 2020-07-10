package bancho.services

import bancho.util.Provider

class ServiceProvider: Provider<Service>() {
    override val toProvide = listOf(
        ConfigService(),
        LocaleService()
    )
}