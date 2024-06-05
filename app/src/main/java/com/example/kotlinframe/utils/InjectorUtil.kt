package com.pcl.mvvm.utils

import com.example.kotlinframe.data.HomeRepository
import com.example.kotlinframe.ui.project.ProjectRepository

object InjectorUtil {

    fun getHomeRepository() = HomeRepository.getInstance()

    fun getProjectRepository() = ProjectRepository.getInstance()

}