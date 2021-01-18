package repository

interface Repository<T> {
    fun create(entity: T): T
    fun getAll(): List<T>
    fun getOne(id: Long): T
    fun delete(id: Long): Int
}