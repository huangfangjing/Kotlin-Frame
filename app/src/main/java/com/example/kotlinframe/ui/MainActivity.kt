package com.example.kotlinframe.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aleyn.mvvm.base.BaseVMActivity
import com.aleyn.mvvm.base.NoViewModel
import com.aleyn.mvvm.ext.AppExt
import com.aleyn.mvvm.widget.navigation.SumFragmentNavigator
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
        //1.寻找出路由控制器对象，它是路由跳转的唯一入口，找到宿主NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navController = findNavController(R.id.nav_fragment)

        //2.自定义FragmentNavigator，mobile_navigation.xml文件中的fragment标识改为SumFragmentNavigator的sumFragment
        val fragmentNavigator =
            SumFragmentNavigator(this, navHostFragment.childFragmentManager, navHostFragment.id)
        //3.注册到Navigator里面，这样才找得到
        navController.navigatorProvider.addNavigator(fragmentNavigator)
        //4.设置Graph，需要将activity_main.xml文件中的app:navGraph="@navigation/mobile_navigation"移除
        navController.setGraph(R.navigation.main_navigation)

        mBinding.navigaView.setupWithNavController(navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            mBinding.toolbar.title = when (destination.id) {
                R.id.home -> "首页"
                R.id.project -> "项目"
                else -> "我的"
            }
        }
    }

    override fun onBackPressed() {
        AppExt.onBackPressed(this)
    }
}

