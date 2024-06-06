package com.example.kotlinframe.ui.home

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *@author : hfj
 */
class ViewPageFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var fragments: SparseArray<Fragment>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return fragments.size()
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    fun setData(fragments: SparseArray<Fragment>) {
        this.fragments = fragments
    }
}