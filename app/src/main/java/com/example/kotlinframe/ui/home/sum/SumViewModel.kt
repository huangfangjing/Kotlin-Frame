package com.example.kotlinframe.ui.home.sum

import com.aleyn.mvvm.base.DataViewModel
import com.aleyn.mvvm.extend.asResponse
import com.aleyn.mvvm.extend.asSuccess
import com.aleyn.mvvm.extend.bindLoading
import com.aleyn.mvvm.extend.getOrThrow
import com.example.kotlinframe.network.entity.ProjectSubInfo
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.flow.onCompletion

/**
 *@author : hfj
 */
class SumViewModel : DataViewModel<ProjectSubInfo>() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun getSumData(page: Int, id: Int) {
        launch {
            homeRepository.getSumData(page, id).asResponse()
                .bindLoading(this@SumViewModel)
                .collect(datas)
        }
    }
}