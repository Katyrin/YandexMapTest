package com.katyrin.yandexmaptest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.katyrin.yandexmaptest.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map

class MapFragment : Fragment(), InputListener {

    private var binding: FragmentMapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(requireContext())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMapBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) firstSetMapPosition()
        else secondSetMapPosition(savedInstanceState)
        binding?.mapview?.map?.addInputListener(this)
    }

    private fun firstSetMapPosition(): Unit =
        with(MapFragmentArgs.fromBundle(requireArguments())) {
            binding?.mapview?.map?.move(
                getCameraPosition(Point(latitude.toDouble(), longitude.toDouble())),
                Animation(Animation.Type.SMOOTH, DURATION_ANIMATION),
                null
            )
        }

    private fun secondSetMapPosition(savedInstanceState: Bundle): Unit =
        with(savedInstanceState) {
            val point = Point(getDouble(CURRENT_LATITUDE), getDouble(CURRENT_LONGITUDE))
            binding?.mapview?.map?.move(getCameraPosition(point, getFloat(CURRENT_ZOOM)))
        }

    private fun getCameraPosition(point: Point, zoom: Float = START_ZOOM): CameraPosition =
        CameraPosition(point, zoom, AZIMUTH, TILT)

    override fun onStart() {
        super.onStart()
        binding?.mapview?.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onMapTap(map: Map, point: Point) {
        map.mapObjects.addPlacemark(point)
    }

    override fun onMapLongTap(map: Map, point: Point) {
        map.mapObjects.addPlacemark(point)
    }

    override fun onStop() {
        super.onStop()
        binding?.mapview?.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapview?.map?.cameraPosition?.apply {
            outState.putFloat(CURRENT_ZOOM, zoom)
            outState.putDouble(CURRENT_LATITUDE, target.latitude)
            outState.putDouble(CURRENT_LONGITUDE, target.longitude)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private companion object {
        const val START_ZOOM = 14.0f
        const val AZIMUTH = 0.0f
        const val TILT = 0.0f
        const val DURATION_ANIMATION = 1f
        const val CURRENT_ZOOM = "CURRENT_ZOOM"
        const val CURRENT_LATITUDE = "CURRENT_LATITUDE"
        const val CURRENT_LONGITUDE = "CURRENT_LONGITUDE"
    }
}