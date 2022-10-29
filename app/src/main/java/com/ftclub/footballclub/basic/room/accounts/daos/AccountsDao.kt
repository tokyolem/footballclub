package com.ftclub.footballclub.basic.room.accounts.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} ORDER BY id ASC")
    fun getAccounts(): Flow<List<Accounts>>

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} WHERE account_email = :accountEmail")
    fun getAccountByEmail(accountEmail: String): Flow<Accounts>

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} WHERE id = :accountId")
    fun getAccountById(accountId: Long): Flow<Accounts>

    @Query("SELECT * FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} WHERE account_email = :accountEmail")
    suspend fun getAccountEmail(accountEmail: String): List<Accounts>

    @Insert
    suspend fun insertAccount(account: Accounts)

    @Update
    suspend fun updateAccount(account: Accounts)

    @Query("DELETE FROM ${AccountsDatabase.ACCOUNTS_TABLE_NAME} WHERE id = :accountId")
    suspend fun deleteAccount(accountId: Long)
}