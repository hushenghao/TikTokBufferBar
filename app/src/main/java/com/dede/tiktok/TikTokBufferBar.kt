package com.dede.tiktok

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.ViewCompat
import kotlin.math.min

/**
 * Created by hsh on 2020/4/16 5:04 PM
 * 抖音缓冲进度条
 */
class TikTokBufferBar : View, ValueAnimator.AnimatorUpdateListener, Runnable,
    Animator.AnimatorListener {

    private var barHeight = 5
    private var barColor = Color.WHITE
    private var barEndColor = 0x33FFFFFF
    private var min = 200
    private val paintEndColor: Int
    private val a = 10
    private var animDelay = 100L

    private val argbEvaluator = ArgbEvaluator()
    private val anim: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        anim.duration = 500
        anim.interpolator = LinearInterpolator()
        anim.repeatMode = ValueAnimator.RESTART
        anim.addUpdateListener(this)
        anim.addListener(this)

        val arrays = context?.obtainStyledAttributes(attrs, R.styleable.TikTokBufferBar)
        if (arrays != null) {
            barColor = arrays.getColor(R.styleable.TikTokBufferBar_bar_color, barColor)
            Log.i("TikTokBufferBar", ": " + barColor)
            barHeight =
                arrays.getDimensionPixelSize(R.styleable.TikTokBufferBar_bar_height, barHeight)
            barEndColor = arrays.getColor(R.styleable.TikTokBufferBar_bar_end_color, barEndColor)
            anim.duration =
                arrays.getInteger(R.styleable.TikTokBufferBar_anim_duration, 500).toLong()
            animDelay = arrays.getInteger(R.styleable.TikTokBufferBar_anim_delay, 100).toLong()

            arrays.recycle()
        }
        paintEndColor = Color.argb(
            0,
            Color.red(barColor),
            Color.green(barColor),
            Color.blue(barColor)
        )

        paint.strokeWidth = barHeight.toFloat()

//        post(this)// 开始动画哦
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = min(MeasureSpec.getSize(heightMeasureSpec), barHeight)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private var p = 0f

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        paint.color = argbEvaluator.evaluate(p, barColor, paintEndColor) as Int

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

    override fun run() {
        anim.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        p = animation.animatedValue as Float
        invalidate()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            anim.start()
        } else {
            anim.cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        anim.start()
    }

    override fun onDetachedFromWindow() {
        anim.cancel()
        super.onDetachedFromWindow()
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (ViewCompat.isAttachedToWindow(this) && visibility == VISIBLE) {
            handler.postDelayed(this, animDelay)
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }


}