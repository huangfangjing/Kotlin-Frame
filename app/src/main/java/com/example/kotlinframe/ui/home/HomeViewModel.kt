package com.example.kotlinframe.ui.home

import com.aleyn.mvvm.base.RefreshViewModel
import com.aleyn.mvvm.extend.asResponse
import com.aleyn.mvvm.extend.bindLoading
import com.aleyn.mvvm.extend.getOrThrow
import com.example.kotlinframe.network.entity.ProjectTabItem
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion

/**
 *@author : hfj
 */
class HomeViewModel : RefreshViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    var bannerData = MutableSharedFlow<List<BannerBean>>()

    var tabData = MutableSharedFlow<MutableList<ProjectTabItem>>()


    fun getBanner() {
        launch {
            homeRepository.getBannerData().asResponse().collect(bannerData)
        }
    }

    fun getTabData() {
        launch {
            homeRepository.getTabData().getOrThrow().let {
                tabData.emit(it)
            }
        }
    }
}