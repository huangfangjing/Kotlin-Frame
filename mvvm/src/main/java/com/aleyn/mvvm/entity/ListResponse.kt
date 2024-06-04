package com.aleyn.mvvm.entity

/**
 *@author : hfj
 */
data class ListResponse<T>(
    val curPage: Int,
    val pageCount: Int,
    val total: Int,
    val datas: MutableList<T>
)