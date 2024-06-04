package com.aleyn.mvvm.base

import com.aleyn.mvvm.entity.ListResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
open class ListViewModel<T> : RefreshViewModel() {
    val _datas = MutableSharedFlow<ListResponse<T>>(1)
    var datas = _datas.asSharedFlow()
}