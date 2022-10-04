package com.ftclub.footballclub.basic.room.accounts.repository

import androidx.lifecycle.LiveData
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.daos.AccountsDao

class AccountsRepository(private val accountsDao: AccountsDao) {

    fun getAccounts(): LiveData<List<Accounts>> = accountsDao.getAccounts()

    suspend fun getAccountsToList(): List<Accounts> = accountsDao.getAccountsToList()

    suspend fun getAccountEmail(accountEmail: String): List<Accounts> =
        accountsDao.getAccountEmail(accountEmail)

    suspend fun getAccountsIds(id: Long): List<Accounts> =
        accountsDao.getAccountsIds(id)

    suspend fun insertAccount(account: Accounts) {
        accountsDao.insertAccount(account)
    }

}