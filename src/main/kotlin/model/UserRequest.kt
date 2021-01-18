package model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRequest (
    @JsonProperty("name")
    val name: String,

    @JsonProperty("email")
    val email: String
)