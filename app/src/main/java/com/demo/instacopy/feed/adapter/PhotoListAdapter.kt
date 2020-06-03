package com.demo.instacopy.feed.adapter
/*
* Created by hrank8t on 03-06-2020 - 16:30:25.
*/




import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.demo.instacopy.feed.data.State
import com.demo.instacopy.feed.models.Photos


class PhotoListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<Photos, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private var colorControlNormal: Int = 0
    private val dataViewType = 1
    private val footerViewType = 2

    private var state = State.LOADING


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == dataViewType) {
            PhotoViewHolder.create(parent, colorControlNormal)
        } else {
            ListFooterViewHolder.create(retry, parent)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == dataViewType) {
            (holder as PhotoViewHolder).bind(getItem(position))
        } else {
            (holder as ListFooterViewHolder).bind(state)
        }
    }


    override fun getItemViewType(position: Int): Int {
//       Single statement return function
        return if (position < super.getItemCount()) {
            dataViewType
        } else {
            footerViewType
        }
    }

    companion object {
        //        Companion Object
//        There is nothing called static in Kotlin. So, in Kotlin, we use a companion object.
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Photos>() {
            override fun areItemsTheSame(oldItem: Photos, newItem: Photos): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photos, newItem: Photos): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }


}