package repository

import config.DbConfig
import io.javalin.http.BadRequestResponse
import model.User
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.*
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
        val userExists: Boolean = transaction(Database.connect(dataSource)) {
            Users.select {
                Users.email eq entity.email
            }.toList().isNotEmpty()
        }

        if(userExists){
            logger.error("This email is already being used")
            throw BadRequestResponse(message = "This email is already being used")
        }

        val userId: Long = transaction(Database.connect(dataSource)) {
            Users.insertAndGetId { row ->
                row[email] = entity.email
                row[name] = entity.name
            }.value
        }
        logger.info("User created success")
        return getOne(userId)
    }

    override fun getAll(): List<User> {
        logger.info("Users list returned")
        return transaction {
            Users.selectAll().map {
                Users.toDomain(it)
            }.toList()
        }
    }

    fun getUserTransactions(userId: Long): List<model.Transaction> {
        logger.info("User transactions list returned")
        return transaction {
            Transactions.select { Transactions.userId eq userId }.map {
                Transactions.toDomain(it)
            }.toList()
        }
    }

    override fun getOne(id: Long): User {
        logger.info("User data returned")
        return transaction(Database.connect(dataSource)) {
            Users.select { Users.id eq id }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }!!
    }

    fun update(userId: Long, entity: User): User {
        transaction(Database.connect(dataSource)) {
            Users.update({ Users.id eq userId }) { row ->
                row[email] = entity.email
                row[name] = entity.name
            }
        }
        logger.info("User updated successfully")
        return getOne(userId)
    }

    override fun delete(id: Long) : Int{
        logger.info("user successfully deleted")
        return transaction(Database.connect(dataSource)) {
            Users.deleteWhere { Users.id eq id }
        }
    }

}