package com.devmin.android_review.presentation.app.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentAllRoomBinding
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.devmin.android_review.presentation.app.common.BaseViewHandler
import com.devmin.android_review.presentation.app.room.adapter.RoomAdapter
import com.devmin.android_review.presentation.extension.baseIntent
import com.devmin.android_review.presentation.extension.showBackToTopAnimation
import kotlinx.android.synthetic.main.fragment_all_room.*

class AllRoomFragment : BaseFragment<AllRoomFragmentViewModel>(), RoomFavoriteViewHandler {
    private lateinit var binding: FragmentAllRoomBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_room, container, false)
        binding.viewHandler = ViewHandler()
        binding.viewModel = getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allRoomList?.layoutManager = LinearLayoutManager(context)
        allRoomList.setItemViewCacheSize(20)
        val adapter = RoomAdapter(requireContext(), getViewModel(), this)
        getViewModel().roomPagedList?.observe(this.viewLifecycleOwner, {
            adapter.submitList(it)
            pullToRefresh?.isRefreshing = false
        })
        allRoomList?.adapter = adapter

        pullToRefresh?.setOnRefreshListener {
            getViewModel().refresh()
        }
        goToTop?.let { floatingActionButton ->
            allRoomList?.showBackToTopAnimation(floatingActionButton)
        }
    }

    override fun roomEnd(room: Room) {
        getViewModel().end(room)
        baseIntent("devmin://android.app/room/end/")
    }

    override fun like(room: Room) {
        getViewModel().create(room)
    }

    override fun unlike(room: Room) {
        getViewModel().delete(room)
    }

    inner class ViewHandler : BaseViewHandler() {
        fun goToTop() {
            allRoomList?.smoothScrollToPosition(0)
        }

        override fun retry() {
            getViewModel().refresh()
        }
    }
}