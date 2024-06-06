package com.example.kotlinframe.ui.home.sum

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aleyn.mvvm.adapter.QuickAdapter
import com.aleyn.mvvm.base.ListRefreshFragment
import com.aleyn.mvvm.widget.StaggeredItemDecoration
import com.blankj.utilcode.util.SizeUtils
import com.example.kotlinframe.network.entity.ProjectSubInfo

/**
 *@author : hfj
 */
class SumFragment : ListRefreshFragment<SumViewModel, ProjectSubInfo>() {

    private var mId: Int = 0
    override val showDivder: Boolean
        get() = true

    companion object {
        fun newInstance(id: Int): SumFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = SumFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mId = arguments?.getInt("id", 0)!!
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return StaggeredItemDecoration(SizeUtils.dp2px(10F))
    }

    override fun loadData() {
        viewModel.getSumData(page, mId)
    }

    override fun getAdapter(): QuickAdapter<ProjectSubInfo> {
        return HomeSumAdapter()
    }

    override fun applyLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

}