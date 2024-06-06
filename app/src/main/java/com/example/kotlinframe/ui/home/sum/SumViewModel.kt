package com.example.kotlinframe.ui.home.sum

import com.aleyn.mvvm.base.DataViewModel
import com.aleyn.mvvm.extend.getOrThrow
import com.example.kotlinframe.network.entity.ProjectSubInfo
import com.pcl.mvvm.utils.InjectorUtil

/**
 *@author : hfj
 */
class SumViewModel : DataViewModel<ProjectSubInfo>() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun getSumData(page: Int, id: Int) {
        launch {
            homeRepository.getSumData(page,id).getOrThrow().let {
                datas.emit(it)
            }
        }
    }
}