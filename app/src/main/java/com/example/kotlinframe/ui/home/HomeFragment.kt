package com.example.kotlinframe.ui.home

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.lifecycle.flowWithLifecycle
import com.aleyn.mvvm.adapter.QuickAdapter
import com.aleyn.mvvm.base.ListRefreshFragment
import com.aleyn.mvvm.extend.flowLaunch
import com.example.kotlinframe.R
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.BannerBean
import com.example.kotlinframe.utils.GlideImageLoader
import com.youth.banner.Banner
import kotlin.system.exitProcess

/**
 *@author : hfj
 */
class HomeFragment : ListRefreshFragment<HomeViewModel, ArticlesBean>() {

    override val showDivder: Boolean
        get() = true

    private lateinit var banner: Banner<BannerBean, GlideImageLoader>
    override fun getAdapter(): QuickAdapter<ArticlesBean> {
        return HomeListAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        banner = Banner<BannerBean, GlideImageLoader>(context).apply {
            minimumWidth = MATCH_PARENT
            layoutParams =
                ViewGroup.LayoutParams(MATCH_PARENT, resources.getDimension(R.dimen.dp_120).toInt())
            setAdapter(GlideImageLoader())
        }
        with(mAdapter) {
            takeIf { !hasHeaderLayout() }?.let { addHeaderView(banner) }
        }
    }

    override fun showDivder(): Boolean {
        return true
    }

    override fun loadData() {
        viewModel.getBanner()
        viewModel.getHomeList(page)
    }

    override fun initObserve() {
        super.initObserve()
        flowLaunch {
            viewModel.banners.flowWithLifecycle(lifecycle).collect {
                banner.setDatas(it)
            }
        }
    }

}