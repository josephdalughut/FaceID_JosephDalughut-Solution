/*
 * Created by Joseph Dalughut on 22/10/2021, 00:00
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.presentation

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A [FragmentStateAdapter] implementation for displaying fragment items in a [ViewPager]
 */
class FragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var fragments: List<Fragment>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        fragments?.let {
            return it.count()
        } ?: run {
            return 0
        }
    }

    override fun createFragment(position: Int): Fragment {
        return fragments!![position]
    }
}