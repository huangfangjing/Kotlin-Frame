package com.example.chartlibraby.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hfj on 18/5/24.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }
}
