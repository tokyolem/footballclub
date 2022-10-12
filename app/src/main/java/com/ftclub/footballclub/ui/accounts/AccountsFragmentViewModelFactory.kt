package com.ftclub.footballclub.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel

class AccountsFragmentViewModelFactory(private val accountsViewModel: AccountsViewModel) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountsFragmentViewModel(accountsViewModel) as T
    }
}