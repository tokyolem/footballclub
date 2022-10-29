package com.ftclub.footballclub.ui.players

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftclub.footballclub.AdministratorActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayersBinding
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment
import com.ftclub.footballclub.ui.search.SearchFragment

class PlayersFragment : Fragment() {

    private lateinit var viewModel: PlayersViewModel
    private lateinit var dbViewModel: AccountsViewModel

    private lateinit var _binding: FragmentPlayersBinding
    private val binding get() = _binding

    private lateinit var accountsList: List<Accounts>
    private lateinit var adapter: PlayersAdapter

    private val SEARCH_TYPE = "players"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this)[PlayersViewModel::class.java]
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        adapter = PlayersAdapter(getAccounts())

        dbViewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            adapter.setData(accounts)
            accountsList = accounts
        }

        with(binding.playersRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PlayersFragment.adapter
        }

        signOut()
        searchNavigation()

        return binding.root
    }

    private fun searchNavigation() {
        binding.searchPlayers.setOnClickListener {
            val action =
                PlayersFragmentDirections.actionNavigationPlayersToSearchFragment(SEARCH_TYPE)
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
        PlayersFingerprint(requireContext(), this, false)
    )
}