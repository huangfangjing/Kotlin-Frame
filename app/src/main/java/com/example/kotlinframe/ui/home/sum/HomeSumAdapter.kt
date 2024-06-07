package com.example.kotlinframe.ui.home.sum

import android.widget.ImageView
import com.aleyn.mvvm.adapter.QuickAdapter
import com.aleyn.mvvm.ext.dpToPx
import com.aleyn.mvvm.ext.setUrl
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kotlinframe.R
import com.example.kotlinframe.network.entity.ProjectSubInfo
import com.example.kotlinframe.utils.ViewUtils

/**
 *@author : hfj
 */
class HomeSumAdapter : QuickAdapter<ProjectSubInfo>(R.layout.layout_home_tab_item), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: ProjectSubInfo) {
        with(holder) {
            setText(R.id.tv_title, item.title)
            setText(R.id.tv_sub_title, item.desc)
            setText(R.id.tv_author_name, item.author)
            setText(R.id.tv_time, item.niceDate)
            val imageView = holder.getView<ImageView>(R.id.iv_main_icon)
            if (!item.envelopePic.isNullOrEmpty()) {
                imageView.setUrl(item.envelopePic)
            }
            ViewUtils.setClipViewCornerRadius(holder.itemView, dpToPx(8))
        }
    }
}