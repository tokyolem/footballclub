package com.ftclub.footballclub.basic.room.accounts.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.repository.AccountsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application): AndroidViewModel(application) {

    private val userScope = CoroutineScope(Dispatchers.IO)

    private val accountsRepository: AccountsRepository
    private val accounts: LiveData<List<Accounts>>

    init {
        val accountsDao = AccountsDatabase.getDatabase(application).accountsDao()
        accountsRepository = AccountsRepository(accountsDao)
        accounts = accountsRepository.getAccounts()
    }

    suspend fun getAccountsList(): List<Accounts> =
        userScope.async {
        return@async accountsRepository.getAccountsToList()
    }.await()

    suspend fun getAccountEmail(accountEmail: String): List<Accounts> =
        userScope.async {
        return@async accountsRepository.getAccountEmail(accountEmail)
    }.await()

    suspend fun getAccountsIds(id: Long): List<Accounts> =
        userScope.async {
            return@async accountsRepository.getAccountsIds(id)
        }.await()

    fun insertAccount(account: Accounts) {
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.insertAccount(account)
        }
    }
}