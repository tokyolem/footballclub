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
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.MainActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.ui.ViewsAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * A simple [Fragment] subclass.
 * Use the [AuthorizationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthorizationFragment : Fragment() {

    private val userScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_authorization, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()

        isViewsVisible(true)
        navigation()
        signIn()
    }

    private fun navigation() {
        val toSignUpButton = requireActivity().findViewById<FrameLayout>(R.id.to_home_page_from_authorization_page)
        toSignUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_homeFragment)
        }
    }

    private fun delayedStartAdministratorActivity() {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            startActivity(intent)
        }
    }

    private fun nextActionWithActivityChange() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)
        val revealView = requireActivity().findViewById<View>(R.id.reveal)
        val progressBarSignIn = requireActivity().findViewById<ProgressBar>(R.id.progress_bar_sign_in)

        userScope.launch {
            delay(2000)
            
            isViewsVisible(false)
            
            ViewsAnimation.revealButton(signInButton, textSignIn, revealView, requireContext())
            ViewsAnimation.fadeOutProgressDialog(progressBarSignIn)

            delayedStartAdministratorActivity()
        }
    }

    private fun nextActionWithoutActivityChange() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)
        val progressBarSignIn = requireActivity().findViewById<ProgressBar>(R.id.progress_bar_sign_in)

        userScope.launch {
            delay(2000)
            
            ViewsAnimation.fadeOutProgressDialog(progressBarSignIn)
            ViewsAnimation.resetButton(signInButton, textSignIn, requireContext())
            
            incorrectAuthorizationDataAnimation()
        }
    }

    private fun isViewsVisible(visibility: Boolean) {
        val userLogin = requireActivity().findViewById<EditText>(R.id.user_login)
        val userPassword = requireActivity().findViewById<EditText>(R.id.user_password)

        if (visibility) {
            userLogin.visibility = View.VISIBLE
            userPassword.visibility = View.VISIBLE
        } else {
            userLogin.visibility = View.INVISIBLE
            userPassword.visibility = View.INVISIBLE
        }
    }

    private fun signIn() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        val userPasswordLine = requireActivity().findViewById<EditText>(R.id.user_password)
        signInButton.setOnClickListener {
            userScope.launch {
                authorizationProcess()
                userPasswordLine.clearFocus()
            }
        }
    }

    private suspend fun isAdministratorAccount(accountEmail: String): Boolean {
        val currentAccount = SignInActivity.accountsViewModel.getAccountEmail(accountEmail)

        return currentAccount.component1().accountRole
    }

    private suspend fun isPasswordAndAccountEmailCorrect(
        accountEmail: String,
        accountPassword: String): Boolean {
        val currentAccount = SignInActivity.accountsViewModel.getAccountEmail(accountEmail)
        var isCorrect = false
        
        for (account in currentAccount) 
            if (accountEmail == account.accountEmail
            && accountPassword == account.hashPassword) 
                isCorrect = true
        
        return isCorrect
    }

    private suspend fun authorizationProcess() {
        val accountLoginLine = requireActivity().findViewById<EditText>(R.id.user_login)
        val accountPasswordLine = requireActivity().findViewById<EditText>(R.id.user_password)

        val accountLogin = accountLoginLine.text.toString()
        val accountPassword = accountPasswordLine.text.toString()

        if (isPasswordAndAccountEmailCorrect(accountLogin, accountPassword)) {
            if (isAdministratorAccount(accountLogin)) {
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
        val accountLoginLine = requireActivity().findViewById<EditText>(R.id.user_login)
        val accountPasswordLine = requireActivity().findViewById<EditText>(R.id.user_password)
        val incorrectMessage = requireActivity().findViewById<TextView>(R.id.message)

        incorrectMessage.setText(R.string.incorrect_message)

        ViewsAnimation.messageShowAnimation(incorrectMessage, requireContext())
        ViewsAnimation.propertyAnimationShow(accountLoginLine, requireContext())
        ViewsAnimation.propertyAnimationShow(accountPasswordLine, requireContext())

        ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
        ViewsAnimation.propertyAnimationHide(accountLoginLine, requireContext())
        ViewsAnimation.propertyAnimationHide(accountPasswordLine, requireContext())
    }

    private fun authorizationProcessButtonAnimation() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)
        val progressBarSignIn = requireActivity().findViewById<ProgressBar>(R.id.progress_bar_sign_in)

        ViewsAnimation.animateButtonWidth(signInButton, requireContext())
        ViewsAnimation.fadeOutTextAndShowProgressDialog(textSignIn, progressBarSignIn)
    }
}