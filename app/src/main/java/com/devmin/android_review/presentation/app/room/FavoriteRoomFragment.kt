package com.devmin.android_review.presentation.app.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentFavoriteRoomBinding
import com.devmin.android_review.entity.Result
import com.devmin.android_review.entity.Room
import com.devmin.android_review.entity.Sort
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.devmin.android_review.presentation.app.common.BaseViewHandler
import com.devmin.android_review.presentation.app.room.adapter.RoomAdapter
import com.devmin.android_review.presentation.extension.baseIntent
import com.devmin.android_review.presentation.extension.makeGone
import com.devmin.android_review.presentation.extension.makeVisible
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_favorite_room.*
import kotlin.math.abs

class FavoriteRoomFragment : BaseFragment<FavoriteRoomFragmentViewModel>(),
    RoomFavoriteViewHandler {
    private lateinit var binding: FragmentFavoriteRoomBinding
    var adapter:RoomAdapter? = null
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

        adapter = RoomAdapter(requireContext(), getViewModel(), this)
        favoriteReviewList?.adapter = adapter
        connectPaging()
        getViewModel().filterLiveData.observe(this.viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                if (adapter == null) {
                    adapter = RoomAdapter(requireContext(), getViewModel(), this)
                    favoriteReviewList?.adapter = adapter
                    connectPaging()
                }
            }
        })

        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                goToTop?.makeVisible()
            } else {
                goToTop?.makeGone()
            }
        })
    }

    private fun connectPaging() {
        getViewModel().favoritePagedLiveData.observe(this.viewLifecycleOwner, {
            if(it.isEmpty()) {
                getViewModel().isResult.set(Result.EMPTY)
            } else{
                getViewModel().isResult.set(Result.SUCCESS)
            }
            adapter?.submitList(it)
        })
    }

    override fun roomEnd(room: Room) {
        getViewModel().end(room)
        baseIntent("devmin://android.app/room/end/")
    }

    override fun like(room: Room) {}

    override fun unlike(room: Room) {
        getViewModel().delete(room)
    }

    inner class ViewHandler : BaseViewHandler(){
        fun goToTop() {
            appBarLayout?.setExpanded(true, true)
            favoriteReviewList?.scrollToPosition(0)
        }
        fun sort(sort: Sort) {
            adapter = null
            favoriteReviewList?.adapter = null
            getViewModel().sort(sort)
        }

        override fun retry() {
            super.retry()
            adapter = null
            favoriteReviewList?.adapter = null
            getViewModel().refresh()
        }
    }
}