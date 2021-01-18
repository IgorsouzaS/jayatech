package config

import org.koin.dsl.module
import repository.TransactionRepository
import repository.UserRepository
import web.controller.TransactionController
import web.controller.UserController
import web.router.TransactionRouter
import web.router.UserRouter

object ModulesConfig {

    private val configModule = module {
        single { AppConfig() }
    }

    private val userModule = module {
        single { UserRepository(DbConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "")) }
        single { UserController(get()) }
        single { UserRouter(get()) }
    }

    private val transactionModule = module {
        single { TransactionRepository(DbConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "")) }
        single { TransactionController(get()) }
        single { TransactionRouter(get()) }
    }

    internal val modules = listOf(configModule, userModule, transactionModule)
}