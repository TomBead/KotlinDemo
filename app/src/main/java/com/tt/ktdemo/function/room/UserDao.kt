package com.tt.ktdemo.function.room

import androidx.room.*


@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>?

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>?

    @Query("SELECT * FROM user WHERE name LIKE :name")
    fun findByName(name: String): User?

    @Query("SELECT *FROM user WHERE uid LIKE:userId")
    fun findById(userId: Int): User?

    @Update(entity = User::class)
    fun updateSingleName(vararg name: UserName?)

    @Update
    fun updateUser(vararg user: User)

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}
