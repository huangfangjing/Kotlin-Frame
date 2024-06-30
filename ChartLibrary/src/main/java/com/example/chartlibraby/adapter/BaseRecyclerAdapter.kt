package com.example.chartlibraby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;


public abstract  class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDataList;
    protected int mLayoutId;
    protected ViewDataBinding binding;
    protected View.OnClickListener mOnClickListener;

    public BaseRecyclerAdapter(Context context, List<T> dataList, int layoutId) {
        mContext = context;
        mDataList = dataList;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseRecyclerAdapter(Context context, List<T> dataList, int layoutId, View.OnClickListener mClickListener) {
        mContext = context;
        mDataList = dataList;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
        this.mOnClickListener = mClickListener;
    }

    public void setmDataList(List<T> dataList){
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public List<T> getmDataList() {
        return mDataList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(mInflater, mLayoutId, viewGroup, false);
        BaseViewHolder viewHolder = new BaseViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);
        binding.getRoot().setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(position);
        T t = mDataList.get(position);
        setItemData(holder,position,t);
    }

    public  void setDatas(List<T> datas){
        this.mDataList = datas;
        notifyDataSetChanged();
    }


    public abstract void setItemData(BaseViewHolder holder,int position,T t);

    protected ViewDataBinding getBinding(){
        return binding;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            Object object = v.getTag();
            if (object!=null){
                int position = (int) object;
                if (!mDataList.isEmpty()){
                    mItemClickListener.onItemClick(position,mDataList.get(position));
                }
            }
        }
    }

    public interface OnItemClickListener<T>{
        void onItemClick(int position, T data);
    }
    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
