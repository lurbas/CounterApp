package com.lucasurbas.counter.ui.explore

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.lucasurbas.counter.R
import com.lucasurbas.counter.app.di.FragmentModule
import com.lucasurbas.counter.app.di.helper.InjectHelper
import com.lucasurbas.counter.ui.BaseFragment
import com.lucasurbas.counter.ui.explore.di.ExploreFragmentModule
import com.lucasurbas.counter.ui.main.MainNavigator
import com.lucasurbas.counter.ui.main.di.MainActivityComponent
import com.lucasurbas.counter.utils.SpaceItemDecoration
import javax.inject.Inject

class ExploreFragment : BaseFragment(), ExplorePresenter.View {

    companion object {

        val TAG = ExploreFragment::class.java.simpleName
        private val COLUMNS_COUNT = 3

        fun newInstance(): Fragment {
            return ExploreFragment()
        }
    }

    @BindView(R.id.explore_recycler_view)
    lateinit var exploreRecyclerView: RecyclerView
    @BindView(R.id.error_layout)
    lateinit var errorLayout: View

    @Inject
    lateinit var presenter: ExplorePresenter
    @Inject
    lateinit var navigator: MainNavigator

    lateinit var exploreAdapter: ExploreAdapter
    lateinit var unbinder: Unbinder

    private fun injectFragment() {
        InjectHelper.getComponent(MainActivityComponent::class.java, requireActivity())
                .plus(FragmentModule(this), ExploreFragmentModule())
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectFragment()
        unbinder = ButterKnife.bind(this, view)
        setupToolbar()
        setupView()

        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        presenter.detachView()
    }

    private fun setupToolbar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.explore_title)
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupView() {
        exploreAdapter = ExploreAdapter { id, view -> navigateToCounterDetailScreen(id, view) }
        exploreAdapter.setHasStableIds(true)
        val gridLayoutManager = GridLayoutManager(activity, COLUMNS_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return exploreAdapter.getSpanSize(position, gridLayoutManager.spanCount)
            }
        }
        exploreRecyclerView.layoutManager = gridLayoutManager
        exploreRecyclerView.adapter = exploreAdapter
        val tileMargin = resources.getDimensionPixelOffset(R.dimen.tile_margin)
        exploreRecyclerView.addItemDecoration(SpaceItemDecoration(tileMargin))
    }

    private fun navigateToCounterDetailScreen(counterId: Int, sharedView: View) {
        navigator.navigateToDetailScreen(counterId, sharedView, this)
    }

    override fun render(exploreState: UiExploreState) {

        exploreAdapter.updateItemList(exploreState.itemList)

        errorLayout.visibility = if (exploreState.itemList.isEmpty() && exploreState.error != null)
            VISIBLE else GONE

        if (exploreState.error != null) {
            Snackbar.make(view!!, R.string.error_general, Snackbar.LENGTH_LONG).show()
        }
    }
}
