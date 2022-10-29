package com.ftclub.footballclub.ui.signInActivity.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentRegistrationBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {

    private val userScope = CoroutineScope(Dispatchers.Main)

    private lateinit var _binding: FragmentRegistrationBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
        signUp()
    }

    private fun signUp() {
        binding.signUpButton.setOnClickListener {
            userScope.launch {
                putAccountToDatabase()
            }
        }
    }

    private suspend fun isAccountExist(accountEmail: String): Boolean {
        val accountEmails = dbViewModel.getAccountEmail(accountEmail)

        return accountEmails.isNotEmpty()
    }

    private suspend fun putAccountToDatabase() {
        val userNewEmail = binding.userNewEmail.text.toString()
        val userNewPassword = binding.userNewPassword.text.toString()
        val passwordRepeat = binding.userRepeatPassword.text.toString()

        if (userNewEmail.isEmpty() || userNewPassword.isEmpty() || passwordRepeat.isEmpty()) {
            registrationLinesEmptyAnimation()
        } else {
            if (userNewPassword != passwordRepeat) {
                registrationPasswordsNotMatchAnimation()
            } else if (isAccountExist(userNewEmail)) {
                accountExistAnimation()
            } else {
                val toInformationFragment = arrayOf(userNewEmail, userNewPassword)
                val action =
                    com.ftclub.footballclub.ui.registration.RegistrationFragmentDirections.actionRegistrationFragmentToInformationFragment(
                        toInformationFragment
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun registrationLinesEmptyAnimation() {
        clearFocus()

        val userNewEmailLine = binding.userNewEmail
        val userNewPasswordLine = binding.userNewPassword
        val userPasswordRepeat = binding.userRepeatPassword
        val incorrectMessage = binding.messageRegistration

        if (userNewEmailLine.text.isEmpty()) {
            ViewsAnimation.propertyAnimationShow(userNewEmailLine, requireContext())
            ViewsAnimation.propertyAnimationHide(userNewEmailLine, requireContext())
            ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
        }
        if (userNewPasswordLine.text.isEmpty()) {
            ViewsAnimation.propertyAnimationShow(userNewPasswordLine, requireContext())
            ViewsAnimation.propertyAnimationHide(userNewPasswordLine, requireContext())
            ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
        }
        if (userPasswordRepeat.text.isEmpty()) {
            ViewsAnimation.propertyAnimationShow(userPasswordRepeat, requireContext())
            ViewsAnimation.propertyAnimationHide(userPasswordRepeat, requireContext())
            ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
        }

        incorrectMessage.setText(R.string.empty_reg_lines)
        ViewsAnimation.messageShowAnimation(incorrectMessage, requireContext())
    }

    private fun registrationPasswordsNotMatchAnimation() {
        clearFocus()

        val userNewPasswordLine = binding.userNewPassword
        val userPasswordRepeat = binding.userRepeatPassword
        val incorrectMessage = binding.messageRegistration

        incorrectMessage.setText(R.string.passwords_reg_error)
        ViewsAnimation.messageShowAnimation(incorrectMessage, requireContext())

        ViewsAnimation.propertyAnimationShow(userNewPasswordLine, requireContext())
        ViewsAnimation.propertyAnimationShow(userPasswordRepeat, requireContext())

        ViewsAnimation.propertyAnimationHide(userNewPasswordLine, requireContext())
        ViewsAnimation.propertyAnimationHide(userPasswordRepeat, requireContext())

        ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
    }

    private fun accountExistAnimation() {
        clearFocus()

        val userNewEmailLine = binding.userNewEmail
        val incorrectMessage = binding.messageRegistration

        incorrectMessage.setText(R.string.account_exist_error)

        ViewsAnimation.messageShowAnimation(incorrectMessage, requireContext())
        ViewsAnimation.propertyAnimationShow(userNewEmailLine, requireContext())

        ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
        ViewsAnimation.propertyAnimationHide(userNewEmailLine, requireContext())
    }

    private fun clearFocus() {
        binding.userNewEmail.clearFocus()
        binding.userNewPassword.clearFocus()
        binding.userRepeatPassword.clearFocus()
    }

    private fun navigation() {
        binding.toHomePageFromRegistrationPage.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }

}