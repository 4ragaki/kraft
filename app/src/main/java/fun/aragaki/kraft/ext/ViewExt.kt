package `fun`.aragaki.kraft.ext

import `fun`.aragaki.kraft.views.CircularReveal
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.snackbar.Snackbar

fun View.snack(
    str: String?, actionText: String? = null, length: Int = Snackbar.LENGTH_LONG,
    action: ((view: View) -> Unit)? = null
) = Snackbar.make(this, str ?: "", length).apply {
    action?.let { setAction(actionText, action) }
}.show()

fun View.applyVerticalInsets(window: Window, callbacks: Array<(Insets) -> Unit>) {
    setOnApplyWindowInsetsListener { _, insets ->
        with(WindowInsetsCompat.toWindowInsetsCompat(window.decorView.rootWindowInsets)) {
            callbacks.forEach { it(stableInsets) }
        }
        insets
    }
}

fun View.withCircularReveal(circularReveal: CircularReveal) = this.apply {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
        ) {
            removeOnLayoutChangeListener(this)
            ViewAnimationUtils.createCircularReveal(
                v, circularReveal.centerX,
                circularReveal.centerY,
                0f,
                circularReveal.hypot
            ).apply {
                interpolator = FastOutSlowInInterpolator()
                duration = 300
                start()

                ValueAnimator().apply {
                    setIntValues(circularReveal.startColor, circularReveal.endColor)
                    setEvaluator(ArgbEvaluator())
                    addUpdateListener {
                        v?.setBackgroundColor(animatedValue as Int)
                    }
                    duration = 300
                    start()
                }
            }
        }
    })
}