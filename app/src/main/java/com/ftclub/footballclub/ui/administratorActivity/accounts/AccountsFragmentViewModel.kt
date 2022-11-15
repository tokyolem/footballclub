package com.ftclub.footballclub.ui.administratorActivity.accounts

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.ui.ViewsAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountsFragmentViewModel(
    application: Application,
    private val dbViewModel: AccountsViewModel
) : AndroidViewModel(application) {

    var accounts = MutableLiveData<List<Accounts>>()

    lateinit var email: String
    lateinit var password: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accounts = dbViewModel.accountsLiveData
        }
    }

    fun removeAccountFromDataBase(accountId: Long) {
        dbViewModel.deleteAccount(accountId)
        Toast.makeText(
            getApplication(),
            "Аккаунт был удалён",
            Toast.LENGTH_LONG
        ).show()
    }

    fun bottomSheetEditableInitialize(item: Accounts) {
        this.email = item.accountEmail
        this.password = item.hashPassword
    }

    fun viewVisibilityWithAnimation(visibility: Int, view: View, animType: Int) {
        when (animType) {
            ViewsAnimation.SCALE_ANIMATION ->
                when (visibility) {
                    View.VISIBLE -> ViewsAnimation.viewScaleUp(view, getApplication())
                    View.GONE -> ViewsAnimation.viewScaleDown(view, getApplication())
                }
            ViewsAnimation.ALPHA_ANIMATION ->
                when (visibility) {
                    View.VISIBLE -> ViewsAnimation.viewShowAnimation(view, getApplication())
                    View.GONE -> ViewsAnimation.viewHideAnimation(view, getApplication())
                }
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