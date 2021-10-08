package com.katyrin.yandexmaptest.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.katyrin.yandexmaptest.R
import com.katyrin.yandexmaptest.databinding.FragmentMainBinding
import com.katyrin.yandexmaptest.utils.checkLocationPermission

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.main_container)
        binding?.showOnMapButton?.setOnClickListener { openMapScreen() }
    }

    private fun openMapScreen(): Unit = checkLocationPermission {
        val navDirections: NavDirections = MainFragmentDirections
            .actionMainFragmentToMapFragment(latitude.toFloat(), longitude.toFloat())
        navController?.navigate(navDirections)
    }

    override fun onDestroyView() {
        binding = null
        navController = null
        super.onDestroyView()
    }
}