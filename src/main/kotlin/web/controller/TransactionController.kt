package web.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.TransactionRepository

class TransactionController(private val transactionRepository: TransactionRepository)  {

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(TransactionController::class.java)
    }

}