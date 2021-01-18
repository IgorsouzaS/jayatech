package web.exception

class InvalidTransaction (
    val type: String,
    override val message: String
) : Exception(message)