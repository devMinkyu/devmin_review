package com.devmin.android_review.presentation.app.room.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmin.android_review.R
import com.devmin.android_review.databinding.ItemRoomBinding
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseViewModel
import com.devmin.android_review.presentation.extension.listAnimation
import com.devmin.android_review.presentation.extension.makeGone
import kotlinx.android.synthetic.main.item_room.view.*

class AllRoomAdapter(
    val context: Context,
    val viewModel: BaseViewModel
) : PagedListAdapter<Room, RoomItemViewHolder>(RoomDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoomItemViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
    ).listAnimation() as RoomItemViewHolder

    override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { room ->
            holder.binding?.room = room
            holder.itemView.lastDate?.makeGone()

            if (TextUtils.isEmpty(room.thumbnail).not()) {
                holder.itemView.thumbnail?.let { appCompatImageView ->
                    Glide.with(appCompatImageView)
                        .load(room.thumbnail)
                        .into(appCompatImageView)
                }
            }
        }
    }
}

class RoomDiffUtil : DiffUtil.ItemCallback<Room>() {
    override fun areItemsTheSame(oldItem: Room, newItem: Room) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Room, newItem: Room) = oldItem == newItem
}

class RoomItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = DataBindingUtil.bind<ItemRoomBinding>(itemView)
}