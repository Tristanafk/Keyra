package com.example.keyra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyra.data.AccountEntity
import com.example.keyra.data.AccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {
    val allAccounts: StateFlow<List<AccountEntity>> = repository.allAccounts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addAccount(name: String, email: String, password: String, url: String, notes: String, isFavorite: Boolean = false, iconUri: String? = null) {
        viewModelScope.launch {
            val account = AccountEntity(
                name = name,
                email = email,
                password = password,
                url = url,
                notes = notes,
                isFavorite = isFavorite,
                iconUri = iconUri
            )
            repository.insertAccount(account)
        }
    }

    fun updateAccount(id: Int, name: String, email: String, password: String, url: String, notes: String, isFavorite: Boolean = false, iconUri: String? = null) {
        viewModelScope.launch {
            val account = AccountEntity(
                id = id,
                name = name,
                email = email,
                password = password,
                url = url,
                notes = notes,
                isFavorite = isFavorite,
                iconUri = iconUri
            )
            repository.updateAccount(account)
        }
    }

    fun deleteAccount(account: AccountEntity) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }

    fun toggleFavorite(account: AccountEntity) {
        viewModelScope.launch {
            val updated = account.copy(isFavorite = !account.isFavorite)
            repository.updateAccount(updated)
        }
    }
}
