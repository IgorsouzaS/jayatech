package util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class DateTimeSerializer : JsonSerializer<LocalDateTime>() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun serialize(value: LocalDateTime, gen: JsonGenerator?, provider: SerializerProvider?) {
        val date: Date = Date.from(value.atZone(ZoneId.systemDefault()).toInstant())
        gen!!.writeString(formatter.format(date))
    }
}
