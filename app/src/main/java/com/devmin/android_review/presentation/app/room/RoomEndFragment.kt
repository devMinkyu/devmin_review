package com.devmin.android_review.presentation.app.room

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentRoomEndBinding
import com.devmin.android_review.presentation.app.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_room_end.*
import kotlin.math.round

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
        val params = appBar.layoutParams
        val width = requireContext().resources.displayMetrics.widthPixels
        params.width = width
        params.height = round(width * 0.66).toInt()
        getViewModel().roomLiveDate.observe(this.viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { room ->
                if (room.description?.imagePath != null && TextUtils.isEmpty(room.description?.imagePath)
                        .not()
                ) {
                    roomImage?.let { appCompatImageView ->
                        Glide.with(appCompatImageView)
                            .load(room.description?.imagePath)
                            .into(appCompatImageView)
                    }
                }
            }
        })
    }

    inner class ViewHandler {
        fun back() {
            parentFragmentManager.popBackStack()
        }
        fun like() {
            getViewModel().like()
        }
    }
}