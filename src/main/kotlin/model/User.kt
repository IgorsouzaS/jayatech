package model

import com.fasterxml.jackson.annotation.JsonProperty

data class User (
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("email")
    val email: String
)