package com.ftclub.footballclub.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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
import com.ftclub.footballclub.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

object ViewsAnimation {

    private val userScope = CoroutineScope(Dispatchers.Main)

    fun messageShowAnimation(textView: TextView, context: Context) {
        val animationMessageHide = AnimationUtils.loadAnimation(context, R.anim.alpha_message_begin)
        textView.startAnimation(animationMessageHide)
        textView.visibility = View.VISIBLE
    }

    fun messageHideAnimation(textView: TextView, context: Context) {
        userScope.launch {
            delay(2000)
            val animationMessageHide = AnimationUtils.loadAnimation(context, R.anim.alpha_message_end)
            textView.startAnimation(animationMessageHide)
            textView.visibility = View.INVISIBLE
        }
    }

    fun propertyAnimationShow(editTextLine: EditText, context: Context) {
        val colorFrom: Int = context.resources.getColor(R.color.AppBarColor, context.resources.newTheme())
        val colorTo: Int = context.resources.getColor(R.color.incorrect_data, context.resources.newTheme())

        val colorAnimation: ValueAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 150
        colorAnimation.addUpdateListener {
            editTextLine.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        colorAnimation.start()
    }

    fun propertyAnimationHide(editTextLine: EditText, context: Context) {
        userScope.launch {
            delay(2000)

            val colorTo: Int = context.resources.getColor(R.color.AppBarColor, context.resources.newTheme())
            val colorFrom: Int = context.resources.getColor(R.color.incorrect_data, context.resources.newTheme())

            val colorAnimation: ValueAnimator =
                ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 500
            colorAnimation.addUpdateListener {
                editTextLine.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
            }
            colorAnimation.start()
        }
    }

    fun revealButton(frameLayout: FrameLayout, textView: TextView, revealView: View, context: Context) {
        frameLayout.elevation = 0f

        revealView.visibility = View.VISIBLE

        val cx = revealView.width
        val cy = revealView.height

        val x = (getFabWidth(context) / 2 + frameLayout.x).toInt()
        val y = (getFabWidth(context) / 2 + frameLayout.y).toInt()

        val finalRadius = max(cx, cy) * 1.2f

        val reveal = ViewAnimationUtils
            .createCircularReveal(revealView, x, y, getFabWidth(context), finalRadius)

        reveal.duration = 350
        reveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                reset(animation)
            }

            private fun reset(animation: Animator?) {
                super.onAnimationEnd(animation)
                revealView.visibility = View.INVISIBLE
                textView.visibility = View.VISIBLE
                textView.alpha = 1f
                frameLayout.elevation = 4f
                val layoutParams = frameLayout.layoutParams
                layoutParams.width = (context.resources.displayMetrics.density * 300).toInt()
                frameLayout.requestLayout()
            }
        })
        reveal.start()
    }

    fun animateButtonWidth(frameLayout: FrameLayout, context: Context) {
        val anim = ValueAnimator.ofInt(frameLayout.measuredWidth, getFabWidth(context).toInt())
        anim.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = frameLayout.layoutParams
            layoutParams.width = value
            frameLayout.requestLayout()
        }
        anim.duration = 250
        anim.start()
    }

    fun showProgressDialog(progressBar: ProgressBar) {
        progressBar.alpha = 1f
        progressBar
            .indeterminateDrawable
            .colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor("#ffffff"), BlendModeCompat.SRC_IN)
        progressBar.visibility = View.VISIBLE
    }

    fun fadeOutTextAndShowProgressDialog(textView: TextView, progressBar: ProgressBar) {
//        val textSignIn = requireActivity().findViewById<TextView>(R.id.text_sign_in)

        textView.animate().alpha(0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    showProgressDialog(progressBar)
                }
            }).start()
    }

    fun fadeOutProgressDialog(progressBar: ProgressBar) {
        progressBar.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
            }
        }).start()
    }

    fun resetButton(frameLayout: FrameLayout, textView: TextView, context: Context) {
        textView.visibility = View.VISIBLE
        textView.alpha = 1f
        frameLayout.elevation = 4f
        val layoutParams = frameLayout.layoutParams
        layoutParams.width = (context.resources.displayMetrics.density * 300).toInt()
        frameLayout.requestLayout()
    }

    @SuppressLint("PrivateResource")
    private fun getFabWidth(context: Context): Float {
        return context.resources.getDimension(com.google.android.material.R.dimen.design_fab_size_normal)
    }

}