package com.ftclub.footballclub.ui.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftclub.footballclub.AdministratorActivity
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAccountsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountsFragment : Fragment() {

    private lateinit var _binding: FragmentAccountsBinding
    private val binding get() = _binding

    private lateinit var viewModel: AccountsViewModel

    private lateinit var adapter: AccountsAdapter

    private val ACCOUNTS_EXTRA_KEY = "accounts_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        adapter = AccountsAdapter(getAccounts())
        adapter.setData(getAccountsListFromExtras())

        with(binding.accountsRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AccountsFragment.adapter
        }

        viewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            adapter.setData(accounts)
            binding.accountsRecyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.logOut.setOnClickListener {
            (activity as AdministratorActivity).finish()
        }
    }

    private fun getAccountsListFromExtras(): List<Accounts> {
        val fromExtras = (activity as AdministratorActivity).intent.extras

        return if (fromExtras != null) fromExtras.getSerializable(ACCOUNTS_EXTRA_KEY) as List<Accounts>
        else throw NullPointerException("Can't found activity's extra: $fromExtras")
    }

    private fun getAccounts() = listOf(
        AccountFingerprint(
            binding.accountsRecyclerView,
            requireContext(),
            viewModel,
            requireActivity()
        )
    )
}
