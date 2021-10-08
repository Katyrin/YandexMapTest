package com.katyrin.yandexmaptest.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.katyrin.yandexmaptest.R
import com.katyrin.yandexmaptest.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var binding: FragmentMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMapBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val latitude: Float = MapFragmentArgs.fromBundle(requireArguments()).latitude
        val longitude: Float = MapFragmentArgs.fromBundle(requireArguments()).longitude
        val locationText = "Latitude: $latitude \nLongitude: $longitude"
        binding?.locationText?.text = locationText
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}