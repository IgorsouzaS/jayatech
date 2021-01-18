import config.AppConfig
import org.h2.tools.Server
import java.util.*

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    Server.createWebServer().start()
    AppConfig().startApplication()
}