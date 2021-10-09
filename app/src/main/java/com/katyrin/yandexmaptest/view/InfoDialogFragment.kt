package com.katyrin.yandexmaptest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.katyrin.yandexmaptest.databinding.FragmentInfoDialogBinding
import com.katyrin.yandexmaptest.utils.ADDRESS_REQUEST_KEY
import com.katyrin.yandexmaptest.utils.DIALOG_REQUEST_KEY

class InfoDialogFragment : BottomSheetDialogFragment() {

    private var binding: FragmentInfoDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentInfoDialogBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val address: String = InfoDialogFragmentArgs.fromBundle(requireArguments()).address
        binding?.addressText?.text = address
        binding?.confirmButton?.setOnClickListener {
            setFragmentResult(DIALOG_REQUEST_KEY, bundleOf(ADDRESS_REQUEST_KEY to address))
            dismiss()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}