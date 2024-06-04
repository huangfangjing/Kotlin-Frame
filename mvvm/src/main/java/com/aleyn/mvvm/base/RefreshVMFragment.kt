package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.viewbinding.ViewBinding
import com.aleyn.mvvm.R
import com.aleyn.mvvm.databinding.RefreshViewLayoutBinding
import com.aleyn.mvvm.extend.flowLaunch
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *@author : hfj
 */
abstract class RefreshVMFragment<VM : RefreshViewModel, VB : ViewBinding> :
    BaseVMFragment<VM, VB>() {

    private lateinit var smartRefresh: SmartRefreshLayout

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        super.initBinding(inflater, container)
        return inflater.inflate(R.layout.refresh_view_layout, mBinding.root as ViewGroup, true)
            .apply {
                smartRefresh = findViewById(R.id.smart_refresh)
            }
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(smartRefresh) {
            setOnRefreshListener {
                loadData()
            }
        }
    }

    override fun initObserve() {
        flowLaunch {
            viewModel.refreshState.flowWithLifecycle(lifecycle).collect {
                smartRefresh.takeIf { it.isRefreshing }.let { it?.finishRefresh() }
            }
        }
    }
}