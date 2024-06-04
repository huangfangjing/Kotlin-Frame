package com.aleyn.mvvm.base

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 *@author : hfj
 */
open class RefreshViewModel : BaseViewModel() {
    val _refreshState = MutableSharedFlow<Unit>()
    var refreshState = _refreshState.asSharedFlow()
}