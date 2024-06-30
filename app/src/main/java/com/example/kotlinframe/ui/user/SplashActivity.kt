package com.example.kotlinframe.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseVMActivity
import com.aleyn.mvvm.base.NoViewModel
import com.aleyn.mvvm.ext.countDownCoroutines
import com.aleyn.mvvm.utils.StatusBarSettingHelper
import com.example.kotlinframe.databinding.ActivitySplashBinding
import com.example.kotlinframe.ui.MainActivity

/**
 *@author : hfj
 */
class SplashActivity : BaseVMActivity<NoViewModel, ActivitySplashBinding>() {

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        StatusBarSettingHelper.setStatusBarTranslucent(this)
        countDownCoroutines(3, lifecycleScope, onTick = {
            mBinding.tvSkip.text = "${it}秒跳过"
        }) {
            jumpAndFinish()
        }
        mBinding.tvSkip.setOnClickListener {
            jumpAndFinish()
        }
    }

    private fun jumpAndFinish() {
        startActivity(Intent(this, MainActivity::class.java)).also { finish() }
    }
}