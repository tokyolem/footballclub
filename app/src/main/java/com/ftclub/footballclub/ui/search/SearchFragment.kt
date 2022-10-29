package com.ftclub.footballclub.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftclub.footballclub.AdministratorActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: create a repository to separate logic from UI

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var _binding: FragmentSearchBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel
    private lateinit var accountsList: List<Accounts>

    private lateinit var adapter: SearchAdapter

    private lateinit var SEARCH_TYPE: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]
        dbViewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            accountsList = accounts
        }

        val args: SearchFragmentArgs by navArgs()
        SEARCH_TYPE = args.searchType

        setVisibilityBottomNavView(View.GONE)
        navigation()
        searchProcess()
        buildRecyclerView()

        return binding.root
    }

    private fun buildRecyclerView() {
        adapter = SearchAdapter(getAccounts())

        with(binding.searchRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SearchFragment.adapter
        }
    }

    private fun searchProcess() {
        binding.searchView.isIconified = false

        Handler(Looper.getMainLooper()).postDelayed({
            requireActivity().currentFocus?.let {
                val imm =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(it, 0)
            }
        }, 100)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchItem(newText!!)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun searchItem(searchableString: String) {

        val searchableItem = mutableListOf<Accounts>()

        for (account in accountsList) {
            if (account.accountEmail.contains(searchableString)
                || account.firstName.contains(searchableString)
                || account.lastName.contains(searchableString)
                || account.phoneNumber.contains(searchableString)
            )
                searchableItem.add(account)
        }

        if (searchableItem.isEmpty()) {
            Toast.makeText(requireContext(), "Ничего не найдено!", Toast.LENGTH_LONG).show()
            adapter.setData(emptyList())
        } else if (searchableString.isEmpty()) {
            adapter.setData(emptyList())
        } else {
            adapter.setData(searchableItem)
        }
    }

    private fun getAccounts() = listOf(
        SearchFingerprint(
            SEARCH_TYPE,
            binding.searchRecyclerView,
            requireContext(),
            requireActivity(),
            this,
            dbViewModel,
            true
        )
    )

    private fun setVisibilityBottomNavView(visibility: Int) {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.visibility = visibility
    }

    private fun navigation() {
        binding.back.setOnClickListener {
            (activity as AdministratorActivity).onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        setVisibilityBottomNavView(View.VISIBLE)
    }

}