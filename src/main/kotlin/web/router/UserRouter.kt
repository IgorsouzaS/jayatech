package web.router

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.after
import io.javalin.apibuilder.ApiBuilder.before
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import web.controller.UserController

@KoinApiExtension
class UserRouter(private val controller: UserController) : KoinComponent {

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(UserRouter::class.java)
    }

    fun register(app: Javalin) {
        app.routes {

            before { ctx -> logger.info(ctx.req.method + " " + ctx.req.requestURL.toString()) }

            path("users") {
                get(controller::getAll)
                post(controller::create)
                path(":id") {
                    get(controller::getOne)
                    put(controller::update)
                    delete(controller::delete)
                    path("transactions") {
                        get(controller::getUserTransactions)
                    }
                }
            }

            after { ctx -> logger.info(ctx.res.status.toString()) }
        }
    }
}
