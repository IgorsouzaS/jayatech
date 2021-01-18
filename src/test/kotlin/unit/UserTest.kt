package unit

import config.ModulesConfig
import model.User
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent
import org.koin.test.KoinTest
import repository.UserRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserTest : KoinTest {

    private val userRepository : UserRepository by KoinJavaComponent.inject(UserRepository::class.java)
    private var user = User(1, "test", "test")

    @Before
    fun before (){
        startKoin{
            modules(ModulesConfig.modules)
        }
    }

    @After
    fun after(){
        stopKoin()
    }

    @Test
    fun `a - should save user`() {
        val createdUser : User = userRepository.create(user)
        assertNotNull(createdUser)
    }

    @Test
    fun `b - should return users list`() {
        val userList : List<User> = userRepository.getAll()
        assertTrue(userList.isNotEmpty())
    }

    @Test
    fun `c - should return user with different name`() {
        val newName : String = user.name+"test"
        val newUser = user.copy(name = newName)
        val createdUser : User = userRepository.update(user.id, newUser)

        assertNotNull(createdUser)
        assertEquals(newName, createdUser.name)
    }

    @Test
    fun `d - should not return any user`() {
        val result : Int = userRepository.delete(user.id)
        print(result)
        assertEquals(1,result)
    }

}