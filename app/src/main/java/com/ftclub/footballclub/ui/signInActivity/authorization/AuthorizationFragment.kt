package com.ftclub.footballclub.ui.signInActivity.authorization

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.ui.administratorActivity.AdministratorActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAuthorizationBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import com.ftclub.footballclub.ui.userActivity.UserActivity
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AuthorizationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthorizationFragment : Fragment() {

    private lateinit var _binding: FragmentAuthorizationBinding
    private val binding get() = _binding

    private lateinit var dbViewModel: AccountsViewModel
    private lateinit var accountsList: List<Accounts>
    private lateinit var currentAccount: Accounts

    private val CURRENT_ACCOUNT_EXTRAS_KEY = "current_account"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        dbViewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        requireActivity().window.statusBarColor =
            resources.getColor(R.color.background, resources.newTheme())

        viewsVisibility(View.VISIBLE)
        navigation()
        signIn()
    }

    private fun navigation() {
        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_homeFragment)
        }
    }

    private fun nextActionWithActivityChange() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewsVisibility(View.INVISIBLE)

            requireActivity().window.statusBarColor = resources.getColor(
                R.color.white,
                requireContext().resources.newTheme()
            )

            ViewsAnimation.revealButton(
                binding.signInButton, binding.textSignIn, binding.reveal,
                requireContext()
            )
            ViewsAnimation.fadeOutProgressDialog(binding.progressBarSignIn)

            if (isAccountHasAdministratorRole()) {
                delayedStartAdministratorActivity()
            } else {
                delayedStartUserActivity()
            }
        }, 2000)
    }

    private fun delayedStartAdministratorActivity() {
        val intent = Intent(context, AdministratorActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 100)
    }

    private fun delayedStartUserActivity() {
        val intent = Intent(context, UserActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val currentAccount = binding.userLoginText.text.toString()
            putExtra(CURRENT_ACCOUNT_EXTRAS_KEY, currentAccount)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 100)
    }

    private fun nextActionWithoutActivityChange() {
        Handler(Looper.getMainLooper()).postDelayed({
            ViewsAnimation.fadeOutProgressDialog(binding.progressBarSignIn)
            ViewsAnimation.resetButton(binding.signInButton, binding.textSignIn, requireContext())

            incorrectAuthorizationDataAnimation()
        }, 2000)
    }

    private fun viewsVisibility(visibility: Int) {
        binding.userLogin.visibility = visibility
        binding.userPassword.visibility = visibility
        binding.back.visibility = visibility
        binding.forgetPasswordButton.visibility = visibility
        binding.rememberAccount.visibility = visibility
        binding.signinMessage.visibility = visibility
    }

    private fun signIn() {
        binding.signInButton.setOnClickListener {
            authorizationProcess()
        }
    }

    private fun isPasswordAndAccountEmailCorrect(
        accountEmail: String,
        accountPassword: String
    ): Boolean {
        dbViewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
            accountsList = accounts
        }

        var isCorrect = false

        for (account in accountsList)
            if (accountEmail == account.accountEmail
                && accountPassword == account.hashPassword
            )
                isCorrect = true

        return isCorrect
    }

    private fun isAccountHasAdministratorRole(): Boolean {

        for (account in accountsList) {
            if (binding.userLoginText.text.toString() == account.accountEmail) {
                currentAccount = account
            }
        }

        return this@AuthorizationFragment.currentAccount.accountRole
    }

    private fun authorizationProcess() {
        val accountLogin = binding.userLoginText.text.toString()
        val accountPassword = binding.userPasswordText.text.toString()

        if (isPasswordAndAccountEmailCorrect(accountLogin, accountPassword)) {
            authorizationProcessButtonAnimation()
            nextActionWithActivityChange()
        } else {
            authorizationProcessButtonAnimation()
            nextActionWithoutActivityChange()
        }
    }

    private fun incorrectAuthorizationDataAnimation() {
        binding.message.setText(R.string.incorrect_message)

        ViewsAnimation.messageShowAnimation(binding.message, requireContext())
        ViewsAnimation.propertyAnimationShow(binding.userLoginText, requireContext())
        ViewsAnimation.propertyAnimationShow(binding.userPasswordText, requireContext())

        ViewsAnimation.messageHideAnimation(binding.message, requireContext())
        ViewsAnimation.propertyAnimationHide(binding.userLoginText, requireContext())
        ViewsAnimation.propertyAnimationHide(binding.userPasswordText, requireContext())
    }

    private fun authorizationProcessButtonAnimation() {
        ViewsAnimation.animateButtonWidth(binding.signInButton, requireContext())
        ViewsAnimation.fadeOutTextAndShowProgressDialog(
            binding.textSignIn,
            binding.progressBarSignIn
        )
    }
}