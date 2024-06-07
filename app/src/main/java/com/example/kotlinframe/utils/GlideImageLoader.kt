package com.example.kotlinframe.utils

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.aleyn.mvvm.ext.setUrlWithoutPlace
import com.pcl.mvvm.network.entity.BannerBean
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

/**
 *   @auther : Aleyn
 *   time   : 2019/09/05
 */
class GlideImageLoader : BannerImageAdapter<BannerBean>(null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerImageHolder {

        var imageView = AppCompatImageView(parent.context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return BannerImageHolder(imageView)
    }

    override fun onBindView(
        holder: BannerImageHolder?,
        data: BannerBean?,
        position: Int,
        size: Int
    ) {
        holder?.imageView?.setUrlWithoutPlace(data?.imagePath)
    }

}