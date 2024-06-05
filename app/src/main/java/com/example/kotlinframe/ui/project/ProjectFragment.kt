package com.example.kotlinframe.ui.project

import androidx.lifecycle.flowWithLifecycle
import com.aleyn.mvvm.base.BaseVMFragment
import com.aleyn.mvvm.extend.flowLaunch
import com.example.kotlinframe.databinding.FragmentProjectBinding
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
class ProjectFragment : BaseVMFragment<ProjectViewModel, FragmentProjectBinding>() {

    override fun loadData() {
        viewModel.getBarData()
        viewModel.getLineData()
    }

    override fun initObserve() {
        flowLaunch {
            viewModel.mBarData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                with(mBinding.barView) {
                    mBinding.barView.buildDate(it, null, "人数", "分数")
                    hideIndicate()
                }
            }
        }
        flowLaunch {
            viewModel.mLineData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                mBinding.lineView.buildDate(it) { twoLevelData ->
                    showToast(twoLevelData.indexName)
                }
            }
        }
    }

}