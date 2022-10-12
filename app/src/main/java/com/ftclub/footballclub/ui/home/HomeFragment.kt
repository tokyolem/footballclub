package com.ftclub.footballclub.ui.home

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
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.basic.DateTime
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

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineScope.launch {
            accountsList = SignInActivity.accountsViewModel.getAccountsList()
            createDefaultAdminAccountIfNotExist()
        }

    }

    override fun onStart() {
        super.onStart()
        navigation()
    }

    private fun navigation() {
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
            SignInActivity.accountsViewModel.insertAccount(
                Accounts(
                    "admin", "admin", true, DateTime.getFormatDateTime(),
                    "admin", "admin", "admin",
                    "admin", "admin", "admin"
                )
            )
        } else {
            return
        }
    }

    companion object {
        var accountsList: List<Accounts>? = null
    }
}