package com.ftclub.footballclub.ui.administratorActivity.players

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentPlayersBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.administratorActivity.AdministratorActivity
import com.ftclub.footballclub.ui.administratorActivity.players.recycler.PlayersAdapter
import com.ftclub.footballclub.ui.administratorActivity.players.recycler.PlayersFingerprint

class PlayersFragment : Fragment() {

    private lateinit var _binding: FragmentPlayersBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel
    private lateinit var viewModel: PlayersFragmentViewModel

    private lateinit var adapter: PlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        viewModel = ViewModelProvider(
            this,
            PlayersFragmentViewModelFactory(
                requireActivity().application,
                dbViewModel
            )
        )[PlayersFragmentViewModel::class.java]

        buildRecyclerView()
        onFabClick()
        addSearchViewListener()

        return binding.root
    }

    private fun buildRecyclerView() {
        adapter = PlayersAdapter(getAccounts())

        with(binding.playersRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PlayersFragment.adapter
        }

        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            accounts?.let {
                adapter.submitList(accounts)
            }
        }

        helperTouchRecyclerView()
    }

    private fun helperTouchRecyclerView() {
        ItemTouchHelper(object : SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                findNavController().navigate(
                    PlayersFragmentDirections.actionNavigationPlayersToPlayerPageFragment(
                        adapter.getItemId(viewHolder.adapterPosition)
                    )
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }).attachToRecyclerView(binding.playersRecycler)

        binding.playersRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.fabAddAccount.isShown) ViewsAnimation.viewScaleDown(
                    binding.fabAddAccount, requireContext()
                ) else if (dy < 0 && !binding.fabAddAccount.isShown) ViewsAnimation.viewScaleUp(
                    binding.fabAddAccount, requireContext()
                )
            }
        })
    }

    private fun getAccounts() = listOf(
        PlayersFingerprint { item ->
            findNavController().navigate(
                PlayersFragmentDirections.actionNavigationPlayersToPlayerPageFragment(
                    item.id!!
                )
            )
        }
    )

    private fun addSearchViewListener() {
        binding.searchPlayers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val searchableItems = viewModel.searchItem(newText!!)
                if (newText.isEmpty()) {
                    adapter.submitList(viewModel.accounts.value as List<Accounts>)
                } else {
                    adapter.submitList(searchableItems)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun onFabClick() {
        binding.fabAddAccount.setOnClickListener {
            findNavController().navigate(PlayersFragmentDirections.actionNavigationPlayersToNavigationAdd())
        }
    }


}