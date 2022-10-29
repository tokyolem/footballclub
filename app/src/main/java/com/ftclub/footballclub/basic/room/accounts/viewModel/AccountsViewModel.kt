package com.ftclub.footballclub.basic.room.accounts.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.repository.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application): AndroidViewModel(application) {

    private val accountsRepository: AccountsRepository

    val accountsLiveData = MutableLiveData<List<Accounts>>()
    val accountByEmail = MutableLiveData<Accounts>()
    val accountById = MutableLiveData<Accounts>()

    init {
        val accountsDao = AccountsDatabase.getDatabase(application).accountsDao()
        accountsRepository = AccountsRepository(accountsDao)
        viewModelScope.launch {
            accountsRepository.getAccountsFlow().collect { items ->
                accountsLiveData.postValue(items)
            }
        }
    }

    fun getAccountByEmail(accountEmail: String) {
        viewModelScope.launch {
            accountsRepository.getAccountByEmail(accountEmail).collect { account ->
                accountByEmail.postValue(account)
            }
        }
    }

    fun getAccountById(accountId: Long) {
        viewModelScope.launch {
            accountsRepository.getAccountById(accountId).collect { account ->
                accountById.postValue(account)
            }
        }
    }

    suspend fun getAccountEmail(accountEmail: String): List<Accounts> =
        viewModelScope.async {
        return@async accountsRepository.getAccountEmail(accountEmail)
    }.await()

    fun updateAccount(account: Accounts) {
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.updateAccount(account)
        }
    }

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