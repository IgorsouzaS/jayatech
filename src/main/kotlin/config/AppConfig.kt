package config

import io.javalin.Javalin
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import web.router.TransactionRouter
import web.router.UserRouter
import java.io.FileInputStream
import java.util.*

class AppConfig : KoinComponent{

    private val userRouter: UserRouter by inject()
    private val transactionRouter: TransactionRouter by inject()

    fun startApplication(): Javalin {

        startKoin {
            modules(ModulesConfig.modules)
        }

        val app = Javalin.create {
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.result("Not found") }
        }.start(getPort())

        transactionRouter.register(app)
        userRouter.register(app)
        return app
    }

    private fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                version("1.0")
                description("Transactions API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
        }
    )

    private fun getPort() : Int{
        val prop = Properties()
        val fis = FileInputStream("src/main/resources/application.properties")
        prop.load(fis)

        return prop.getProperty("server.port").toInt()
    }
}