package com.example.kotlinframe.ui.project

import androidx.lifecycle.flowWithLifecycle
import com.aleyn.mvvm.base.BaseVMFragment
import com.aleyn.mvvm.base.RefreshVMFragment
import com.aleyn.mvvm.extend.flowLaunch
import com.example.kotlinframe.R
import com.example.kotlinframe.databinding.FragmentProjectBinding
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
class ProjectFragment : RefreshVMFragment<ProjectViewModel, FragmentProjectBinding>() {

    override fun loadData() {
        viewModel.getBarData()
    }

    override fun initObserve() {
        flowLaunch {
            viewModel.mHorData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                mBinding.horView.buildDate(it) { twoLevelData ->
                    showToast(twoLevelData.indexName)
                }
            }
        }
    }
}