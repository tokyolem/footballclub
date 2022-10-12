package com.ftclub.footballclub.ui.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AccountsFragmentViewModel(
    private val accountsViewModel: AccountsViewModel
) : ViewModel() {

    private val userScope = CoroutineScope(Dispatchers.IO)

    var accountsLiveData: LiveData<List<Accounts>> = accountsViewModel.accounts

    suspend fun getAccountsList(): List<Accounts> =
        userScope.async {
            return@async accountsViewModel.getAccountsList()
        }.await()
}