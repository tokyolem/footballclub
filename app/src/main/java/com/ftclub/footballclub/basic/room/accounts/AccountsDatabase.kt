package com.ftclub.footballclub.basic.room.accounts

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.daos.AccountsDao

@Database(entities = [Accounts::class], version = 1, exportSchema = false)
abstract class AccountsDatabase: RoomDatabase() {

    abstract fun accountsDao(): AccountsDao

    companion object {
        const val ACCOUNTS_TABLE_NAME = "ACCOUNTS_TABLE"
        private const val ACCOUNTS_DATABASE_NAME = "accounts_database"

        @Volatile
        private var INSTANCE: AccountsDatabase? = null

        fun getDatabase(context: Context): AccountsDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountsDatabase::class.java,
                    ACCOUNTS_DATABASE_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}