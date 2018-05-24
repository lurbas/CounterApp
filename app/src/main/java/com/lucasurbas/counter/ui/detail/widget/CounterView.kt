package com.lucasurbas.counter.ui.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lucasurbas.counter.R
import com.lucasurbas.counter.app.di.helper.InjectHelper
import com.lucasurbas.counter.ui.detail.widget.di.CounterViewModule
import com.lucasurbas.counter.ui.main.di.MainActivityComponent
import java.util.*
import javax.inject.Inject

class CounterView : FrameLayout, CounterViewPresenter.View {

    companion object {
        private val DEFAULT = -1F
    }

    private var primaryTextSize: Float = 0F
    private var secondaryTextSize: Float = 0F

    @BindView(R.id.minute_left_digit)
    lateinit var minuteLeftDigit: TextView
    @BindView(R.id.minute_right_digit)
    lateinit var minuteRightDigit: TextView
    @BindView(R.id.minutes_divider)
    lateinit var minutesDivider: TextView
    @BindView(R.id.second_left_digit)
    lateinit var secondLeftDigit: TextView
    @BindView(R.id.second_right_digit)
    lateinit var secondRightDigit: TextView
    @BindView(R.id.seconds_divider)
    lateinit var secondsDivider: TextView
    @BindView(R.id.millis_digits)
    lateinit var millisDigits: TextView

    @Inject
    lateinit var presenter: CounterViewPresenter

    private fun injectView() {
        InjectHelper.getComponent(MainActivityComponent::class.java, context)
                .plus(CounterViewModule())
                .inject(this)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.view_counter, this)
        injectView()
        ButterKnife.bind(this)

        if (attrs != null) {
            applyAttrs(attrs)
        }
        setupView()
    }

    private fun applyAttrs(attrs: AttributeSet) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.CounterView)

        try {
            primaryTextSize = a.getDimension(R.styleable.CounterView_cv_primaryTextSize, DEFAULT)
            secondaryTextSize = a.getDimension(R.styleable.CounterView_cv_secondaryTextSize, DEFAULT)
        } finally {
            a.recycle()
        }
    }

    private fun setupView() {
        if (primaryTextSize != DEFAULT) {
            minuteLeftDigit.textSize = primaryTextSize
            minuteRightDigit.textSize = primaryTextSize
            minutesDivider.textSize = primaryTextSize
            secondLeftDigit.textSize = primaryTextSize
            secondRightDigit.textSize = primaryTextSize
        }
        if (secondaryTextSize != DEFAULT) {
            secondsDivider.textSize = secondaryTextSize
            millisDigits.textSize = secondaryTextSize
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.destroy()
    }

    override fun render(counterViewState: UiCounterViewState) {
        counterViewState.value?.let {
            minuteLeftDigit.text = counterViewState.minuteLeft.toString()
            minuteRightDigit.text = counterViewState.minuteRight.toString()
            secondLeftDigit.text = counterViewState.secondLeft.toString()
            secondRightDigit.text = counterViewState.secondRight.toString()
            millisDigits.text = String.format(Locale.ROOT, "%02d", counterViewState.millis)
        }
    }

    fun setCounterValueInMillis(value: Long) {
        presenter.newValue(value)
    }
}
