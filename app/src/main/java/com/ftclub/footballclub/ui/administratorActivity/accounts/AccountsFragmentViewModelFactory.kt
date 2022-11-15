package com.ftclub.footballclub.ui.administratorActivity.accounts

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel

class AccountsFragmentViewModelFactory(
    private val application: Application,
    val dbViewModel: AccountsViewModel
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountsFragmentViewModel(application, dbViewModel) as T
    }

}