package com.ftclub.footballclub

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ftclub.footballclub.databinding.ActivitySignInBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max


class SignInActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        val signInBinding = findViewById<FrameLayout>(R.id.sign_in_button)
        signInBinding.setOnClickListener {
            userPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        isViewsVisible(true)
    }

    private fun userPermission() {
        val userLoginLine = findViewById<EditText>(R.id.user_login)
        val userPasswordLine = findViewById<EditText>(R.id.user_password)

        val userNameFromLine = userLoginLine.text.toString()
        val userPasswordFromLine = userPasswordLine.text.toString()

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
        binding.textSignIn.animate().alpha(0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                showProgressDialog()
                }
        }).start()
    }

    private fun animateButtonWidth() {
        val anim = ValueAnimator.ofInt(binding.signInButton.measuredWidth, getFabWidth().toInt())
        anim.addUpdateListener (ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation!!.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = binding.signInButton.layoutParams
            layoutParams.width = value
            binding.signInButton.requestLayout()
        })
        anim.duration = 250
        anim.start()
    }

    private fun showProgressDialog() {
        binding.progressBarSignIn.alpha = 1f
        binding.progressBarSignIn
            .indeterminateDrawable
            .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN)
        binding.progressBarSignIn.visibility = View.VISIBLE
    }

    private fun revealButton() {
        binding.signInButton.elevation = 0f

        binding.reveal.visibility = View.VISIBLE

        var cx = binding.reveal.width
        var cy = binding.reveal.height

        var x = (getFabWidth() / 2 + binding.signInButton.x).toInt()
        var y = (getFabWidth() / 2 + binding.signInButton.y).toInt()

        val finalRadius = max(cx, cy) * 1.2f

        val reveal = ViewAnimationUtils
            .createCircularReveal(binding.reveal, x, y, getFabWidth(), finalRadius)

        reveal.duration = 350
        reveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                reset(animation)
            }

            private fun reset(animation: Animator?) {
                super.onAnimationEnd(animation)
                binding.reveal.visibility = View.INVISIBLE
                binding.textSignIn.visibility = View.VISIBLE
                binding.textSignIn.alpha = 1f
                binding.signInButton.elevation = 4f
                val layoutParams = binding.signInButton.layoutParams
                layoutParams.width = (resources.displayMetrics.density * 300).toInt()
                binding.signInButton.requestLayout()
            }
        })
        reveal.start()
    }

    private fun fadeOutProgressDialog() {
        binding.progressBarSignIn.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
            }
        }).start()
    }

    private fun delayedStartNextActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
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
        binding.textSignIn.visibility = View.VISIBLE
        binding.textSignIn.alpha = 1f
        binding.signInButton.elevation = 4f
        val layoutParams = binding.signInButton.layoutParams
        layoutParams.width = (resources.displayMetrics.density * 300).toInt()
        binding.signInButton.requestLayout()
    }

    private fun isViewsVisible(visibility: Boolean) {
        val userLoginLine = findViewById<EditText>(R.id.user_login)
        val userPasswordLine = findViewById<EditText>(R.id.user_password)
        val authorizationPageText = findViewById<TextView>(R.id.sign_in_text_view)

        if (visibility) {
            userLoginLine.visibility = View.VISIBLE
            userPasswordLine.visibility = View.VISIBLE
            authorizationPageText.visibility = View.VISIBLE
        } else {
            userLoginLine.visibility = View.INVISIBLE
            userPasswordLine.visibility = View.INVISIBLE
            authorizationPageText.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("PrivateResource")
    private fun getFabWidth(): Float {
        return resources.getDimension(com.google.android.material.R.dimen.design_fab_size_normal)
    }

}