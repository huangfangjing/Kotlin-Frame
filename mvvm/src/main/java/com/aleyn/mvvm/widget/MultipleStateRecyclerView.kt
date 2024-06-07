package com.aleyn.mvvm.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aleyn.mvvm.R
import com.aleyn.mvvm.ext.gone
import com.aleyn.mvvm.ext.visible

/**
 *@author : hfj
 */
class MultipleStateRecyclerView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    var recyclerView: RecyclerView? = null
    var throwableLatout: LinearLayout? = null
    var throwableImage: ImageView? = null
    var throwableText: TextView? = null


    init {
        LayoutInflater.from(getContext()).inflate(R.layout.mutiple_state_recyclerview, this)
        recyclerView = findViewById(R.id.recycler_view)
        throwableLatout = findViewById(R.id.ll_throwable)
        throwableImage = findViewById(R.id.iv_throwable_tip)
        throwableText = findViewById(R.id.tv_throwable_tip)
    }

    fun showContentView() {
        recyclerView?.visible()
        throwableLatout?.gone()
    }

    fun showThrowableView() {
        recyclerView?.gone()
        throwableLatout?.visible()
    }

    fun showEmptyView() {
        showThrowableView()
        throwableImage?.setImageResource(R.drawable.icon_data_empty)
        throwableText?.text = "暂无数据"
    }

    fun showNetWorkError() {
        showThrowableView()
        throwableImage?.setImageResource(R.drawable.icon_net_error)
        throwableText?.text = "网络异常"
    }

    fun showSystemError() {
        showThrowableView()
        throwableImage?.setImageResource(R.drawable.icon_net_error)
        throwableText?.text = "系统异常"
    }

}