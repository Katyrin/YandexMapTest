package com.katyrin.yandexmaptest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.katyrin.yandexmaptest.R
import com.katyrin.yandexmaptest.databinding.FragmentMapBinding
import com.katyrin.yandexmaptest.model.data.MyPoint
import com.katyrin.yandexmaptest.utils.*
import com.katyrin.yandexmaptest.viewmodel.AppState
import com.katyrin.yandexmaptest.viewmodel.MapViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(), InputListener, Session.SearchListener, LocationListener {

    private var binding: FragmentMapBinding? = null
    private var searchManager: SearchManager? = null
    private var navController: NavController? = null
    private var myLocation: Point? = null
    private val viewModel: MapViewModel by viewModel()

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
        firstInit()
        if (savedInstanceState == null) firstSetMapPosition()
        else secondSetMapPosition(savedInstanceState)
        binding?.mapview?.map?.addInputListener(this)
    }

    private fun firstInit() {
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        navController = Navigation.findNavController(requireActivity(), R.id.main_container)
        viewModel.liveData.observe(viewLifecycleOwner, ::renderData)
        viewModel.getMyPointList()
        setFragmentResultListener(DIALOG_REQUEST_KEY) { _, bundle ->
            viewModel.saveCacheLocationWithAddress(bundle.getString(ADDRESS_REQUEST_KEY) ?: "")
        }
        setFragmentResultListener(EDIT_REQUEST_KEY) { _, bundle ->
            val point: Point = bundle.getParcelable<MyPoint>(POINT_REQUEST_KEY)
                ?.let { Point(it.latitude, it.longitude) } ?: Point()
            binding?.mapview?.map?.move(getCameraPosition(point))
            myLocation = point
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> toast(appState.message)
            is AppState.CachePoint -> openEditScreen(appState.myPoint)
            is AppState.SuccessList -> appState.myPoints.forEach { showPointIntoMap(it) }
        }
    }

    private fun openEditScreen(myPoint: MyPoint) {
        viewModel.getMyPointList()
        showPointIntoMap(myPoint)
        val navDirections: NavDirections =
            MapFragmentDirections.actionMapFragmentToEditFragment(myPoint)
        navController?.navigate(navDirections)
    }

    private fun showPointIntoMap(myPoint: MyPoint): Unit = with(myPoint) {
        binding?.mapview?.map?.mapObjects?.addPlacemark(Point(latitude, longitude))
    }

    private fun firstSetMapPosition(): Unit = with(MapFragmentArgs.fromBundle(requireArguments())) {
        binding?.mapview?.map?.move(
            getCameraPosition(Point(latitude.toDouble(), longitude.toDouble())),
            Animation(Animation.Type.SMOOTH, DURATION_ANIMATION),
            null
        )
        MapKitFactory.getInstance().createLocationManager().subscribeForLocationUpdates(
            DESIRED_ACCURACY,
            MINIMAL_TIME,
            MINIMAL_DISTANCE,
            false,
            FilteringMode.OFF,
            this@MapFragment
        )
    }

    override fun onLocationUpdated(location: Location) {
        if (myLocation == null) binding?.mapview?.map?.move(getCameraPosition(location.position))
        myLocation = location.position
    }

    override fun onLocationStatusUpdated(status: LocationStatus) {}

    private fun secondSetMapPosition(savedInstanceState: Bundle): Unit = with(savedInstanceState) {
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

    override fun onMapTap(map: Map, point: Point) {}

    override fun onMapLongTap(map: Map, point: Point) {
        viewModel.cacheLocation(point.latitude, point.longitude)
        searchManager?.submit(point, map.cameraPosition.zoom.toInt(), SearchOptions(), this)
    }

    override fun onSearchResponse(response: Response) {
        response.collection.children.firstOrNull()?.obj
            ?.metadataContainer
            ?.getItem(ToponymObjectMetadata::class.java)
            ?.address
            ?.apply { openInfoScreen(formattedAddress) }
    }

    private fun openInfoScreen(address: String) {
        val navDirections: NavDirections = MapFragmentDirections
            .actionMapFragmentToInfoDialogFragment(address)
        navController?.navigate(navDirections)
    }

    override fun onSearchError(error: Error) {
        toast(requireContext().getString(R.string.error_network))
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
        searchManager = null
        navController = null
        myLocation = null
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
        const val DESIRED_ACCURACY = 0.0
        const val MINIMAL_TIME = 1000L
        const val MINIMAL_DISTANCE = 1.0
    }
}