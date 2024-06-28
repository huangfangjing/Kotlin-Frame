package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.aleyn.mvvm.adapter.QuickAdapter
import com.aleyn.mvvm.databinding.BaseRecycleviewBinding
import com.aleyn.mvvm.ext.addDivider
import com.aleyn.mvvm.extend.flowLaunch
import com.aleyn.mvvm.widget.RecyclerViewDivider
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
abstract class ListRefreshActivity<VM : DataViewModel<T>, T> :
    BaseVMActivity<VM, BaseRecycleviewBinding>() {

    open val showDivder = false //分割线
    var page: Int = 0
    val mAdapter by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { getAdapter() }

    abstract fun getAdapter(): QuickAdapter<T>

    override fun initView(savedInstanceState: Bundle?) {
        with(mAdapter) {
            loadMoreModule.setOnLoadMoreListener(::loadMore)
        }
        with(mBinding.mutipleView) {
            recyclerView?.adapter = mAdapter
            recyclerView?.layoutManager = applyLayoutManager()
            if (showDivder) recyclerView?.addDivider(getItemDecoration())
        }
        with(mBinding.smartRefresh) {
            setOnRefreshListener {
                dropDownRefresh()
            }
        }
    }

    override fun initObserve() {
        flowLaunch {
            viewModel.refreshState.flowWithLifecycle(lifecycle).collect {
                mBinding.smartRefresh.takeIf { it.isRefreshing }.let { it?.finishRefresh() }
            }
        }

        flowLaunch {
            viewModel.datas.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                if (it.curPage == 1) mAdapter.setList(it.datas)
                else mAdapter.addData(it.datas)
                if (it.curPage == it.pageCount) mAdapter.loadMoreModule.loadMoreEnd()
                else mAdapter.loadMoreModule.loadMoreComplete()
                page = it.curPage
            }
        }
    }

    open fun getItemDecoration(): ItemDecoration {
        return RecyclerViewDivider(this)
    }

    open fun applyLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    private fun dropDownRefresh() {
        page = 0
        loadData()
    }

    private fun loadMore() {
        page += 1
        loadData()
    }

    override fun initBinding() {
        val cls = BaseRecycleviewBinding::class.java
        cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
            mBinding = it.invoke(null, layoutInflater) as BaseRecycleviewBinding
        }
    }

    override fun showEmptyView() {
        mBinding.mutipleView.showEmptyView()
    }

    override fun showContentView() {
        mBinding.mutipleView.showContentView()
    }

    override fun showNetWorkError() {
        mBinding.mutipleView.showNetWorkError()
    }

    override fun showSystemError() {
        mBinding.mutipleView.showSystemError()
    }
}