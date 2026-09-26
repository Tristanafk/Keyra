package com.example.keyra.data

import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountDao: AccountDao) {
    val allAccounts: Flow<List<AccountEntity>> = accountDao.getAllAccounts()

    suspend fun insertAccount(account: AccountEntity) {
        accountDao.insertAccount(account)
    }

    suspend fun updateAccount(account: AccountEntity) {
        accountDao.updateAccount(account)
    }

    suspend fun deleteAccount(account: AccountEntity) {
        accountDao.deleteAccount(account)
    }

    suspend fun getAccountById(id: Int): AccountEntity? {
        return accountDao.getAccountById(id)
    }
}
