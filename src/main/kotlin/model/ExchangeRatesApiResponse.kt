package model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ExchangeRatesApiResponse(
    @JsonProperty("rates")
    var rates: HashMap<String, Float>,

    @JsonProperty("base")
    var base: String,

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "America/Sao_Paulo")
    var date: Date
)