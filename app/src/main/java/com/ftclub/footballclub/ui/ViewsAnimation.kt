package com.ftclub.footballclub.ui

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.ftclub.footballclub.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

object ViewsAnimation {

    const val SCALE_ANIMATION = 1
    const val ALPHA_ANIMATION = 2

    fun turnIn(context: Context, v: View) {
        val turnAnim = AnimationUtils.loadAnimation(context, R.anim.turn_in)

        turnAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                (v as MaterialButton).icon = ResourcesCompat.getDrawable(
                    context.resources, R.drawable.ic_baseline_arrow_drop_up_24,
                    context.resources.newTheme()
                )
            }
        })

        v.startAnimation(turnAnim)
    }

    fun turnOut(context: Context, v: View) {
        val turnAnim = AnimationUtils.loadAnimation(context, R.anim.turn_out)

        turnAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                (v as MaterialButton).icon = ResourcesCompat.getDrawable(
                    context.resources, R.drawable.ic_baseline_arrow_drop_down_24,
                    context.resources.newTheme()
                )
            }
        })

        v.startAnimation(turnAnim)
    }

    fun messageShowAnimation(textView: TextView, context: Context) {
        val animationMessageHide = AnimationUtils.loadAnimation(context, R.anim.alpha_message_begin)
        textView.startAnimation(animationMessageHide)
        textView.visibility = View.VISIBLE
    }

    fun messageHideAnimation(textView: TextView, context: Context) {
        Handler(Looper.getMainLooper()).postDelayed({
            val animationMessageHide =
                AnimationUtils.loadAnimation(context, R.anim.alpha_message_end)
            textView.startAnimation(animationMessageHide)
            textView.visibility = View.INVISIBLE
        }, 2000)
    }

    fun viewShowAnimation(view: View, context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_message_begin)
        view.startAnimation(animation)
        view.visibility = View.VISIBLE
    }

    fun viewHideAnimation(view: View, context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_message_end)
        view.startAnimation(animation)
        view.visibility = View.GONE
    }

    fun viewScaleUp(view: View, context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        view.startAnimation(animation)
        view.visibility = View.VISIBLE
    }

    fun viewScaleDown(view: View, context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        view.startAnimation(animation)
        view.visibility = View.INVISIBLE
    }

    fun propertyAnimationShow(editTextLine: EditText, context: Context) {

        val colorFrom = 1627389952
        val colorTo: Int =
            context.resources.getColor(R.color.incorrect_data, context.resources.newTheme())

        val colorAnimation: ValueAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 150
        colorAnimation.addUpdateListener {
            editTextLine.setHintTextColor(ColorStateList.valueOf(it.animatedValue as Int))
        }
        colorAnimation.start()
    }

    fun propertyAnimationHide(editTextLine: EditText, context: Context) {
        Handler(Looper.getMainLooper()).postDelayed({
            val colorTo = 1627389952
            val colorFrom: Int =
                context.resources.getColor(R.color.incorrect_data, context.resources.newTheme())

            val colorAnimation: ValueAnimator =
                ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 500
            colorAnimation.addUpdateListener {
                editTextLine.setHintTextColor(ColorStateList.valueOf(it.animatedValue as Int))
            }
            colorAnimation.start()
        }, 2000)
    }

    fun revealButton(
        frameLayout: FrameLayout,
        textView: TextView,
        revealView: View,
        context: Context
    ) {
        frameLayout.elevation = 0f

        revealView.visibility = View.VISIBLE

        val cx = revealView.width
        val cy = revealView.height

        val x = (getFabWidth(context) / 2 + frameLayout.x).toInt()
        val y = (getFabWidth(context) / 2 + frameLayout.y).toInt()

        val finalRadius = max(cx, cy) * 1.2f

        val reveal = ViewAnimationUtils
            .createCircularReveal(revealView, x, y, getFabWidth(context), finalRadius)

        reveal.duration = 400
        reveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                reset(animation)
            }

            private fun reset(animation: Animator?) {
                super.onAnimationEnd(animation!!)
                revealView.visibility = View.INVISIBLE
                textView.visibility = View.VISIBLE
                textView.alpha = 1f
                frameLayout.elevation = 4f
                val layoutParams = frameLayout.layoutParams
                layoutParams.width = (context.resources.displayMetrics.density * 357).toInt()
                frameLayout.requestLayout()
            }
        })
        reveal.start()
    }

    fun animateButtonWidth(frameLayout: FrameLayout, context: Context) {
        val anim = ValueAnimator.ofInt(frameLayout.measuredWidth, getFabWidth(context).toInt())
        anim.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
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
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.parseColor("#000000"),
                BlendModeCompat.SRC_IN
            )
        progressBar.visibility = View.VISIBLE
    }

    fun fadeOutTextAndShowProgressDialog(textView: TextView, progressBar: ProgressBar) {
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
        progressBar.animate().alpha(0f).setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            }).start()
    }

    fun resetButton(frameLayout: FrameLayout, textView: TextView, context: Context) {
        textView.visibility = View.VISIBLE
        textView.alpha = 1f
        frameLayout.elevation = 4f
        val layoutParams = frameLayout.layoutParams
        layoutParams.width = (context.resources.displayMetrics.density * 357).toInt()
        frameLayout.requestLayout()
    }

    @SuppressLint("PrivateResource")
    private fun getFabWidth(context: Context): Float {
        return context.resources.getDimension(com.google.android.material.R.dimen.design_fab_size_normal)
    }

}