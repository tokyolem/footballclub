package com.ftclub.footballclub.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.bumptech.glide.Glide
import com.ftclub.footballclub.MainActivity
import com.ftclub.footballclub.R
import com.ftclub.footballclub.SignInActivity
import com.ftclub.footballclub.databinding.FragmentAuthorizationBinding
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_authorization, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        isViewsVisible(true)

        val signInButton = requireActivity().findViewById<FrameLayout>(R.id.sign_in_button)
        signInButton!!.setOnClickListener {
            userPermission()
        }
    }

    private fun userPermission() {
        val userLogin = requireActivity().findViewById<EditText>(R.id.user_login)
        val userPassword = requireActivity().findViewById<EditText>(R.id.user_password)

        val userNameFromLine = userLogin.text.toString()
        val userPasswordFromLine = userPassword.text.toString()

        if (userNameFromLine == "admin" && userPasswordFromLine == "admin"){
            animateButtonWidth()
            fadeOutTextAndShowProgressDialog()
            nextActionWithActivityChange()
        } else {
            animateButtonWidth()
            fadeOutTextAndShowProgressDialog()
            nextActionWithoutActivityChange()
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

        var cx = revealView.width
        var cy = revealView.height

        var x = (getFabWidth() / 2 + signInButton.x).toInt()
        var y = (getFabWidth() / 2 + signInButton.y).toInt()

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
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            isViewsVisible(false)
            revealButton()
            fadeOutProgressDialog()
            delayedStartNextActivity()
        }
    }

    private fun nextActionWithoutActivityChange() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            fadeOutProgressDialog()
            resetSignInButton()
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
        val signInTextView = requireActivity().findViewById<TextView>(R.id.sign_in_text_view)

        if (visibility) {
            userLogin.visibility = View.VISIBLE
            userPassword.visibility = View.VISIBLE
            signInTextView.visibility = View.VISIBLE
        } else {
            userLogin.visibility = View.INVISIBLE
            userPassword.visibility = View.INVISIBLE
            signInTextView.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("PrivateResource")
    private fun getFabWidth(): Float {
        return resources.getDimension(com.google.android.material.R.dimen.design_fab_size_normal)
    }
}