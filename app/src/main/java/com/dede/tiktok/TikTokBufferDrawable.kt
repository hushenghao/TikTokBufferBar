package com.dede.tiktok

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import kotlin.math.max

class TikTokBufferDrawable : Drawable(), Animatable,
    ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    companion object {
        private const val DEFAULT_BAR_HEIGHT = 5
        private const val DEFAULT_BAR_MIN_WIDTH = 200
        private const val DEFAULT_ANIM_DURATION = 500L
        private const val DEFAULT_ANIM_DELAY = 100L
    }

    private var barColor = Color.WHITE
    private var barEndColor: Int
    private var min = DEFAULT_BAR_MIN_WIDTH
    private val paintEndColor: Int
    private val a = 10
    private var animDelay = DEFAULT_ANIM_DELAY

    private val argbEvaluator = ArgbEvaluator()
    private val anim: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        anim.duration = DEFAULT_ANIM_DURATION
        anim.interpolator = LinearInterpolator()
        anim.repeatMode = ValueAnimator.RESTART
        anim.addUpdateListener(this)
        anim.addListener(this)

        paintEndColor = getAlphaColor(0)
        barEndColor = getAlphaColor(80)
    }

    private fun getAlphaColor(@IntRange(from = 0, to = 255) alpha: Int): Int {
        return Color.argb(alpha, Color.red(barColor), Color.green(barColor), Color.blue(barColor))
    }

    private fun getBarHeight(): Int {
        return max(bounds.height(), DEFAULT_BAR_HEIGHT)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        paint.strokeWidth = getBarHeight().toFloat()
    }

    override fun getIntrinsicWidth(): Int {
        return bounds.width()
    }

    override fun getIntrinsicHeight(): Int {
        return getBarHeight()
    }

    fun setStartDelay(@IntRange(from = 0) delay: Long) {
        animDelay = delay
    }

    fun setDuration(@IntRange(from = 0) duration: Long) {
        anim.duration = duration
    }

    fun setColor(@ColorInt color: Int) {
        barColor = color
    }

    override fun draw(canvas: Canvas) {
        paint.color = argbEvaluator.evaluate(p, barColor, paintEndColor) as Int

        val width = bounds.width()
        val m = min / 2f
        val w = width / 2f
        val t = w - m
        val c = m + t * p

        val endColor = barEndColor

        var gradient = LinearGradient(
            w - c,
            0f,
            w - c + a,
            0f,
            endColor,
            barColor,
            Shader.TileMode.CLAMP
        )

        paint.shader = gradient
        canvas.drawLine(w - c, 0f, w, 0f, paint)// 左半边

        gradient = LinearGradient(
            w + c - a,
            0f,
            w + c,
            0f,
            barColor,
            endColor,
            Shader.TileMode.CLAMP
        )

        paint.shader = gradient
        canvas.drawLine(w, 0f, w + c, 0f, paint)// 右半边
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun unscheduleSelf(what: Runnable) {
        stop()
        super.unscheduleSelf(what)
    }

    override fun scheduleSelf(what: Runnable, `when`: Long) {
        super.scheduleSelf(what, `when`)
        start()
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        if (visible) {
            start()
        } else {
            stop()
        }
        return super.setVisible(visible, restart)
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun isRunning(): Boolean {
        return anim.isRunning
    }

    override fun start() {
        anim.startDelay = 0
        anim.start()
    }

    override fun stop() {
        anim.cancel()
    }

    private var p = 0f

    override fun onAnimationUpdate(animation: ValueAnimator) {
        p = animation.animatedValue as Float
        invalidateSelf()
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        anim.startDelay = animDelay
        anim.start()
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }

}