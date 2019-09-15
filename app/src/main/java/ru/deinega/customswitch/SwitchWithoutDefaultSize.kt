package ru.deinega.customswitch

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.pow
import kotlin.math.roundToInt


open class SwitchWithoutDefaultSize @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val paint: Paint
    private lateinit var rect: RectF
    private var mWidth = 0
    private var mHeight = 0
    private var radius: Float = 0f
    private var padding: Float = 0f
    private var corners: Float = 0f
    private var xCircle: Float = 0f
    private var paddingClickableArea: Int = 0
    private var colorOff: Int = 0
    private var colorOn: Int = 0
    private var currentColor: Int = 0
    private var isChecked = false
    private var positionXStartCircle: Float = 0f
    private var positionXEndCircle: Float = 0f
    private var positionYCircle: Float = 0f
    private var listener: OnSwitchChangedListener? = null
    private lateinit var animator: ValueAnimator

    init {
        initAttrs(context, attrs, defStyleAttr)
        paint = Paint()
        paint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) + paddingClickableArea * 2
        val heightSize = (MeasureSpec.getSize(widthMeasureSpec) * 0.6f + paddingClickableArea * 2).toInt()
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        calculatePositions()
        initAnimation()
        updateSwitch()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(rect, corners, corners, paint)
        paint.color = Color.WHITE
        canvas.drawCircle(xCircle, positionYCircle, radius, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        isChecked = isChecked.not()
        updateSwitch()
        listener?.onSwitchChanged(isChecked)
        return super.onTouchEvent(event)
    }

    fun setOnSwitchChangeListener(listener: OnSwitchChangedListener) {
        this.listener = listener
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwitchWithoutDefaultSize, defStyleAttr, 0)
        colorOff =
            a.getColor(R.styleable.SwitchWithoutDefaultSize_color_off, context.getColorFromRes(R.color.switch_off))
        colorOn =
            a.getColor(R.styleable.SwitchWithoutDefaultSize_color_on, context.getColorFromRes(R.color.switch_on))
        isChecked =
            a.getBoolean(R.styleable.SwitchWithoutDefaultSize_checked, false)
        paddingClickableArea =
            (a.getInt(
                R.styleable.SwitchWithoutDefaultSize_padding_for_clickable,
                10
            ) * resources.displayMetrics.density).roundToInt()
        a.recycle()
    }

    private fun calculatePositions() {
        rect = RectF(
            0f + paddingClickableArea,
            0f + paddingClickableArea,
            mWidth.toFloat() - paddingClickableArea,
            mHeight.toFloat() - paddingClickableArea
        )
        radius = (mWidth - paddingClickableArea * 2) / 4f
        padding = (mWidth - paddingClickableArea * 2) / 20f
        corners = resources.displayMetrics.density.toDouble().pow(2.0).toFloat() * mWidth * 0.03f
        positionXStartCircle = radius + padding + paddingClickableArea
        positionXEndCircle = mWidth - positionXStartCircle
        positionYCircle = mHeight - positionXStartCircle
    }

    private fun initAnimation() {
        animator = ValueAnimator()
        animator.duration = 200
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            refresh(it)
        }
    }

    private fun updateSwitch() {
        if (isChecked) {
            startCheckedAnimation()
        } else {
            startUncheckedAnimation()
        }
    }

    private fun startCheckedAnimation() {
        currentColor = colorOn
        animator.setFloatValues(positionXStartCircle, positionXEndCircle)
        animator.start()
    }

    private fun startUncheckedAnimation() {
        currentColor = colorOff
        animator.setFloatValues(positionXEndCircle, positionXStartCircle)
        animator.start()
    }

    private fun refresh(valueAnimator: ValueAnimator) {
        val position = valueAnimator.animatedValue as Float
        xCircle = position
        paint.color = currentColor
        invalidate()
    }

}