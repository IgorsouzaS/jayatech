package config

import org.koin.dsl.module
import web.router.TransactionRouter
import web.router.UserRouter

object ModulesConfig {

    private val configModule = module {
        single { AppConfig() }
    }

    private val userModule = module {
        single { UserRouter() }
    }

    private val transactionModule = module {
        single { TransactionRouter() }
    }

    internal val modules = listOf(configModule, userModule, transactionModule)
}