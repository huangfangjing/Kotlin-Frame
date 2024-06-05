package com.aleyn.mvvm.ext

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.blankj.utilcode.util.ToastUtils

/**
 *@author : hfj
 */
object AppExt {
    private var preExit = false
    private var mHandler = Handler(Looper.getMainLooper()) {
        preExit = false
        true
    }

    fun onBackPressed(
        activity: Activity,
        tipCallback: (() -> Unit) = {
            ToastUtils.showShort("再按一次退出程序")
        },
        closeCallback: (() -> Unit) = {
            activity.finish()
        }
    ) {
        if (!preExit) {
            preExit = true
            tipCallback()
            mHandler.sendEmptyMessageDelayed(0, 2000)
        } else {
            closeCallback()
        }
    }

}