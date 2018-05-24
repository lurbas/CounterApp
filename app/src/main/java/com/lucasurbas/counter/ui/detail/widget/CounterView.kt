package com.lucasurbas.counter.ui.detail.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
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
    private var counterViewState: UiCounterViewState? = null;

    @BindView(R.id.minute_left_digit)
    lateinit var minuteLeftDigit: TextView
    @BindView(R.id.minute_left_next_digit)
    lateinit var minuteLeftNextDigit: TextView
    @BindView(R.id.minute_right_digit)
    lateinit var minuteRightDigit: TextView
    @BindView(R.id.minute_right_next_digit)
    lateinit var minuteRightNextDigit: TextView
    @BindView(R.id.minutes_divider)
    lateinit var minutesDivider: TextView
    @BindView(R.id.second_left_digit)
    lateinit var secondLeftDigit: TextView
    @BindView(R.id.second_left_next_digit)
    lateinit var secondLeftNextDigit: TextView
    @BindView(R.id.second_right_digit)
    lateinit var secondRightDigit: TextView
    @BindView(R.id.second_right_next_digit)
    lateinit var secondRightNextDigit: TextView
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
            minuteLeftNextDigit.textSize = primaryTextSize
            minuteRightDigit.textSize = primaryTextSize
            minuteRightNextDigit.textSize = primaryTextSize
            minutesDivider.textSize = primaryTextSize
            secondLeftDigit.textSize = primaryTextSize
            secondLeftNextDigit.textSize = primaryTextSize
            secondRightDigit.textSize = primaryTextSize
            secondRightNextDigit.textSize = primaryTextSize
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
            if (counterViewState.minuteLeftAnimate) {
                animateDigit(minuteLeftDigit, minuteLeftNextDigit, counterViewState.minuteLeft.toString())
            } else if (minuteLeftDigit.tag == null) {
                minuteLeftDigit.text = counterViewState.minuteLeft.toString()
            }
            if (counterViewState.minuteRightAnimate) {
                animateDigit(minuteRightDigit, minuteRightNextDigit, counterViewState.minuteRight.toString())
            } else if (minuteRightDigit.tag == null) {
                minuteRightDigit.text = counterViewState.minuteRight.toString()
            }
            if (counterViewState.secondLeftAnimate) {
                animateDigit(secondLeftDigit, secondLeftNextDigit, counterViewState.secondLeft.toString())
            } else if (secondLeftDigit.tag == null) {
                secondLeftDigit.text = counterViewState.secondLeft.toString()
            }
            if (counterViewState.secondRightAnimate) {
                animateDigit(secondRightDigit, secondRightNextDigit, counterViewState.secondRight.toString())
            } else if (secondRightDigit.tag == null) {
                secondRightDigit.text = counterViewState.secondRight.toString()
            }
            millisDigits.text = String.format(Locale.ROOT, "%02d", counterViewState.millis)

            this.counterViewState = counterViewState
        }
    }

    fun animateDigit(digitView: TextView, digitNextView: TextView, stringValue: String) {
        digitNextView.text = stringValue
        digitNextView.animate()
                .translationY(digitNextView.height.toFloat())
                .alpha(1F)
                .setDuration(300)
                .withEndAction { resetNextView(digitNextView) }
                .start()

        digitView.tag = "Freeze"
        digitView.animate()
                .translationY(digitView.height.toFloat())
                .alpha(0F)
                .setDuration(300)
                .withEndAction { resetView(digitView, stringValue) }
                .start()
    }

    private fun resetView(textView: TextView, stringValue: String) {
        textView.text = stringValue
        textView.translationY = 0F
        textView.alpha = 1F;
        textView.tag = null
    }

    private fun resetNextView(textView: TextView) {
        textView.translationY = 0F
        textView.alpha = 0F;
    }

    fun setCounterValueInMillis(value: Long) {
        presenter.newValue(value)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.counterViewState = counterViewState
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            state.counterViewState?.let { render(it) }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {

        var counterViewState: UiCounterViewState? = null

        constructor(source: Parcel) : super(source) {
            counterViewState = source.readParcelable(UiCounterViewState::class.java.classLoader)
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(counterViewState, flags)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
