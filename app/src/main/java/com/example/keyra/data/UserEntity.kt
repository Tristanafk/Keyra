package com.example.keyra.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val password: String,
    val isVerified: Boolean = false
)
