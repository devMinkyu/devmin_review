package com.devmin.android_review.presentation.app.room.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmin.android_review.R
import com.devmin.android_review.databinding.ItemRoomBinding
import com.devmin.android_review.entity.Result
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseViewModel
import com.devmin.android_review.presentation.app.room.AllRoomFragmentViewModel
import com.devmin.android_review.presentation.app.room.FavoriteRoomFragmentViewModel
import com.devmin.android_review.presentation.app.room.RoomFavoriteViewHandler
import com.devmin.android_review.presentation.extension.listAnimation
import com.devmin.android_review.presentation.extension.makeGone
import kotlinx.android.synthetic.main.item_room.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RoomAdapter(
    val viewModel: BaseViewModel,
    val viewHandler: RoomFavoriteViewHandler
) : PagedListAdapter<Room, RoomItemViewHolder>(RoomDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoomItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
    ).listAnimation() as RoomItemViewHolder

    override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
        if(viewModel.isResult.get() != Result.SUCCESS) {
            viewModel.isResult.set(Result.SUCCESS)
        }
        val item = getItem(position)
        item?.let { room ->
            val favorite = when (viewModel) {
                is AllRoomFragmentViewModel -> {
                    viewModel.favoriteMap[room.id] = ObservableBoolean(viewModel.favoriteSet.contains(room.id.toString()))
                    viewModel.favoriteMap[room.id]!!
                }
                is FavoriteRoomFragmentViewModel -> {
                    ObservableBoolean(true)
                }
                else -> ObservableBoolean(false)
            }
            holder.binding?.let {
                with(holder.binding) {
                    this.room = room
                    this.favorite = favorite
                    this.viewHandler = viewHandler
                }
            }
//            holder.binding?.room = room
//            holder.binding?.favorite = favorite
//            holder.binding?.viewHandler = viewHandler

            with(holder.itemView) {
                room.lastUpdate?.let {
                    lastUpdate?.setReferenceTime(it.time)
                } ?: lastDate?.makeGone()

                like?.setOnClickListener {
                    favorite.set(favorite.get().not())
                    if (favorite.get()) {
                        viewHandler.like(room)
                    } else {
                        viewHandler.unlike(room)
                    }
                }
                if (TextUtils.isEmpty(room.thumbnail).not()) {
                    settingImage(holder, room)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    skeletonGroup?.finishAnimation()
                    skeletonGroup?.setShowSkeleton(false)
                }
            }
        }
    }

    private fun settingImage(holder: RoomItemViewHolder, room: Room) {
        holder.itemView.thumbnail?.let { appCompatImageView ->
            Glide.with(appCompatImageView)
                .load(room.thumbnail)
                .into(appCompatImageView)
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