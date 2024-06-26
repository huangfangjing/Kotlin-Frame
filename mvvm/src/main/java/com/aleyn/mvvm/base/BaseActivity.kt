package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.aleyn.mvvm.R
import com.aleyn.mvvm.databinding.BaseViewBinding
import com.aleyn.mvvm.event.Message
import com.aleyn.mvvm.ext.gone
import com.aleyn.mvvm.ext.visible
import com.aleyn.mvvm.widget.loading.LoadingUtils
import java.lang.reflect.ParameterizedType


@Suppress("DEPRECATION")
abstract class BaseActivity<DB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mBinding: DB

    open val layoutId: Int = 0

    lateinit var baseViewBinding: BaseViewBinding

    private val dialogUtils by lazy { LoadingUtils(baseContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
        initBinding()
        initView(savedInstanceState)
        initObserve()
        loadData()
    }

    open fun initView(savedInstanceState: Bundle?) {}
    open fun initObserve() {}
    open fun loadData() {}

    /**
     * 设置状态栏底色颜色
     */
    open fun setStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.colorPrimary)

        //设置状态栏字体为白色
        val stateView: View = window.decorView
        if (stateView != null) {
            var vis = stateView.systemUiVisibility
            vis = vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //白色
            stateView.systemUiVisibility = vis //设置状态栏字体颜色
        }
    }

    /**
     * DataBinding or ViewBinding
     */
    open fun initBinding() {
        val type = javaClass.genericSuperclass

        val baseViewCls = BaseViewBinding::class.java
        baseViewBinding = baseViewCls.getDeclaredMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as BaseViewBinding

        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments
                .map { it as Class<*> }
                .first { ViewBinding::class.java.isAssignableFrom(it) }
            when {
                ViewDataBinding::class.java.isAssignableFrom(cls) && cls != ViewDataBinding::class.java -> {
                    if (layoutId == 0) throw IllegalArgumentException("Using DataBinding requires overriding method layoutId")
                    mBinding = DataBindingUtil.setContentView(this, layoutId)
                    (mBinding as ViewDataBinding).lifecycleOwner = this
                    baseViewBinding.flContainer.takeIf { it.childCount == 0 }.let {
                        it?.addView(
                            mBinding.root,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }

                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, layoutInflater) as DB
                        if (baseViewBinding.flContainer.childCount == 0) {
                            baseViewBinding.flContainer.addView(
                                mBinding.root,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                        setContentView(baseViewBinding.root)
                    }
                }

                else -> {
                    if (layoutId == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    setContentView(layoutId)
                }
            }
        } else throw IllegalArgumentException("Generic error")
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    protected fun showLoading(tip: String = "加载中...") {
        if (lifecycle.currentState == Lifecycle.State.STARTED) {
            dialogUtils.showLoading(tip)
        }
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        dialogUtils.dismissLoading()
    }

    open fun showContentView() {
        baseViewBinding.flContainer.visible()
        baseViewBinding.llThrowable.gone()
    }

    open fun showThrowableView() {
        baseViewBinding.flContainer.gone()
        baseViewBinding.llThrowable.visible()
    }

    open fun showEmptyView() {
        showThrowableView()
        baseViewBinding.ivThrowableTip?.setImageResource(R.drawable.icon_data_empty)
        baseViewBinding.tvThrowableTip?.text = "暂无数据"
    }

    open fun showNetWorkError() {
        showThrowableView()
        baseViewBinding.ivThrowableTip?.setImageResource(R.drawable.icon_net_error)
        baseViewBinding.tvThrowableTip?.text = "网络异常"
    }

    open fun showSystemError() {
        showThrowableView()
        baseViewBinding.ivThrowableTip?.setImageResource(R.drawable.icon_net_error)
        baseViewBinding.tvThrowableTip?.text = "系统异常"
    }

}