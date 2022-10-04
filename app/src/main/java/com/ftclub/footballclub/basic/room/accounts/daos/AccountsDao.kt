package com.ftclub.footballclub.basic.room.accounts.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts

@Dao
interface AccountsDao {

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} ORDER BY id ASC")
    fun getAccounts(): LiveData<List<Accounts>>

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} ORDER BY id ASC")
    suspend fun getAccountsToList(): List<Accounts>

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} WHERE account_email = :accountEmail")
    suspend fun getAccountEmail(accountEmail: String): List<Accounts>

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} WHERE id = :id")
    suspend fun getAccountsIds(id: Long): List<Accounts>

    @Insert
    suspend fun insertAccount(account: Accounts)
}