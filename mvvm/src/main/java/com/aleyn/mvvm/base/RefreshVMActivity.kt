package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.viewbinding.ViewBinding
import com.aleyn.mvvm.R
import com.aleyn.mvvm.extend.flowLaunch
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
abstract class RefreshVMActivity<VM : RefreshViewModel, VB : ViewBinding> : BaseVMActivity<VM, VB>() {

    private lateinit var smartRefresh: SmartRefreshLayout

    override fun initBinding() {
        super.initBinding()
        smartRefresh = layoutInflater.inflate(R.layout.refresh_view_layout, null) as SmartRefreshLayout
        smartRefresh.takeIf { it.childCount == 0 }.let {
            it?.addView(
                baseViewBinding.root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
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
            viewModel.refreshState.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                smartRefresh.takeIf { it.isRefreshing }.let { it?.finishRefresh() }
            }
        }
    }

}