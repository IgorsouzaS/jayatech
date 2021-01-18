package repository

import config.DbConfig
import model.User
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object Users : LongIdTable("USERS") {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val name: Column<String> = varchar("name", 100)

    fun toDomain(row: ResultRow): User {
        return User(
            id = row[id].value,
            name = row[name],
            email = row[email]
        )
    }
}

class UserRepository(db: DbConfig) : Repository<User> {

    private var dataSource: JdbcDataSource = db.getDataSource()

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Users)
        }
    }

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