package model

import com.fasterxml.jackson.annotation.JsonProperty

data class Error(
    @JsonProperty("error")
    val error: String
)
