package com.ftclub.footballclub.ui.administratorActivity.add

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAddBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.administratorActivity.AdministratorActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class AddFragment : Fragment() {

    private lateinit var viewModel: AddViewModel
    private lateinit var dbViewModel: AccountsViewModel

    private lateinit var _binding: FragmentAddBinding
    private val binding get() = _binding

    private lateinit var accountsList: List<Accounts>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this)[AddViewModel::class.java]
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        setBottomSheetState(0, BottomSheetBehavior.STATE_EXPANDED, true)

        binding.toNextActionAddReg.visibility = View.INVISIBLE

        dbViewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            accountsList = accounts
        }

        nextAddingAction()
        setBottomNavViewVisibility(View.GONE)
        onBackClick()

        return binding.root
    }

    private fun setBottomSheetState(mPeekHeight: Int, mState: Int, withPostDelayed: Boolean) {
        when (withPostDelayed) {
            true -> BottomSheetBehavior.from(binding.bottomSheetAdd).apply {
                peekHeight = mPeekHeight
                Handler(Looper.getMainLooper()).postDelayed({
                    this.state = mState
                }, 100)
            }

            false -> BottomSheetBehavior.from(binding.bottomSheetAdd).apply {
                peekHeight = mPeekHeight
                this.state = mState
            }
        }
    }

    private fun nextAddingAction() {
        methodOfAdding()

        binding.toNextActionAddReg.setOnClickListener {
            addingProcess(true)
        }

        binding.addNewAccountHalf.setOnClickListener {
            addingProcess(false)
        }
    }

    private fun isAccountExist(accountEmail: String): Boolean {
        for (account in accountsList) if (accountEmail == account.accountEmail) return true

        return false
    }

    private fun methodOfAdding() {
        binding.addFullAccount.setOnClickListener {
            setBottomSheetState(0, BottomSheetBehavior.STATE_COLLAPSED, false)

            if (binding.toNextActionAddReg.visibility != View.VISIBLE)
                ViewsAnimation.viewScaleUp(binding.toNextActionAddReg, requireContext())

            if (binding.addNewAccountHalf.visibility == View.VISIBLE) {
                propertyButtonHalfAddAnimation(false)
            }
        }

        binding.addHalfAccount.setOnClickListener {
            setBottomSheetState(0, BottomSheetBehavior.STATE_COLLAPSED, false)

            if (binding.toNextActionAddReg.visibility == View.VISIBLE)
                ViewsAnimation.viewScaleDown(binding.toNextActionAddReg, requireContext())

            if (binding.addNewAccountHalf.visibility != View.VISIBLE) {
                propertyButtonHalfAddAnimation(true)
            }
        }
    }

    private fun addingProcess(isFullProcess: Boolean) {
        if (isLinesEmpty()) {
            showMessage("Заполните все необходимые поля!", Toast.LENGTH_LONG)
            return
        } else {
            if (binding.addNewPassword.text.toString() != binding.addRepeatPassword.text.toString()) {
                showMessage("Пароли не совпадают!", Toast.LENGTH_LONG)
                return
            } else if (isAccountExist(getAccountEmail())) {
                showMessage("Аккаунт с таким email адресом уже существует!", Toast.LENGTH_LONG)
                return
            } else {
                when (isFullProcess) {
                    true -> {
                        val toAddInformationFragment =
                            arrayOf(getAccountEmail(), getAccountPassword())
                        findNavController().navigate(
                            AddFragmentDirections.actionNavigationAddToAddInformationFragment(
                                toAddInformationFragment
                            )
                        )
                    }
                    false -> {
                        dbViewModel.insertAccount(
                            Accounts(
                                getAccountEmail(), getAccountPassword(), false,
                                DateTime.getFormatDateTime(), "", "", "",
                                "", "", ""
                            )
                        )
                        showMessage("Аккаунт успешно добавлен!", Toast.LENGTH_LONG)
                        findNavController().navigate(R.id.action_navigation_add_to_navigation_accounts)
                    }
                }
            }
        }
    }

    private fun isLinesEmpty() =
        binding.addNewEmail.text!!.isEmpty()
                || binding.addNewPassword.text!!.isEmpty()
                || binding.addRepeatPassword.text!!.isEmpty()


    private fun propertyButtonHalfAddAnimation(visibility: Boolean) {
        if (visibility) {
            ViewsAnimation.viewScaleUp(binding.addNewAccountHalf, requireContext())
        } else {
            ViewsAnimation.viewScaleDown(binding.addNewAccountHalf, requireContext())
        }
    }

    private fun onBackClick() {
        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setBottomNavViewVisibility(visibility: Int) {
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            .visibility = visibility
    }

    private fun showMessage(message: String, duration: Int) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    private fun getAccountEmail() = binding.addNewEmail.text.toString()

    private fun getAccountPassword() = binding.addNewPassword.text.toString()

    override fun onStop() {
        super.onStop()
        setBottomNavViewVisibility(View.VISIBLE)
    }
}