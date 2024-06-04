package com.aleyn.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *@author : hfj
 */
abstract class QuickAdapter<T>(private val layoutResId: Int, open val itemClick: OnItemClickListener? = null ):BaseQuickAdapter<T,BaseViewHolder>(layoutResId) {
    init {
       setOnItemClickListener(itemClick)
    }
}