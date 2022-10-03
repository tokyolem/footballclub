package com.ftclub.footballclub.basic.room

import androidx.room.RoomDatabase
import androidx.room.Database
import com.ftclub.footballclub.basic.room.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.daos.AccountsDao

@Database(entities =
    [Accounts::class],
    version = 1,
    exportSchema = false)
abstract class Database: RoomDatabase() {

    abstract fun accountsDao(): AccountsDao

    companion object {
        const val ACCOUNTS_TABLE_NAME = "ACCOUNTS_TABLE"
    }
}