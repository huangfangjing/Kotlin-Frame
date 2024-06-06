package com.example.kotlinframe.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.util.SparseArray
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import com.aleyn.mvvm.base.RefreshVMFragment
import com.aleyn.mvvm.extend.flowLaunch
import com.example.kotlinframe.databinding.FragmentHomeBinding
import com.example.kotlinframe.network.entity.ProjectTabItem
import com.example.kotlinframe.ui.home.sum.SumFragment
import com.example.kotlinframe.utils.GlideImageLoader
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

        mBinding.banner.setAdapter(GlideImageLoader())
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
                it.filter { item -> item.name != "完整项目" }.forEachIndexed { index, item ->
                    mProjectTabs.add(item)
                    mArrayTabFragments.append(index, SumFragment.newInstance(item.id))
                }
                mFragmentAdapter?.setData(mArrayTabFragments)
                mFragmentAdapter?.notifyItemRangeChanged(0, mArrayTabFragments.size())

                // 解决 TabLayout 刷新数据后滚动到错误位置
                mBinding?.tabHome?.let {
                    it.post { it.getTabAt(0)?.select() }
                }
            }
        }
    }

    private val tabSelectedCall = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            setTabTextSize(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            //非选中效果在xml中设置
            tab?.customView = null
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }

    private fun setTabTextSize(tabFirst: TabLayout.Tab?) {
        TextView(requireContext()).apply {
            typeface = Typeface.DEFAULT_BOLD
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            setTextColor(ContextCompat.getColor(requireContext(), com.aleyn.mvvm.R.color.black))
        }.also {
            it.text = tabFirst?.text
            tabFirst?.customView = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTabLayoutMediator?.detach()
    }

}