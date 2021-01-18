package config

import org.h2.jdbcx.JdbcDataSource

class DbConfig(url: String, user: String, password: String) {
    private val dataSource = JdbcDataSource()

    init {
        dataSource.setURL(url)
        dataSource.user = user
        dataSource.password = password
    }

    fun getDataSource(): JdbcDataSource {
        return dataSource
    }
}