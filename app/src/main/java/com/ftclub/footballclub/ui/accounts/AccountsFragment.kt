package com.ftclub.footballclub.ui.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftclub.footballclub.AdministratorActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAccountsBinding
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [AccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountsFragment : Fragment() {

    private lateinit var _binding: FragmentAccountsBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    private lateinit var adapter: AccountsAdapter

    private val ACCOUNTS_EXTRA_KEY = "accounts_list"

    private val SEARCH_TYPE = "accounts"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        adapter = AccountsAdapter(getAccounts())
        adapter.setData(getAccountsListFromExtras())

        with(binding.accountsRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AccountsFragment.adapter
        }

        dbViewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            adapter.setData(accounts)
        }

        signOut()
        searchNavigation()

        return binding.root
    }

    private fun getAccountsListFromExtras(): List<Accounts> {
        val fromExtras = (activity as AdministratorActivity).intent.extras

        return if (fromExtras != null) fromExtras.getSerializable(ACCOUNTS_EXTRA_KEY)
                as List<Accounts>
        else throw NullPointerException("Can't found activity's extra: $fromExtras")
    }

    private fun searchNavigation() {
        binding.searchAccounts.setOnClickListener {
            val action =
                AccountsFragmentDirections.actionNavigationAccountsToSearchFragment(SEARCH_TYPE)
            findNavController().navigate(action)
        }
    }

    private fun signOut() {
        binding.signOut.setOnClickListener {
            val dialog = CustomDialogFragment(
                R.string.sigh_out_title,
                R.string.sign_out_message,
            ) { (activity as AdministratorActivity).finish() }
            dialog.show(requireActivity().supportFragmentManager, "custom_dialog")
        }
    }

    private fun getAccounts() = listOf(
        AccountFingerprint(
            binding.accountsRecyclerView,
            requireContext(),
            requireActivity(),
            dbViewModel
        )
    )
}
