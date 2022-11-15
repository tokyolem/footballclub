package com.ftclub.footballclub.ui.signInActivity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        dbViewModel.getAccountByEmail("admin")
        dbViewModel.accountByEmail.observe(viewLifecycleOwner) {
            if (it == null) createDefaultAdminAccountIfNotExist()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        navigation()
    }

    private fun navigation() {
        binding.toAuthorizationPage.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_authorizationFragment)
        }

        binding.toRegistrationPage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registrationFragment)
        }
    }


    private fun createDefaultAdminAccountIfNotExist() {
        dbViewModel.insertAccount(
            Accounts(
                "admin", "admin", true, DateTime.getFormatDateTime(),
                resources.getString(R.string.admin_inf), "Тренер", "23/06/2002",
                "Дмитрий", "Фоменок (тренер)", "+375 (29) 15-15-860"
            )
        )
    }

}
