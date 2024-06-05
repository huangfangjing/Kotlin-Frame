package com.aleyn.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *@author : hfj
 */
abstract class QuickAdapter<T>(layoutResId: Int) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId)
