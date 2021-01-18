package config

import org.koin.dsl.module
import web.controller.TransactionController
import web.controller.UserController
import web.router.TransactionRouter
import web.router.UserRouter

object ModulesConfig {

    private val configModule = module {
        single { AppConfig() }
    }

    private val userModule = module {
        single { UserController(get()) }
        single { UserRouter() }
    }

    private val transactionModule = module {
        single { TransactionController(get()) }
        single { TransactionRouter() }
    }

    internal val modules = listOf(configModule, userModule, transactionModule)
}