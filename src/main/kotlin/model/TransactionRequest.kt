package model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TransactionRequest(
    @JsonProperty("userId")
    var userId: Long,

    @JsonProperty("originCurrency")
    var originCurrency: String,

    @JsonProperty("originAmount")
    var originAmount: Double,

    @JsonProperty("destinationCurrency")
    var destinationCurrency: String
)
