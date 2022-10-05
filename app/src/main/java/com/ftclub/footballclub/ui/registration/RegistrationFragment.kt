package com.ftclub.footballclub.ui.registration

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.ftclub.footballclub.R
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.basic.room.accounts.accountsObject.Accounts
import com.ftclub.footballclub.ui.ViewsAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {

    private val userScope = CoroutineScope(Dispatchers.Main)

    private var isAnimationActive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation()
        signUp()
    }

    private fun signUp() {
        val signUpButton = requireActivity().findViewById<FrameLayout>(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            userScope.launch {
                putAccountToDatabase()
            }
        }
    }

    private suspend fun isAccountExist(accountEmail: String): Boolean {
        val accountEmails = SignInActivity.accountsViewModel.getAccountEmail(accountEmail)

        return accountEmails.isNotEmpty()
    }

    private suspend fun putAccountToDatabase () {
        val userNewEmailLine = requireActivity().findViewById<EditText>(R.id.user_new_email)
        val userNewPasswordLine = requireActivity().findViewById<EditText>(R.id.user_new_password)
        val userPasswordRepeat = requireActivity().findViewById<EditText>(R.id.user_repeat_password)

        val userNewEmail = userNewEmailLine.text.toString()
        val userNewPassword = userNewPasswordLine.text.toString()
        val passwordRepeat = userPasswordRepeat.text.toString()


        if (userNewEmail.isEmpty() || userNewPassword.isEmpty() || passwordRepeat.isEmpty()) {
            registrationLinesEmptyAnimation()
        } else {
            if (userNewPassword != passwordRepeat) {
                registrationPasswordsNotMatchAnimation()
            }  else if (isAccountExist(userNewEmail)) {
                accountExistAnimation()

        if (userNewPassword != passwordRepeat) {
            if (!isAnimationActive)
                registrationPasswordsNotMatch()
        } else {
            if (userNewEmail == "" || userNewPassword == "") {
                if (!isAnimationActive)
                    registrationLinesEmptyAnimation()
            } else if (isAccountExist(userNewEmail)) {
                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)

            } else {
                SignInActivity.accountsViewModel.insertAccount(
                    Accounts(userNewEmail, userNewPassword, false)
                )
            }
        }
    }


    private fun editTextLineAnimation(editTextLine: EditText) {
        val colorFrom: Int = resources.getColor(R.color.AppBarColor, resources.newTheme())
        val colorTo: Int = resources.getColor(R.color.incorrect_data, resources.newTheme())

        val colorAnimation: ValueAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 150
        colorAnimation.addUpdateListener {
            editTextLine.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        colorAnimation.start()
    }

    private fun registrationLinesEmptyAnimation() {
        clearFocus()

        val userNewEmailLine = requireActivity().findViewById<EditText>(R.id.user_new_email)
        val userNewPasswordLine = requireActivity().findViewById<EditText>(R.id.user_new_password)
        val userPasswordRepeat = requireActivity().findViewById<EditText>(R.id.user_repeat_password)
        val incorrectMessage = requireActivity().findViewById<TextView>(R.id.message_registration)

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

            editTextLineAnimation(userNewEmailLine)
            propertyAnimationHide(userNewEmailLine)
            messageHideAnimation(incorrectMessage)
        }
        if (userNewPasswordLine.text.isEmpty()) {
            editTextLineAnimation(userNewPasswordLine)
            propertyAnimationHide(userNewPasswordLine)
            messageHideAnimation(incorrectMessage)
        }
        if (userPasswordRepeat.text.isEmpty()) {
            editTextLineAnimation(userPasswordRepeat)
            propertyAnimationHide(userPasswordRepeat)
            messageHideAnimation(incorrectMessage)
        }
        incorrectMessage.setText(R.string.empty_reg_lines)
        messageShowAnimation(incorrectMessage)
    }

    private fun registrationPasswordsNotMatch() {

        clearFocus()

        val userNewPasswordLine = requireActivity().findViewById<EditText>(R.id.user_new_password)
        val userPasswordRepeat = requireActivity().findViewById<EditText>(R.id.user_repeat_password)
        val incorrectMessage = requireActivity().findViewById<TextView>(R.id.message_registration)

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

        val userNewEmailLine = requireActivity().findViewById<EditText>(R.id.user_new_email)
        val incorrectMessage = requireActivity().findViewById<TextView>(R.id.message_registration)

        incorrectMessage.setText(R.string.account_exist_error)

        ViewsAnimation.messageShowAnimation(incorrectMessage, requireContext())
        ViewsAnimation.propertyAnimationShow(userNewEmailLine, requireContext())

        ViewsAnimation.messageHideAnimation(incorrectMessage, requireContext())
        ViewsAnimation.propertyAnimationHide(userNewEmailLine, requireContext())

        messageShowAnimation(incorrectMessage)

        editTextLineAnimation(userNewPasswordLine)
        editTextLineAnimation(userPasswordRepeat)

        propertyAnimationHide(userNewPasswordLine)
        propertyAnimationHide(userPasswordRepeat)

        messageHideAnimation(incorrectMessage)

    }

    private fun propertyAnimationHide(editTextLine: EditText) {
        userScope.launch {
            delay(2000)

            val colorTo: Int = resources.getColor(R.color.AppBarColor, resources.newTheme())
            val colorFrom: Int = resources.getColor(R.color.incorrect_data, resources.newTheme())

            val colorAnimation: ValueAnimator =
                ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 500
            colorAnimation.addUpdateListener {
                editTextLine.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
            }
            colorAnimation.start()
        }
    }

    private fun messageHideAnimation(textView: TextView) {
        userScope.launch {
            delay(2000)
            val animationMessageHide = AnimationUtils.loadAnimation(context, R.anim.alpha_message)
            textView.startAnimation(animationMessageHide)
            textView.visibility = View.INVISIBLE
        }
    }

    private fun messageShowAnimation(textView: TextView) {
        val animationMessageHide = AnimationUtils.loadAnimation(context, R.anim.alpha_message_begin)
        textView.startAnimation(animationMessageHide)
        textView.visibility = View.VISIBLE

    }

    private fun clearFocus() {
        val userNewEmailLine = requireActivity().findViewById<EditText>(R.id.user_new_email)
        val userNewPasswordLine = requireActivity().findViewById<EditText>(R.id.user_new_password)
        val userPasswordRepeat = requireActivity().findViewById<EditText>(R.id.user_repeat_password)

        userNewEmailLine.clearFocus()
        userNewPasswordLine.clearFocus()
        userPasswordRepeat.clearFocus()
    }


    private fun navigation() {
        val toSignInButton = requireActivity().findViewById<FrameLayout>(R.id.to_home_page_from_registration_page)
        toSignInButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }


}