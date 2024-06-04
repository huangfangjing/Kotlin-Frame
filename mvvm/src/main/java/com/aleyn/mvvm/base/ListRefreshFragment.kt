package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleyn.mvvm.adapter.QuickAdapter
import com.aleyn.mvvm.databinding.BaseRecycleviewBinding
import com.aleyn.mvvm.extend.flowLaunch

/**
 *@author : hfj
 */
abstract class ListRefreshFragment<VM : ListViewModel<T>, T> :
    BaseVMFragment<VM, BaseRecycleviewBinding>() {

    var page: Int = 0
    val mAdapter by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { getAdapter() }

    abstract fun getAdapter(): QuickAdapter<T>

    override fun initView(savedInstanceState: Bundle?) {
        with(mAdapter) {
            loadMoreModule?.setOnLoadMoreListener(::loadMore)
        }
        with(mBinding.recycleView) {
            layoutManager = applyLayoutManager()
            adapter = mAdapter
        }
        with(mBinding.smartRefresh) {
            setOnRefreshListener {
                dropDownRefresh()
            }
        }
    }

    open fun applyLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun initObserve() {
        flowLaunch {
            viewModel.refreshState.flowWithLifecycle(lifecycle).collect {
                mBinding.smartRefresh.takeIf { it.isRefreshing }.let { it?.finishRefresh() }
            }
        }

        flowLaunch {
            viewModel.datas.flowWithLifecycle(lifecycle).collect {
                if (it.curPage == 1) mAdapter.setList(it.datas)
                else mAdapter.addData(it.datas)
                if (it.curPage == it.pageCount) mAdapter.loadMoreModule.loadMoreEnd()
                else mAdapter.loadMoreModule.loadMoreComplete()
                page = it.curPage
            }
        }
    }

    private fun dropDownRefresh() {
        page = 0
        loadData()
    }

    private fun loadMore() {
        page += 1
        loadData()
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        val cls = BaseRecycleviewBinding::class.java
        return cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
            _binding = it.invoke(null, inflater) as BaseRecycleviewBinding
            mBinding.root
        }
    }

}