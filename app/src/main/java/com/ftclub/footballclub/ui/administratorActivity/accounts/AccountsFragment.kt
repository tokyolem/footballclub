package com.ftclub.footballclub.ui.administratorActivity.accounts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAccountsBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.administratorActivity.AdministratorActivity
import com.ftclub.footballclub.ui.administratorActivity.accounts.recycler.AccountFingerprint
import com.ftclub.footballclub.ui.administratorActivity.accounts.recycler.AccountsAdapter
import com.ftclub.footballclub.ui.dialog.CustomDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * A simple [Fragment] subclass.
 * Use the [AccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountsFragment : Fragment() {

    private lateinit var _binding: FragmentAccountsBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel
    private lateinit var viewModel: AccountsFragmentViewModel

    private lateinit var adapter: AccountsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]
        viewModel = ViewModelProvider(
            this,
            AccountsFragmentViewModelFactory(
                requireActivity().application,
                dbViewModel
            )
        )[AccountsFragmentViewModel::class.java]

        buildRecyclerView()
        onFabClick()
        addSearchViewListener()

        BottomSheetBehavior.from(binding.bottomSheetAccounts).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return binding.root
    }

    private fun buildRecyclerView() {
        adapter = AccountsAdapter(getAccounts())

        with(binding.accountsRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AccountsFragment.adapter
        }

        binding.progressBar.visibility = View.VISIBLE
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            accounts?.let {
                adapter.submitList(accounts)
                binding.progressBar.visibility = View.GONE
            }
        }

        helperTouchRecyclerView()
    }

    private fun helperTouchRecyclerView() {
        ItemTouchHelper(object : SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                CustomDialogFragment(R.string.delete_title, R.string.delete_message, {
                    viewModel.removeAccountFromDataBase(adapter.getItemId(position))
                }, {
                    adapter.notifyItemRemoved(position + 1)
                    adapter.notifyItemRangeChanged(position, adapter.itemCount)
                })
                    .show(requireActivity().supportFragmentManager, "custom_dialog")
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }).attachToRecyclerView(binding.accountsRecyclerView)

        binding.accountsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.fabAddAccount.isShown) ViewsAnimation.viewScaleDown(
                    binding.fabAddAccount, requireContext()
                ) else if(dy < 0 && !binding.fabAddAccount.isShown) ViewsAnimation.viewScaleUp(
                    binding.fabAddAccount, requireContext()
                )
            }
        })
    }

    private fun getAccounts() = listOf(
        AccountFingerprint { item, view ->
            openPopupActionsMenu(item, view)
        }
    )

    private fun onFabClick() {
        binding.fabAddAccount.setOnClickListener {
            findNavController().navigate(AccountsFragmentDirections.actionNavigationAccountsToNavigationAdd())
        }
    }

    private fun addSearchViewListener() {
        binding.searchAccounts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    @SuppressLint("RestrictedApi")
    private fun openPopupActionsMenu(item: Accounts, view: View) {
        val actionsMenu = PopupMenu(
            context,
            view
        )

        actionsMenu.menuInflater.inflate(R.menu.account_action_menu, actionsMenu.menu)

        actionsMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_account -> {
                    openEditBottomSheet()
                    viewModel.bottomSheetEditableInitialize(item)
                    setAccountContentToBottomSheet()

                    viewModel.viewVisibilityWithAnimation(
                        View.VISIBLE, binding.translucentBlackBackground,
                        ViewsAnimation.ALPHA_ANIMATION
                    )
                    viewModel.viewVisibilityWithAnimation(
                        View.GONE, requireActivity().findViewById(R.id.nav_view),
                        ViewsAnimation.SCALE_ANIMATION
                    )
                }

                R.id.account_remove -> {
                    CustomDialogFragment(R.string.delete_title, R.string.delete_message, {
                        viewModel.removeAccountFromDataBase(item.id!!)
                    }, null)
                        .show(requireActivity().supportFragmentManager, "custom_dialog")
                }
            }

            true
        }

        setForceShowMenuIcons(actionsMenu)
        actionsMenu.show()
    }

    private fun setForceShowMenuIcons(popupMenu: PopupMenu) {
        try {
            val fields: Array<Field> = popupMenu.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper: Any = field.get(popupMenu) as Any
                    val classPopupHelper = Class.forName(
                        menuPopupHelper
                            .javaClass.name
                    )
                    val setForceIcons: Method = classPopupHelper.getMethod(
                        "setForceShowIcon", Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun openEditBottomSheet() {
        BottomSheetBehavior.from(binding.bottomSheetAccounts).apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.fabAddAccount.hide()
        bottomSheetCallback()
    }

    private fun bottomSheetCallback() {
        BottomSheetBehavior.from(binding.bottomSheetAccounts)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        viewModel.viewVisibilityWithAnimation(
                            View.GONE,
                            binding.translucentBlackBackground,
                            ViewsAnimation.ALPHA_ANIMATION
                        )
                        viewModel.viewVisibilityWithAnimation(
                            View.VISIBLE,
                            requireActivity().findViewById(R.id.nav_view),
                            ViewsAnimation.SCALE_ANIMATION
                        )

                        binding.fabAddAccount.show()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
    }

    private fun setAccountContentToBottomSheet() {
        binding.accountEditEmail.setText(viewModel.email)
        binding.accountEditPassword.setText(viewModel.password)
    }
}
