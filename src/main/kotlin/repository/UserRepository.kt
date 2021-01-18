package repository

import model.User
import org.slf4j.LoggerFactory

class UserRepository : Repository<User> {

    private val logger = LoggerFactory.getLogger(UserRepository::class.java)

    override fun create(entity: User): User {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getOne(id: Long): User {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Int {
        TODO("Not yet implemented")
    }

    fun update(userId : Long, entity: User): User{
        TODO()
    }

    fun getUserTransactions(userId: Long): List<model.Transaction>{
        TODO()
    }
}