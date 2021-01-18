package web.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.UserRepository

class UserController(private val userRepository: UserRepository){

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(UserController::class.java)
    }




}