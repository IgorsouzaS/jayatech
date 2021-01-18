package model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Transaction(
    @JsonProperty("userId")
    var userId: Long,

    @JsonProperty("id")
    val id: Long?,

    @JsonProperty("originCurrency")
    var originCurrency: String,

    @JsonProperty("originAmount")
    var originAmount: Double,

    @JsonProperty("destinationCurrency")
    var destinationCurrency: String,

    @JsonProperty("destinationAmount")
    var destinationAmount: Double,

    @JsonProperty("rate")
    var rate: Float,

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "America/Sao_Paulo", shape = JsonFormat.Shape.STRING)
    var createdAt: LocalDateTime
)
