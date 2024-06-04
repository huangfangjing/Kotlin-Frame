package com.pcl.mvvm.utils

import com.example.kotlinframe.data.HomeRepository

object InjectorUtil {

    fun getHomeRepository() = HomeRepository.getInstance()

}