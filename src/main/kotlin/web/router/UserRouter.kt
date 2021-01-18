package web.router

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.koin.core.component.KoinComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserRouter : KoinComponent {

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(UserRouter::class.java)
    }

    fun register(app: Javalin) {
        app.routes {

            before { ctx -> logger.info(ctx.req.method + " " + ctx.req.requestURL.toString()) }

            path("users") {

            }

            after { ctx -> logger.info(ctx.res.status.toString()) }
        }
    }
}