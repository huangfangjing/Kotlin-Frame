package com.aleyn.mvvm.widget.loading

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.aleyn.mvvm.R


/**
 * 等待提示框
 */
class LoadingUtils(private val mContext: Context) {

    private var loadView: CenterLoadingView? = null

    private var preDismiss = false
    private var mHandler = Handler(Looper.getMainLooper()) {
        preDismiss = false
        true
    }

    /**
     * 统一耗时操作Dialog
     */
    fun showLoading(txt: String?) {
        if (preDismiss) {
            return
        }
        if (loadView == null) {
            loadView = CenterLoadingView(mContext, R.style.dialog)
        }
        if (loadView?.isShowing == true) {
            loadView?.dismiss()
        }
        if (!TextUtils.isEmpty(txt)) {
            loadView?.setTitle(txt as CharSequence)
        }
        if (mContext is Activity && mContext.isFinishing) {
            return
        }
        loadView?.show()
    }

    /**
     * 关闭Dialog
     */
    fun dismissLoading() {
        if (mContext is Activity && mContext.isFinishing) {
            return
        }

        loadView?.let {
            if (it.isShowing) {
                it.dismiss()
                preDismiss = true
                mHandler.sendEmptyMessageDelayed(0, 500)
            }
        }
    }
}
