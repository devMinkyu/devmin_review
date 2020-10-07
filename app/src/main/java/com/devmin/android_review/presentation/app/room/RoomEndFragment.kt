package com.devmin.android_review.presentation.app.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentFavoriteRoomBinding
import com.devmin.android_review.databinding.FragmentRoomEndBinding
import com.devmin.android_review.presentation.app.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_favorite_room.*

class RoomEndFragment : BaseFragment<RoomEndFragmentViewModel>() {
    private lateinit var binding: FragmentRoomEndBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_end, container, false)
        binding.viewHandler = ViewHandler()
        binding.viewModel = getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteReviewList?.layoutManager = LinearLayoutManager(context)
    }

    inner class ViewHandler {

    }
}