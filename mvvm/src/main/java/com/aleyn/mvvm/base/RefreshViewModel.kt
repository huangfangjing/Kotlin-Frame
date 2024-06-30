package com.aleyn.mvvm.base

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


open class RefreshViewModel : BaseViewModel() {
    val refreshState = MutableSharedFlow<Unit>(1)
}