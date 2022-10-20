package com.ftclub.footballclub.ui.players

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayersBinding

class PlayersFragment : Fragment() {

    private lateinit var viewModel: PlayersViewModel
    private lateinit var dbViewModel: AccountsViewModel

    private lateinit var _binding: FragmentPlayersBinding
    private val binding get() = _binding

    private lateinit var adapter: PlayersAdapter

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
        }

        with(binding.playersRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PlayersFragment.adapter
        }

        return binding.root
    }

    private fun getAccounts() = listOf(
        PlayersFingerprint(requireContext(), this)
    )
}