package web.router

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.koin.core.component.KoinComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import web.controller.TransactionController

class TransactionRouter(private val controller: TransactionController) : KoinComponent{

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(TransactionRouter::class.java)
    }

    fun register(app: Javalin) {
        app.routes {

            before { ctx -> logger.info(ctx.req.method + " " + ctx.req.requestURL.toString()) }

            path("transactions") {
                get(controller::getAll)
                post(controller::create)
                path(":id") {
                    get(controller::getOne)
                    delete(controller::delete)
                }
            }

            after { ctx -> logger.info(ctx.res.status.toString()) }
        }
    }
}