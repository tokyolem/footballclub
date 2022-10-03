package com.ftclub.footballclub.basic.room

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object BaseDatabase {

    private val coroutine = CoroutineScope(Dispatchers.Main)

    private const val accountsDatabaseName = "accounts_database"

    private var accountsInstance: Database? = null

    val accountsDatabase: Database
        get() {
            if (accountsInstance != null) return accountsInstance as Database
            else throw IllegalAccessException("Cannot access to Accounts database")
        }

    fun init(context: Context) {
        if (accountsInstance == null) accountsInstance = getSpecificDatabase(context, accountsDatabaseName)
    }

    private fun getSpecificDatabase(context: Context, databaseName: String): Database {
        synchronized(this) {
            return androidx.room.Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                databaseName
            ).fallbackToDestructiveMigration().build()
        }
    }
}