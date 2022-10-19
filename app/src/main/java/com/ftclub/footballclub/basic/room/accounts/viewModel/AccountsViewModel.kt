package com.ftclub.footballclub.basic.room.accounts.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.repository.AccountsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application): AndroidViewModel(application) {

    private val accountsRepository: AccountsRepository

    val accountsLiveData = MutableLiveData<List<Accounts>>()

    init {
        val accountsDao = AccountsDatabase.getDatabase(application).accountsDao()
        accountsRepository = AccountsRepository(accountsDao)
        viewModelScope.launch {
            accountsRepository.getAccountsFlow().collect { items ->
                accountsLiveData.postValue(items)
            }
        }
    }

    suspend fun getAccountEmail(accountEmail: String): List<Accounts> =
        viewModelScope.async {
        return@async accountsRepository.getAccountEmail(accountEmail)
    }.await()

    fun insertAccount(account: Accounts) {
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.insertAccount(account)
        }
    }

    fun deleteAccount(accountId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.deleteAccount(accountId)
        }
    }
}