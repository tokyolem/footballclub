package com.ftclub.footballclub.ui.authorization

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.AdministratorActivity
import com.ftclub.footballclub.MainActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.basic.room.accounts.viewModel.AccountsViewModel
import com.ftclub.footballclub.databinding.FragmentAuthorizationBinding
import com.ftclub.footballclub.ui.ViewsAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.math.max

/**
 * A simple [Fragment] subclass.
 * Use the [AuthorizationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthorizationFragment : Fragment() {

    private lateinit var _binding: FragmentAuthorizationBinding
    private val binding get() = _binding

    private lateinit var viewModel: AccountsViewModel
    lateinit var accountsList: List<Accounts>

    private val ACCOUNTS_EXTRA_KEY = "accounts_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[AccountsViewModel::class.java]

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        isViewsVisible(true)
        navigation()
        signIn()
    }

    private fun navigation() {
        binding.toHomePageFromAuthorizationPage.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_homeFragment)
        }
    }

    private fun delayedStartAdministratorActivity() {
        val intent = Intent(context, AdministratorActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val serializableList = accountsList as ArrayList<Accounts>
            putExtra(ACCOUNTS_EXTRA_KEY, serializableList as java.io.Serializable)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 100)
    }

    private fun nextActionWithActivityChange() {
        Handler(Looper.getMainLooper()).postDelayed({
            isViewsVisible(false)

            requireActivity().window.statusBarColor = resources.getColor(
                R.color.background_about_app,
                requireContext().resources.newTheme()
            )

            ViewsAnimation.revealButton(
                binding.signInButton,
                binding.textSignIn,
                binding.reveal,
                requireContext()
            )
            ViewsAnimation.fadeOutProgressDialog(binding.progressBarSignIn)

            delayedStartAdministratorActivity()
        }, 2000)

    }

    private fun nextActionWithoutActivityChange() {
        Handler(Looper.getMainLooper()).postDelayed({
            ViewsAnimation.fadeOutProgressDialog(binding.progressBarSignIn)
            ViewsAnimation.resetButton(binding.signInButton, binding.textSignIn, requireContext())

            incorrectAuthorizationDataAnimation()
        }, 2000)
    }

    private fun isViewsVisible(visibility: Boolean) {
        if (visibility) {
            binding.userLogin.visibility = View.VISIBLE
            binding.userPassword.visibility = View.VISIBLE
            binding.toHomePageFromAuthorizationPage.visibility = View.VISIBLE
            binding.rememberAccount.visibility = View.VISIBLE
            binding.signinMessage.visibility = View.VISIBLE
        } else {
            binding.userLogin.visibility = View.INVISIBLE
            binding.userPassword.visibility = View.INVISIBLE
            binding.toHomePageFromAuthorizationPage.visibility = View.INVISIBLE
            binding.rememberAccount.visibility = View.INVISIBLE
            binding.signinMessage.visibility = View.INVISIBLE
        }
    }

    private fun signIn() {
        binding.signInButton.setOnClickListener {
            authorizationProcess()
        }
    }

    private fun isAdministratorAccount(): Boolean {
        return accountsList.component1().accountRole
    }

    private fun isPasswordAndAccountEmailCorrect(
        accountEmail: String,
        accountPassword: String
    ): Boolean {
        viewModel.accountsLiveData.observe(viewLifecycleOwner) { accounts ->
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

    private fun authorizationProcess() {
        val accountLogin = binding.userLogin.text.toString()
        val accountPassword = binding.userPassword.text.toString()

        if (isPasswordAndAccountEmailCorrect(accountLogin, accountPassword)) {
            if (isAdministratorAccount()) {
                authorizationProcessButtonAnimation()
                nextActionWithActivityChange()
            } else {
                authorizationProcessButtonAnimation()
                nextActionWithActivityChange()
            }
        } else {
            authorizationProcessButtonAnimation()
            nextActionWithoutActivityChange()
        }
    }

    private fun incorrectAuthorizationDataAnimation() {
        binding.message.setText(R.string.incorrect_message)

        ViewsAnimation.messageShowAnimation(binding.message, requireContext())
        ViewsAnimation.propertyAnimationShow(binding.userLogin, requireContext())
        ViewsAnimation.propertyAnimationShow(binding.userPassword, requireContext())

        ViewsAnimation.messageHideAnimation(binding.message, requireContext())
        ViewsAnimation.propertyAnimationHide(binding.userLogin, requireContext())
        ViewsAnimation.propertyAnimationHide(binding.userPassword, requireContext())
    }

    private fun authorizationProcessButtonAnimation() {
        ViewsAnimation.animateButtonWidth(binding.signInButton, requireContext())
        ViewsAnimation.fadeOutTextAndShowProgressDialog(
            binding.textSignIn,
            binding.progressBarSignIn
        )
    }
}