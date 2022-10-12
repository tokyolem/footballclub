package com.ftclub.footballclub.ui.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ftclub.footballclub.R
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAccountsBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [AccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountsFragment : Fragment() {

    private val userScope = CoroutineScope(Dispatchers.Main)

    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = _binding!!

    private lateinit var model: AccountsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountsBinding.inflate(inflater, container, false)

        model = ViewModelProvider(
            this,
            AccountsFragmentViewModelFactory(SignInActivity.accountsViewModel)
        )[AccountsFragmentViewModel::class.java]

        userScope.launch {
            val recyclerView = binding.accountsRecyclerView
            val accountPageList = model.getAccountsList()

            recyclerView.apply {
                adapter = AccountsAdapter(accountPageList)
                layoutManager = LinearLayoutManager(context)
            }
        }

        val date = Date()

        return binding.root
    }
}
