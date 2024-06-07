package com.example.kotlinframe.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.util.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import com.aleyn.mvvm.base.RefreshVMFragment
import com.aleyn.mvvm.base.WebviewDetailActivity
import com.aleyn.mvvm.extend.flowLaunch
import com.example.kotlinframe.databinding.FragmentHomeBinding
import com.example.kotlinframe.network.entity.ProjectTabItem
import com.example.kotlinframe.ui.home.sum.SumFragment
import com.example.kotlinframe.utils.GlideImageLoader
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pcl.mvvm.network.entity.BannerBean
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
class HomeFragment : RefreshVMFragment<HomeViewModel, FragmentHomeBinding>() {

    private var mArrayTabFragments = SparseArray<Fragment>()
    private var mTabLayoutMediator: TabLayoutMediator? = null
    private var mProjectTabs: MutableList<ProjectTabItem> = mutableListOf()
    private lateinit var mFragmentAdapter: ViewPageFragmentAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        with(mBinding.banner) {
            addBannerLifecycleObserver(viewLifecycleOwner)
                .setIndicator(CircleIndicator(requireContext())).setAdapter(GlideImageLoader())
                .setOnBannerListener { data, _ ->
                    data as BannerBean
                    WebviewDetailActivity.start(requireContext(), data.url, data.title)
                }
        }
        mFragmentAdapter =
            ViewPageFragmentAdapter(childFragmentManager, lifecycle, mArrayTabFragments)
        mBinding.viewPager.adapter = mFragmentAdapter
        //可左右滑动
        mBinding.viewPager.isUserInputEnabled = true
        mTabLayoutMediator = TabLayoutMediator(
            mBinding.tabHome,
            mBinding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = mProjectTabs[position].name
        }
        //tabLayout和viewPager2关联起来
        mTabLayoutMediator?.attach()

        mBinding.tabHome.addOnTabSelectedListener(tabSelectedCall)
    }

    override fun loadData() {
        viewModel.getBanner()
        viewModel.getTabData()
    }

    override fun initObserve() {
        super.initObserve()
        flowLaunch {
            viewModel.bannerData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                mBinding.banner.setDatas(it)
            }
        }
        flowLaunch {
            viewModel.tabData.asSharedFlow().flowWithLifecycle(lifecycle).collect {

                if (mArrayTabFragments.isEmpty()) {
                    it.filter { item -> item.name != "完整项目" && item.name != "资源聚合类" }
                        ?.forEachIndexed { index, item ->
                            mProjectTabs.add(item)
                            mArrayTabFragments.append(index, SumFragment.newInstance(item.id))
                        }
                    mFragmentAdapter?.setData(mArrayTabFragments)
                    mFragmentAdapter?.notifyItemRangeChanged(0, mArrayTabFragments.size())

                    // 解决 TabLayout 刷新数据后滚动到错误位置
                    mBinding?.tabHome?.let {
                        it.post { it.getTabAt(0)?.select() }
                    }
                } else {
                    val sumFragment =
                        mArrayTabFragments.get(mBinding.viewPager.currentItem) as SumFragment
                    sumFragment.loadData()
                }
            }
        }
    }

    private val tabSelectedCall = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.customView = TextView(requireContext()).apply {
                text = tab?.text
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
                setTextColor(ContextCompat.getColor(requireContext(), com.aleyn.mvvm.R.color.black))
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tab?.customView = null
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.banner.destroy()
        mTabLayoutMediator?.detach()
    }

}