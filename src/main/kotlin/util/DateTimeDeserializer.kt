package util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

class DateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): LocalDateTime {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = jsonParser.text
        return try {
            sdf.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }
}