package repository

import model.Transaction
import org.slf4j.LoggerFactory

class TransactionRepository : Repository<model.Transaction>{

    private val logger = LoggerFactory.getLogger(TransactionRepository::class.java)

    override fun create(entity: Transaction): Transaction {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Transaction> {
        TODO("Not yet implemented")
    }

    override fun getOne(id: Long): Transaction {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Int {
        TODO("Not yet implemented")
    }
}