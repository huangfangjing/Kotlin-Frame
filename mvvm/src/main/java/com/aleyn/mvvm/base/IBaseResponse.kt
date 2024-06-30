package com.aleyn.mvvm.base

import androidx.annotation.Keep


@Keep
interface IBaseResponse<T> {
    fun code(): Int
    fun msg(): String
    fun data(): T
    fun isSuccess(): Boolean
}