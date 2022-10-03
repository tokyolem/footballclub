package com.ftclub.footballclub.basic.room

import android.content.Context
import com.ftclub.footballclub.basic.room.accountsObject.Accounts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    /**
     * This method adds the account passed as a parameter to the database.
     * @see Accounts
     */
    fun insertAccount(account: Accounts) {
        coroutine.launch {
            accountsDatabase.accountsDao().insertAccount(account)
        }
    }

    fun getAccountEmail(accountEmail: String): List<Accounts> {
        val email = mutableListOf<Accounts>()
        coroutine.launch {
            email.addAll(accountsDatabase.accountsDao().getAccountName(accountEmail))
        }
        return email
    }
}