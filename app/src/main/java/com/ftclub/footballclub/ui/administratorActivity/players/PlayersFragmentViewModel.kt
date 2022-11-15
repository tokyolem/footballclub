package com.ftclub.footballclub.ui.administratorActivity.players

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersFragmentViewModel(
    application: Application,
    private val dbViewModel: AccountsViewModel
) : AndroidViewModel(application) {

    var accounts = MutableLiveData<List<Accounts>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accounts = dbViewModel.accountsLiveData
        }
    }

    fun searchItem(searchableString: String): List<Accounts> {
        val accountsList = accounts.value as List<Accounts>
        val searchableItem = mutableListOf<Accounts>()

        for (account in accountsList) {
            if (account.accountEmail.lowercase().contains(searchableString.lowercase())
                || account.firstName.lowercase().contains(searchableString.lowercase())
                || account.lastName.lowercase().contains(searchableString.lowercase())
                || account.phoneNumber.lowercase().contains(searchableString.lowercase())
                || account.playerPosition.lowercase().contains(searchableString.lowercase())
            )
                searchableItem.add(account)
        }

        return if (searchableItem.isEmpty()) {
            Toast.makeText(getApplication(), "Ничего не найдено!", Toast.LENGTH_LONG).show()
            emptyList()
        } else {
            searchableItem
        }
    }

}