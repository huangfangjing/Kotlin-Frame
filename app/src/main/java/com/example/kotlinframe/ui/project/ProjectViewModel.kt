package com.example.kotlinframe.ui.project

import com.aleyn.mvvm.base.RefreshViewModel
import com.example.chartlibraby.bean.TwoLevelData
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 *@author : hfj
 */
class ProjectViewModel : RefreshViewModel() {

    private val projectRepository by lazy { InjectorUtil.getProjectRepository() }

    val mBarData = MutableSharedFlow<List<TwoLevelData>>()
    val mLineData = MutableSharedFlow<List<TwoLevelData>>()

    fun getBarData() {
        launch {
            var list: List<TwoLevelData> = projectRepository.getBarData()
            mBarData.emit(list)
            refreshState.emit(Unit)
        }

    }

    fun getLineData() {
        launch {
            var list: List<TwoLevelData> = projectRepository.getLineData()
            mLineData.emit(list)
        }
    }
}