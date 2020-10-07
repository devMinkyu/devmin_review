package com.devmin.android_review.presentation.app.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentFavoriteRoomBinding
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.devmin.android_review.presentation.app.room.adapter.AllRoomAdapter
import com.devmin.android_review.presentation.extension.baseIntent
import kotlinx.android.synthetic.main.fragment_favorite_room.*

class FavoriteRoomFragment : BaseFragment<FavoriteRoomFragmentViewModel>(),
    RoomFavoriteViewHandler {
    private lateinit var binding: FragmentFavoriteRoomBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_room, container, false)
        binding.viewHandler = ViewHandler()
        binding.viewModel = getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteReviewList?.layoutManager = LinearLayoutManager(context)
        favoriteReviewList?.setItemViewCacheSize(20)
        val adapter = AllRoomAdapter(requireContext(), getViewModel(), this)
        favoriteReviewList?.adapter = adapter
        getViewModel().favoritePagedLiveData.observe(
            this.viewLifecycleOwner,
            Observer(adapter::submitList)
        )
    }

    override fun roomEnd(room: Room) {
        getViewModel().end(room)
        baseIntent("devmin://android.app/room/end/")
    }

    override fun like(room: Room) {}

    override fun unlike(room: Room) {
        getViewModel().delete(room)
    }

    inner class ViewHandler {
        fun goToTop() {
//            roomContainer?.scrollTo(0, 0)
        }
    }
}