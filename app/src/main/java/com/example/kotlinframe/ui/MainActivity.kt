package com.example.kotlinframe.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aleyn.mvvm.adapter.QuickAdapter
import com.aleyn.mvvm.base.BaseVMActivity
import com.aleyn.mvvm.base.NoViewModel
import com.example.kotlinframe.R
import com.example.kotlinframe.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : BaseVMActivity<NoViewModel, ActivityMainBinding>() {


    override fun initView(savedInstanceState: Bundle?) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        mBinding.navigaView.setupWithNavController(navHostFragment.navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            mBinding.toolbar.title = when (destination.id) {
                R.id.home -> "首页"
                R.id.project -> "项目"
                else -> "我的"
            }
        }
    }
}

fun main() {
}
