package com.example.keyra.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val url: String,
    val notes: String,
    val isFavorite: Boolean = false,
    val iconUri: String? = null,
    val strength: Float = 0f
)
