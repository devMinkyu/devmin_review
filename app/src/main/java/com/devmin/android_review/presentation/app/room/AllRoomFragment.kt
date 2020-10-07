package com.devmin.android_review.presentation.app.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentAllRoomBinding
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.devmin.android_review.presentation.app.room.adapter.AllRoomAdapter
import kotlinx.android.synthetic.main.fragment_all_room.*

class AllRoomFragment : BaseFragment<AllRoomFragmentViewModel>() {
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
        val adapter = AllRoomAdapter(requireContext(), getViewModel())
        getViewModel().roomPagedList?.observe(this.viewLifecycleOwner, {
            adapter.submitList(it)
        })
        allRoomList?.adapter = adapter
    }

    inner class ViewHandler
}