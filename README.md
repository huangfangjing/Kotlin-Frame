# Kotlin+JetPack+协程实现的MVVM架构Android客户端

### 项目基于Kotlin语言，采用ViewModel+flow实现MVVM架构。
### 网络请求采用协程+retrofit+okhttp，兼容ViewBinding和Databinding
### 高度封装的基类BaseVMActivity+列表ListRefreshActivity

<img src="https://github.com/huangfangjing/Kotlin-Frame/blob/master/test1.gif" width="400px">           <img src="https://github.com/huangfangjing/Kotlin-Frame/blob/master/test2.gif" width="400px">  

#### 部分代码：

#### 请求数据

      override fun loadData() {
        viewModel.getBarData()
     }

#### 数据回调

      override fun initObserve() {
        flowLaunch {
            viewModel.mHorData.asSharedFlow().flowWithLifecycle(lifecycle).collect {
                mBinding.horView.buildDate(it) { twoLevelData ->
                    showToast(twoLevelData.indexName)
                }
            }
        }
    }



