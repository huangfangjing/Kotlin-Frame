package com.example.kotlinframe.network.api

import com.aleyn.mvvm.entity.ListResponse
import com.example.kotlinframe.common.BaseResult
import com.example.kotlinframe.network.entity.ProjectSubInfo
import com.example.kotlinframe.network.entity.ProjectSubList
import com.example.kotlinframe.network.entity.ProjectTabItem
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.network.entity.UsedWeb
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *   @author : Aleyn
 *   time   : 2019/10/29
 */
interface HomeService {

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    fun getBanner(): Flow<BaseResult<List<BannerBean>>>

    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("article/listproject/{page}/json")
    fun getHomeList(
        @Path("page") page: Int,
    ): Flow<BaseResult<ListResponse<ArticlesBean>>>

    /**
     * 导航数据
     */
    @GET("project/tree/json")
    suspend fun naviJson(): BaseResult<List<NavTypeBean>>


    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResult<HomeListBean>


    /**
     * 常用网站
     */
    @GET("friend/json")
    fun getPopularWeb(): Flow<BaseResult<List<UsedWeb>>>

    /**
     * 首页项目
     */
    @GET("/project/tree/json")
    suspend fun getTabData(): BaseResult<MutableList<ProjectTabItem>>

    /**
     * 项目二级列表
     * @param page  分页数量
     * @param cid    项目分类的id
     */
    @GET("/project/list/{page}/json")
    suspend fun getSumtList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResult<ListResponse<ProjectSubInfo>>
}