package com.katyrin.yandexmaptest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.katyrin.yandexmaptest.databinding.FragmentEditBinding
import com.katyrin.yandexmaptest.utils.EDIT_REQUEST_KEY
import com.katyrin.yandexmaptest.utils.POINT_REQUEST_KEY

class EditFragment : Fragment() {

    private var binding: FragmentEditBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentEditBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(): Unit = with(EditFragmentArgs.fromBundle(requireArguments())) {
        setFragmentResult(EDIT_REQUEST_KEY, bundleOf(POINT_REQUEST_KEY to myPoint))
        binding?.apply {
            addressEditText.setText(myPoint?.address)
            latitudeEditText.setText(myPoint?.latitude.toString())
            longitudeEditText.setText(myPoint?.longitude.toString())
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}