package com.ftclub.footballclub.basic.room.accounts.repository

import androidx.annotation.WorkerThread
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.daos.AccountsDao
import javax.inject.Inject

class AccountsRepository @Inject constructor(
    private val accountsDao: AccountsDao
) {

    suspend fun deleteAccount(accountId: Long) {
        accountsDao.deleteAccount(accountId)
    }

    fun getAccountsFlow() = accountsDao.getAccounts()

    fun getAccountByEmail(accountEmail: String) = accountsDao.getAccountByEmail(accountEmail)

    fun getAccountById(accountId: Long) = accountsDao.getAccountById(accountId)

    @WorkerThread
    suspend fun getAccountEmail(accountEmail: String): List<Accounts> =
        accountsDao.getAccountEmail(accountEmail)

    @WorkerThread
    suspend fun insertAccount(account: Accounts) {
        accountsDao.insertAccount(account)
    }

    @WorkerThread
    suspend fun updateAccount(account: Accounts) {
        accountsDao.updateAccount(account)
    }

}