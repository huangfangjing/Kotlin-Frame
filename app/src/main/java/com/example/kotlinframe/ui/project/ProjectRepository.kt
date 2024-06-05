package com.example.kotlinframe.ui.project

import com.aleyn.mvvm.base.BaseModel
import com.example.chartlibraby.bean.StaticData
import com.example.chartlibraby.bean.TwoLevelData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *@author : hfj
 */
class ProjectRepository:BaseModel() {

    companion object{
        @Volatile
        private var INSTANCE: ProjectRepository? = null

        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProjectRepository().also { INSTANCE = it }
            }
    }

    fun getBarData():List<TwoLevelData>{
        return Gson().fromJson(StaticData.DATA_BAR, object : TypeToken<List<TwoLevelData>>() {}.type)
    }

    fun getLineData():List<TwoLevelData>{
        return Gson().fromJson(StaticData.DATA_Line, object : TypeToken<List<TwoLevelData>>() {}.type)
    }
}