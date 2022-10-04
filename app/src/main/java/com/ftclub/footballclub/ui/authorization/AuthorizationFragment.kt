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

    private fun fadeOutTextAndShowProgressDialog() {
        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)

        textSignIn.animate().alpha(0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    showProgressDialog()
                }
            }).start()
    }

    private fun animateButtonWidth() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)

        val anim = ValueAnimator.ofInt(signInButton.measuredWidth, getFabWidth().toInt())
        anim.addUpdateListener (ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation!!.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = signInButton.layoutParams
            layoutParams.width = value
            signInButton.requestLayout()
        })
        anim.duration = 250
        anim.start()
    }

    private fun showProgressDialog() {
        val progressBarSignIn = requireActivity().findViewById<ProgressBar>(R.id.progress_bar_sign_in)
        progressBarSignIn.alpha = 1f
        progressBarSignIn
            .indeterminateDrawable
            .colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor("#ffffff"), BlendModeCompat.SRC_IN)
        progressBarSignIn.visibility = View.VISIBLE
    }

    private fun revealButton() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)
        val revealView = requireActivity().findViewById<View>(R.id.reveal)

        signInButton.elevation = 0f

        revealView.visibility = View.VISIBLE

        val cx = revealView.width
        val cy = revealView.height

        val x = (getFabWidth() / 2 + signInButton.x).toInt()
        val y = (getFabWidth() / 2 + signInButton.y).toInt()

        val finalRadius = max(cx, cy) * 1.2f

        val reveal = ViewAnimationUtils
            .createCircularReveal(revealView, x, y, getFabWidth(), finalRadius)

        reveal.duration = 350
        reveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                reset(animation)
            }

            private fun reset(animation: Animator?) {
                super.onAnimationEnd(animation)
                revealView.visibility = View.INVISIBLE
                textSignIn.visibility = View.VISIBLE
                textSignIn.alpha = 1f
                signInButton.elevation = 4f
                val layoutParams = signInButton.layoutParams
                layoutParams.width = (resources.displayMetrics.density * 300).toInt()
                signInButton.requestLayout()
            }
        })
        reveal.start()
    }

    private fun fadeOutProgressDialog() {
        val progressBarSignIn = requireActivity().findViewById<ProgressBar>(R.id.progress_bar_sign_in)

        progressBarSignIn.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
            }
        }).start()
    }

    private fun delayedStartNextActivity() {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            startActivity(intent)
        }
    }

    private fun nextActionWithActivityChange() {
        userScope.launch {
            delay(2000)
            
            isViewsVisible(false)
            
            revealButton()
            fadeOutProgressDialog()
            delayedStartNextActivity()
        }
    }

    private fun nextActionWithoutActivityChange() {
        userScope.launch {
            delay(2000)
            
            fadeOutProgressDialog()
            resetSignInButton()
            
            incorrectAuthorizationDataAnimation()
            delay(2000)
            propertyAnimationHide()
        }
    }

    private fun resetSignInButton() {
        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)

        textSignIn.visibility = View.VISIBLE
        textSignIn.alpha = 1f
        signInButton.elevation = 4f
        val layoutParams = signInButton.layoutParams
        layoutParams.width = (resources.displayMetrics.density * 300).toInt()
        signInButton.requestLayout()
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

    @SuppressLint("PrivateResource")
    private fun getFabWidth(): Float {
        return resources.getDimension(com.google.android.material.R.dimen.design_fab_size_normal)
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
                animateButtonWidth()
                fadeOutTextAndShowProgressDialog()
                nextActionWithActivityChange()
            } else {
                animateButtonWidth()
                fadeOutTextAndShowProgressDialog()
                nextActionWithActivityChange()
            }
        } else {
            animateButtonWidth()
            fadeOutTextAndShowProgressDialog()
            nextActionWithoutActivityChange()
        }
    }

    private fun incorrectAuthorizationDataAnimation() {
        val accountLoginLine = requireActivity().findViewById<EditText>(R.id.user_login)
        val accountPasswordLine = requireActivity().findViewById<EditText>(R.id.user_password)
        val incorrectMessage = requireActivity().findViewById<TextView>(R.id.message)

        val animationBegin = AnimationUtils.loadAnimation(context, R.anim.little_trans_begin)
        val animationEnd = AnimationUtils.loadAnimation(context, R.anim.little_trans_end)


        accountLoginLine.backgroundTintList =
            ColorStateList.valueOf(resources.getColor(R.color.incorrect_data, resources.newTheme()))

        accountPasswordLine.setText("")
        accountPasswordLine.backgroundTintList =
            ColorStateList.valueOf(resources.getColor(R.color.incorrect_data, resources.newTheme()))

        incorrectMessage.visibility = View.VISIBLE
        
        accountLoginLine.startAnimation(animationBegin)
        accountLoginLine.startAnimation(animationEnd)
        accountPasswordLine.startAnimation(animationBegin)
        accountPasswordLine.startAnimation(animationEnd)
    }

    private fun propertyAnimationHide() {
        val userLoginLine = requireActivity().findViewById<EditText>(R.id.user_login)
        val userPasswordLine = requireActivity().findViewById<EditText>(R.id.user_password)
        val incorrectMessage = requireActivity().findViewById<TextView>(R.id.message)

        val animationMessageHide = AnimationUtils.loadAnimation(context, R.anim.alpha_message)

        val colorTo: Int = resources.getColor(R.color.AppBarColor, resources.newTheme())
        val colorFrom: Int = resources.getColor(R.color.incorrect_data, resources.newTheme())

        val colorAnimation: ValueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 500
        colorAnimation.addUpdateListener {
            userLoginLine.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
            userPasswordLine.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        colorAnimation.start()

        incorrectMessage.startAnimation(animationMessageHide)
        incorrectMessage.visibility = View.INVISIBLE
    }
}