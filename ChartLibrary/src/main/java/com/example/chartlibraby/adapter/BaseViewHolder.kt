package com.example.chartlibraby.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *@author : hfj
 */

class BaseViewHolder : RecyclerView.ViewHolder {
    lateinit var viewBinding: ViewDataBinding

    constructor(itemView: View) : super(itemView)

    constructor(binding: ViewDataBinding) : super(binding.root) {
        this.viewBinding = binding
    }

    fun getBinding(): ViewDataBinding {
        return viewBinding
    }

    fun setBinding(binding: ViewDataBinding) {
        this.viewBinding = binding
    }
}
