package com.ftclub.footballclub.ui.userActivity.userPlayers

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentUserPlayersBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment
import com.ftclub.footballclub.ui.userActivity.UserActivity
import com.ftclub.footballclub.ui.userActivity.userPlayers.recycler.UserPlayersAdapter
import com.ftclub.footballclub.ui.userActivity.userPlayers.recycler.UserPlayersFingerprint
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView

/**
 * A simple [Fragment] subclass.
 * Use the [UserPlayersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserPlayersFragment : Fragment() {

    private var _binding: FragmentUserPlayersBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbViewModel: AccountsViewModel
    private lateinit var viewModel: UserPlayersFragmentViewModel

    private lateinit var adapter: UserPlayersAdapter

    private var currentAccount: Accounts? = null
    private val CURRENT_ACCOUNT_EXTRAS_KEY = "current_account"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPlayersBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]
        viewModel = ViewModelProvider(
            this,
            UserPlayersFragmentViewModelFactory(
                requireActivity().application,
                dbViewModel
            )
        )[UserPlayersFragmentViewModel::class.java]

        buildRecyclerView()
        addSearchViewListener()
        setCurrentAccount()
        actionsWithSlideMenu()

        return binding.root
    }

    private fun buildRecyclerView() {
        adapter = UserPlayersAdapter(getAccounts())

        with(binding.playersRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UserPlayersFragment.adapter
        }

        dbViewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            accounts?.let {
                adapter.submitList(accounts)
            }
        }

        helperTouchRecyclerView()
    }

    private fun setCurrentAccount() {
        val currentAccountEmail =
            (activity as UserActivity).intent.extras!!.getString(CURRENT_ACCOUNT_EXTRAS_KEY)
        dbViewModel.getAccountByEmail(currentAccountEmail!!)
        dbViewModel.accountByEmail.observe(viewLifecycleOwner) { account ->
            account?.let { this.currentAccount = account }
        }
    }

    private fun getAccounts() = listOf(
        UserPlayersFingerprint { item ->
            findNavController().navigate(
                UserPlayersFragmentDirections.actionNavUserPlayersToNavUserPlayerPage(
                    item.id!!
                )
            )
        }
    )

    private fun helperTouchRecyclerView() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                findNavController().navigate(
                    UserPlayersFragmentDirections.actionNavUserPlayersToNavUserPlayerPage(
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
    }

    private fun addSearchViewListener() {
        binding.searchPlayers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val searchableItems = viewModel.searchItem(newText!!)
                if (newText.isEmpty()) {
                    adapter.submitList(dbViewModel.accountsLiveData.value as List<Accounts>)
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

    private fun actionsWithSlideMenu() {
        val drawerLayout =
            requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout_user)

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.nav_view_user)

        binding.openMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
            expandPartNavDrawer()
            setAccountDataToMenuHeader()
            onSignOnOutClick()
            onFullUserProfileClick(navigationView.getHeaderView(0))
        }
    }

    private fun setAccountDataToMenuHeader() {
        if (currentAccount != null) {
            requireActivity().findViewById<TextView>(R.id.current_account_name).text =
                "${currentAccount!!.firstName} ${currentAccount!!.lastName}"

            requireActivity().findViewById<TextView>(R.id.current_account_email).text =
                currentAccount!!.accountEmail
        } else throw NullPointerException("Current account not found: $currentAccount")
    }

    private fun expandPartNavDrawer() {
        val expandProfile = requireActivity().findViewById<MaterialButton>(R.id.expand_profile)
        val expandablePart =
            requireActivity().findViewById<ConstraintLayout>(R.id.expandable_part_menu)
        val expandedMenu = requireActivity().findViewById<NavigationView>(R.id.nav_view_user)

        expandProfile.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                expandedMenu as ViewGroup?,
                AutoTransition()
            )

            if (expandablePart.visibility == View.VISIBLE) {
                ViewsAnimation.turnOut(requireContext(), expandProfile)
                expandablePart.visibility = View.GONE
            } else {
                ViewsAnimation.turnIn(requireContext(), expandProfile)
                expandablePart.visibility = View.VISIBLE
            }
        }

    }

    private fun onFullUserProfileClick(header: View) {
        header.findViewById<MaterialButton>(R.id.profile).setOnClickListener {
            findNavController().navigate(
                UserPlayersFragmentDirections.actionNavUserPlayersToNavUserProfile2(
                    currentAccount!!.id!!
                )
            )
        }
    }

    private fun onSignOnOutClick() {
        requireActivity().findViewById<MaterialButton>(R.id.finish_user_activity)
            .setOnClickListener {
                CustomDialogFragment(R.string.sigh_out_title, R.string.sign_out_message, {
                    requireActivity().finish()
                }, null)
                    .show(requireActivity().supportFragmentManager, "custom_dialog")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}