package com.example.kotlinframe.ui.home

import com.aleyn.mvvm.base.DataViewModel
import com.aleyn.mvvm.extend.asResponse
import com.aleyn.mvvm.extend.bindLoading
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion

/**
 *@author : hfj
 */
class HomeViewModel : DataViewModel<ArticlesBean>() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val _banners = MutableSharedFlow<List<BannerBean>>()
    var banners = _banners.asSharedFlow()



    fun getBanner() {
        launch {
            homeRepository.getBannerData().asResponse().collect(_banners)
        }
    }

    fun getHomeList(page: Int) {
        launch {
            homeRepository.getHomeList(page)
                .bindLoading(this@HomeViewModel)
                .onCompletion { refreshState.emit(Unit) }
                .asResponse()
                .collect(datas)
        }
    }

}