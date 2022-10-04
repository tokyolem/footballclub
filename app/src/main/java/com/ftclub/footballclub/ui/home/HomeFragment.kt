package com.ftclub.footballclub.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var accountsViewModel: AccountsViewModel

    private var accountsList: List<Accounts>? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        accountsViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineScope.launch {
            accountsList = accountsViewModel.getAccountsList()
            createDefaultAdminAccountIfNotExist()
        }

    }

    override fun onStart() {
        super.onStart()
        buttonActions()
    }

    private fun buttonActions() {
        val toAuthorizationFragment =
            requireActivity().findViewById<FrameLayout>(R.id.to_authorization_page)
        val toRegistrationFragment =
            requireActivity().findViewById<FrameLayout>(R.id.to_registration_page)

        toAuthorizationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_authorizationFragment)
        }

        toRegistrationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registrationFragment)
        }
    }

    private fun createDefaultAdminAccountIfNotExist() {
        if (accountsList!!.isEmpty()) {
            accountsViewModel.insertAccount(Accounts("admin", 1, "admin"))
        } else {
            return
        }
    }
}