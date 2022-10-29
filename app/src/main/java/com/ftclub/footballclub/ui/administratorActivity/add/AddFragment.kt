package com.ftclub.footballclub.ui.administratorActivity.add

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.DateTime
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAddBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private lateinit var viewModel: AddViewModel
    private lateinit var accountsViewModel: AccountsViewModel

    private val userScope = CoroutineScope(Dispatchers.Main)

    private lateinit var _binding: FragmentAddBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this)[AddViewModel::class.java]
        accountsViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        BottomSheetBehavior.from(binding.bottomSheetAdd).apply {
            peekHeight = 200
            Handler(Looper.getMainLooper()).postDelayed({
                this.state = BottomSheetBehavior.STATE_EXPANDED
            }, 100)
        }

        binding.toNextActionAddReg.visibility = View.INVISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addKindSelectAction()

        binding.toNextActionAddReg.setOnClickListener {
            userScope.launch {
                addingFullProcess()
            }
        }

        binding.addNewAccountHalf.setOnClickListener {
            userScope.launch {
                addingHalfProcess()
            }
        }
    }

    private suspend fun isAccountExist(accountEmail: String): Boolean {
        val accountEmails = accountsViewModel.getAccountEmail(accountEmail)

        return accountEmails.isNotEmpty()
    }

    private fun addKindSelectAction() {
        binding.addFullAccount.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheetAdd).state =
                BottomSheetBehavior.STATE_COLLAPSED

            if (binding.toNextActionAddReg.visibility != View.VISIBLE)
                ViewsAnimation.viewScaleUp(binding.toNextActionAddReg, requireContext())

            propertyLinesShowAnimation()

            if (binding.addNewAccountHalf.visibility == View.VISIBLE) {
                propertyButtonHalfAddAnimation(false)
            }
        }

        binding.addHalfAccount.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheetAdd).state =
                BottomSheetBehavior.STATE_COLLAPSED

            if (binding.toNextActionAddReg.visibility == View.VISIBLE)
                ViewsAnimation.viewScaleDown(binding.toNextActionAddReg, requireContext())

            propertyLinesShowAnimation()

            if (binding.addNewAccountHalf.visibility != View.VISIBLE) {
                propertyButtonHalfAddAnimation(true)
            }
        }
    }

    private fun isLinesEmpty() =
        binding.addNewEmail.text.isEmpty()
                || binding.addNewPassword.text.isEmpty()
                || binding.addRepeatPassword.text.isEmpty()

    private suspend fun addingHalfProcess() {
        if (isLinesEmpty()) {
            propertyLinesEmptyAnimation()
            return
        } else {
            if (binding.addNewPassword.text.toString() != binding.addRepeatPassword.text.toString()) {
                showMessage("Пароли не совпадают!", Toast.LENGTH_LONG)
                return
            } else if (isAccountExist(getAccountEmail())) {
                showMessage("Аккаунт с таким email адресом уже существует!", Toast.LENGTH_LONG)
                return
            } else {
                accountsViewModel.insertAccount(
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

    private suspend fun addingFullProcess() {
        if (isLinesEmpty()) {
            propertyLinesEmptyAnimation()
            return
        } else {
            if (binding.addNewPassword.text.toString() != binding.addRepeatPassword.text.toString()) {
                showMessage("Пароли не совпадают!", Toast.LENGTH_LONG)
                return
            } else if (isAccountExist(getAccountEmail())) {
                showMessage("Аккаунт с таким email адресом уже существует!", Toast.LENGTH_LONG)
                return
            } else {
                val toAddInformationFragment = arrayOf(getAccountEmail(), getAccountPassword())
                val action =
                    com.ftclub.footballclub.ui.add.AddFragmentDirections.actionNavigationAddToAddInformationFragment(
                        toAddInformationFragment
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun propertyLinesEmptyAnimation() {
        if (binding.addNewEmail.text.isEmpty()) {
            ViewsAnimation.propertyAnimationShow(binding.addNewEmail, requireContext())
            ViewsAnimation.propertyAnimationHide(binding.addNewEmail, requireContext())
        }
        if (binding.addNewPassword.text.isEmpty()) {
            ViewsAnimation.propertyAnimationShow(binding.addNewPassword, requireContext())
            ViewsAnimation.propertyAnimationHide(binding.addNewPassword, requireContext())
        }
        if (binding.addRepeatPassword.text.isEmpty()) {
            ViewsAnimation.propertyAnimationShow(binding.addRepeatPassword, requireContext())
            ViewsAnimation.propertyAnimationHide(binding.addRepeatPassword, requireContext())
        }

        showMessage("Заполните все необходимые поля!", Toast.LENGTH_LONG)
    }

    private fun propertyLinesShowAnimation() {
        if (binding.addNewEmail.visibility == View.VISIBLE) {
            return
        } else {
            ViewsAnimation.viewShowAnimation(binding.addNewEmail, requireContext())
            ViewsAnimation.viewShowAnimation(binding.addNewPassword, requireContext())
            ViewsAnimation.viewShowAnimation(binding.addRepeatPassword, requireContext())
        }
    }

    private fun propertyButtonHalfAddAnimation(visibility: Boolean) {
        if (visibility) {
            ViewsAnimation.viewShowAnimation(binding.addNewAccountHalf, requireContext())
        } else {
            ViewsAnimation.viewHideAnimation(binding.addNewAccountHalf, requireContext())
        }
    }

    private fun showMessage(message: String, duration: Int) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    private fun getAccountEmail() = binding.addNewEmail.text.toString()

    private fun getAccountPassword() = binding.addNewPassword.text.toString()

}