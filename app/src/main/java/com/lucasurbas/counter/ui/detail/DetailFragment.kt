package com.lucasurbas.counter.ui.detail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import butterknife.*
import com.lucasurbas.counter.R
import com.lucasurbas.counter.app.di.FragmentModule
import com.lucasurbas.counter.app.di.helper.InjectHelper
import com.lucasurbas.counter.service.RunningCounterService
import com.lucasurbas.counter.ui.BaseFragment
import com.lucasurbas.counter.ui.detail.di.DetailFragmentModule
import com.lucasurbas.counter.ui.detail.widget.CounterView
import com.lucasurbas.counter.ui.main.di.MainActivityComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailFragment : BaseFragment(), DetailPresenter.View {

    companion object {

        val TAG = DetailFragment::class.java.simpleName
        private val COUNTER_ID_KEY = "counter_id_key"

        fun newInstance(counterId: Int): Fragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putInt(COUNTER_ID_KEY, counterId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @BindView(R.id.counter_view)
    lateinit var counterView: CounterView
    @BindView(R.id.start_button)
    lateinit var startButton: Button
    @BindView(R.id.stop_button)
    lateinit var stopButton: Button
    @BindViews(R.id.preset_1_button, R.id.preset_2_button, R.id.preset_3_button, R.id.preset_4_button)
    lateinit var presetViews: Array<View>

    @Inject
    lateinit var presenter: DetailPresenter

    lateinit var unbinder: Unbinder

    private fun injectFragment() {
        InjectHelper.getComponent(MainActivityComponent::class.java, requireActivity())
                .plus(FragmentModule(this), DetailFragmentModule())
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectFragment()
        unbinder = ButterKnife.bind(this, view)
        postponeEnterTransition()
        setupToolbar()

        presenter.attachView(this)
        presenter.getCounter(arguments!!.getInt(COUNTER_ID_KEY))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        presenter.detachView()
    }

    private fun setupToolbar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.detail_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun render(detailState: UiDetailState) {
        val counter = detailState.counter
        counter?.let {
            ButterKnife.apply(presetViews, { view: View, _: Int -> view.setEnabled(!it.isRunning) })
            startButton.visibility = if (it.isRunning) GONE else VISIBLE
            stopButton.visibility = if (it.isRunning) VISIBLE else GONE
            counterView.setCounterValueInMillis(it.valueInMillis)
            if (counterView.background == null) {
                counterView.setBackgroundColor(Color.parseColor(it.color))
            }
            ViewCompat.setTransitionName(counterView, "backgroundImage${counter.id}");
            startPostponedEnterTransition()
        }

        detailState.error?.let {
            Snackbar.make(view!!, R.string.error_general, Snackbar.LENGTH_LONG).show()
        }
    }

    @OnClick(R.id.start_button)
    internal fun onStartClick() {
        val intent = RunningCounterService.newInstance(activity as Context,
                RunningCounterService.ACTION_START, arguments!!.getInt(COUNTER_ID_KEY))
        activity?.startService(intent)
    }

    @OnClick(R.id.stop_button)
    internal fun onStopClick() {
        val intent = RunningCounterService.newInstance(activity as Context,
                RunningCounterService.ACTION_STOP, arguments!!.getInt(COUNTER_ID_KEY))
        activity?.startService(intent)
    }

    @OnClick(R.id.preset_1_button)
    internal fun onPreset1Click() {
        presenter.updateInitialValue(arguments!!.getInt(COUNTER_ID_KEY),
                TimeUnit.SECONDS.toMillis(30))
    }

    @OnClick(R.id.preset_2_button)
    internal fun onPreset2Click() {
        presenter.updateInitialValue(arguments!!.getInt(COUNTER_ID_KEY),
                TimeUnit.MINUTES.toMillis(2))
    }

    @OnClick(R.id.preset_3_button)
    internal fun onPreset3Click() {
        presenter.updateInitialValue(arguments!!.getInt(COUNTER_ID_KEY),
                TimeUnit.MINUTES.toMillis(10))
    }

    @OnClick(R.id.preset_4_button)
    internal fun onPreset4Click() {
        presenter.updateInitialValue(arguments!!.getInt(COUNTER_ID_KEY),
                TimeUnit.MINUTES.toMillis(30))
    }
}
