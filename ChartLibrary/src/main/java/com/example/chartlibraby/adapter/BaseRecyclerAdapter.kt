package com.example.chartlibraby.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *@author : hfj
 */
abstract class BaseRecyclerAdapter<T>(
    var mContext: Context,
    var mDataList: List<T>,
    var mLayoutId: Int,
    var mOnClickListener: OnClickListener?
) : RecyclerView.Adapter<BaseViewHolder>() {

    lateinit var binding: ViewDataBinding
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)

    constructor(
        mContext: Context,
        mDataList: List<T>,
        mLayoutId: Int
    ) : this(mContext, mDataList, mLayoutId, null)


    fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): BaseViewHolder {
        binding = DataBindingUtil.inflate(mInflater, mLayoutId, viewGroup, false)
        val viewHolder = BaseViewHolder(binding.root)
        viewHolder.viewBinding = binding
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.tag = position
        val t = mDataList[position]
        setItemData(holder, position, t)
    }

    abstract fun setItemData(holder: BaseViewHolder, position: Int, t: T)

    override fun getItemCount(): Int {
        return if (mDataList.isNullOrEmpty()) 0 else mDataList.size
    }

}