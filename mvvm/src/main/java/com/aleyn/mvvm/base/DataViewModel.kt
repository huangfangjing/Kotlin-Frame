package com.aleyn.mvvm.base

import com.aleyn.mvvm.entity.ListResponse
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 *@author : hfj
 */
open class DataViewModel<T> : RefreshViewModel() {
    val datas = MutableSharedFlow<ListResponse<T>>(1)
}