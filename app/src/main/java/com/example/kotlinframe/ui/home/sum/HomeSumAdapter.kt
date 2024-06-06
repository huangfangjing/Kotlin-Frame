package com.example.kotlinframe.ui.home.sum

import android.widget.ImageView
import coil.load
import com.aleyn.mvvm.adapter.QuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kotlinframe.R
import com.example.kotlinframe.network.entity.ProjectSubInfo

/**
 *@author : hfj
 */
class HomeSumAdapter : QuickAdapter<ProjectSubInfo>(R.layout.layout_home_tab_item),LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: ProjectSubInfo) {
        with(holder) {
            setText(R.id.tv_title, item.title)
            setText(R.id.tv_sub_title, item.desc)
            setText(R.id.tv_author_name, item.author)
            setText(R.id.tv_time, item.niceDate)
            val imageView = holder.getView<ImageView>(R.id.iv_main_icon)
            if (!item.envelopePic.isNullOrEmpty()) {
                imageView.load(item.envelopePic)
            }
        }
    }
}