package com.aleyn.mvvm.ext

import com.blankj.utilcode.util.SizeUtils

/**
 *@author : hfj
 */
fun Any.dpToPx(value: Int): Int {
    return SizeUtils.dp2px(value.toFloat())
}