package com.ftclub.footballclub.ui.signInActivity.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            errorMessage("Заполните все требуемые поля")
        } else {
            if (userNewPassword != passwordRepeat) {
                errorMessage("Пароли не совпадают!")
                binding.userLogin.boxBackgroundColor = resources.getColor(R.color.background_about_app, resources.newTheme())
            } else if (isAccountExist(userNewEmail)) {
                errorMessage("Аккаунт с таким email адресом уже существует!")
            } else {
                val toInformationFragment = arrayOf(userNewEmail, userNewPassword)
                val action =
                    RegistrationFragmentDirections.actionRegistrationFragmentToInformationFragment(
                        toInformationFragment
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun errorMessage(message: String) {
        clearFocus()
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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