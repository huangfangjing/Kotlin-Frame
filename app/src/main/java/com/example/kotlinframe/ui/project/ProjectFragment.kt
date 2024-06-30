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
        viewModel.getLineData()
        viewModel.getRadarData()
        viewModel.getHorData()
    }

    override fun initObserve() {
        super.initObserve()
        flowLaunch {
            viewModel.mBarData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                with(mBinding.barView) {
                    mBinding.barView.buildDate(
                        it, null, context.getString(R.string.person_sum),
                        context.getString(R.string.score)
                    )
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

        flowLaunch {
            viewModel.mRadarData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                mBinding.aiRadarView.buildData(it) { content, _ ->
                    showToast(content)
                }
            }
        }
        flowLaunch {
            viewModel.mHorData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                mBinding.horView.buildDate(it) { twoLevelData ->
                    showToast(twoLevelData.indexName)
                }
            }
        }
    }

}