package com.pcl.mvvm.network.entity

data class YsBean(
    var finish: String,
    var halfFinish: String,
    var noFinish: String,
    var list: MutableList<YsListBean>
)

data class YsListBean(
    var bookName: String?,
    var className: String?,
    var examineState: String?,
    var studentName: String?
)