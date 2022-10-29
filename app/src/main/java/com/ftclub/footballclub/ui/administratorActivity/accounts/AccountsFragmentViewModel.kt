package com.ftclub.footballclub.ui.administratorActivity.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountsFragmentViewModel(application: Application) : AndroidViewModel(application) {

    /*private var accountsFragmentRepository: AccountsFragmentRepository

    var accountsLiveData: LiveData<List<Accounts>>

    init {
        val accountsDao = AccountsDatabase.getDatabase(application).accountsDao()
        accountsFragmentRepository = AccountsFragmentRepository(accountsDao)
        accountsLiveData = accountsFragmentRepository.accountsLiveData
    }

    fun deleteAccount(accountId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            accountsFragmentRepository.deleteAccount(accountId)
        }
    }*/
}