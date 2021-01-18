package web.controller

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import model.User
import model.UserRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.UserRepository

class UserController(private val userRepository: UserRepository){
    private companion object {
        val logger: Logger = LoggerFactory.getLogger(UserController::class.java)
    }

    private val notFound : String = "User not found"

    @OpenApi(
        summary = "Create user",
        operationId = "createUser",
        tags = ["User"],
        requestBody = OpenApiRequestBody([OpenApiContent(UserRequest::class)]),
        responses = [
            OpenApiResponse("200", [OpenApiContent(User::class)]),
            OpenApiResponse("400", [OpenApiContent(BadRequestResponse::class)]),
        ],
        method = HttpMethod.POST
    )
    fun create(ctx: Context){
        try {
            val userReq : UserRequest = ctx.bodyAsClass(UserRequest::class.java)
            val user = User(0, userReq.name, userReq.email)

            ctx.json(userRepository.create(user))
            logger.info("User created success")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = ex.message.toString()
            )
        }
    }

    @OpenApi(
        summary = "Update a user",
        operationId = "updateUser",
        tags = ["User"],
        requestBody = OpenApiRequestBody([OpenApiContent(UserRequest::class)]),
        responses = [
            OpenApiResponse("200", [OpenApiContent(User::class)]),
            OpenApiResponse("400", [OpenApiContent(BadRequestResponse::class)])
        ],
        method = HttpMethod.PUT
    )
    fun update(ctx: Context){
        try {
            val id = ctx.pathParam("id").toLong()
            val userReq = ctx.bodyAsClass(UserRequest::class.java)
            val user = User(id, userReq.name, userReq.email)
            ctx.json(userRepository.update(id, user))
            logger.info("User updated successfully")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = ex.message.toString()
            )
        }
    }

    @OpenApi(
        summary = "Get a user by id",
        operationId = "getAUser",
        tags = ["User"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(User::class)]),
            OpenApiResponse("400", [OpenApiContent(BadRequestResponse::class)])
        ],
        method = HttpMethod.GET
    )
    fun getOne(ctx: Context) {
        try {
            ctx.json(userRepository.getOne(ctx.pathParam("id").toLong()))
            logger.info("User data returned")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = notFound
            )
        }
    }

    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Array<User>::class)]),
            OpenApiResponse("400", [OpenApiContent(BadRequestResponse::class)])
        ],
        method = HttpMethod.GET
    )
    fun getAll(ctx : Context) {
        try {
            ctx.json(userRepository.getAll())
            logger.info("User list returned")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = ex.message.toString()
            )
        }
    }

    @OpenApi(
        summary = "Get all user transactions",
        operationId = "getAllUserTransactions",
        tags = ["User"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Array<model.Transaction>::class)]),
            OpenApiResponse("400", [OpenApiContent(BadRequestResponse::class)])
        ],
        method = HttpMethod.GET
    )
    fun getUserTransactions(ctx : Context){
        try {
            ctx.json(userRepository.getUserTransactions(ctx.pathParam("id").toLong()))
            logger.info("User transactions list returned")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = notFound
            )
        }
    }

    @OpenApi(
        summary = "Delete a user",
        operationId = "deleteUser",
        tags = ["User"],
        responses = [
            OpenApiResponse("200"),
            OpenApiResponse("400", [OpenApiContent(BadRequestResponse::class)])
        ],
        method = HttpMethod.DELETE
    )
    fun delete(ctx: Context) {
        try {
            userRepository.delete(ctx.pathParam("id").toLong())
            ctx.status(200)
            logger.info("User successfully deleted")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = notFound
            )
        }
    }

}