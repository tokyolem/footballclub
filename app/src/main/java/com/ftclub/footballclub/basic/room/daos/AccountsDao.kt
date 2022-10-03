package com.ftclub.footballclub.basic.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ftclub.footballclub.basic.room.Database
import com.ftclub.footballclub.basic.room.accountsObject.Accounts

@Dao
interface AccountsDao {

    @Query("SELECT * FROM ${Database.ACCOUNTS_TABLE_NAME} ORDER BY id ASC")
    suspend fun getAccounts(): List<Accounts>

    @Query("SELECT * FROM ${Database.ACCOUNTS_TABLE_NAME} WHERE account_email LIKE :accountEmail ORDER BY id ASC")
    suspend fun getAccountName(accountEmail: String): List<Accounts>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccount(account: Accounts)
}