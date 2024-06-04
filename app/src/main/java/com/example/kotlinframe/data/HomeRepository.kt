package com.example.kotlinframe.data

import com.aleyn.mvvm.base.BaseModel
import com.aleyn.mvvm.entity.ListResponse
import com.example.kotlinframe.common.BaseResult
import com.example.kotlinframe.network.api.HomeService
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.utils.RetrofitClient
import kotlinx.coroutines.flow.Flow

/**
 *@author : hfj
 */
class HomeRepository : BaseModel() {

    private val mService by lazy { RetrofitClient.getInstance().create(HomeService::class.java) }

    fun getBannerData(): Flow<BaseResult<List<BannerBean>>> = mService.getBanner()

    fun getHomeList(page: Int): Flow<BaseResult<ListResponse<ArticlesBean>>> = mService.getHomeList(page)

    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository().also { INSTANCE = it }
            }
    }
}