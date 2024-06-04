package com.example.kotlinframe

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *   @author : Aleyn
 *   time   : 2019/11/04
 */
class MyApplication : Application() {

    companion object {
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
                ClassicsHeader(context)
            }
        }
    }


    override fun onCreate() {
        super.onCreate()

        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }
    }
}