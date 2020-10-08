package com.devmin.android_review.presentation.app.room.adapter

import android.content.Context
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
    val context: Context,
    val viewModel: BaseViewModel,
    val viewHandler: RoomFavoriteViewHandler
) : PagedListAdapter<Room, RoomItemViewHolder>(RoomDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoomItemViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
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
            holder.binding?.room = room
            holder.binding?.favorite = favorite
            holder.binding?.viewHandler = viewHandler

            room.lastUpdate?.let {
                holder.itemView.lastUpdate?.setReferenceTime(it.time)
            } ?: holder.itemView.lastDate?.makeGone()


            CoroutineScope(Dispatchers.IO).launch {
                when (viewModel) {
                    is FavoriteRoomFragmentViewModel -> delay(300)
                    else -> delay(800)
                }
                holder.itemView.skeletonGroup?.finishAnimation()
            }
            holder.itemView.like?.setOnClickListener {
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