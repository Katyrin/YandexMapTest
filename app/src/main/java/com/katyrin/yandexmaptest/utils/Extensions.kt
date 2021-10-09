package com.katyrin.yandexmaptest.utils

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.katyrin.yandexmaptest.R

private const val REQUEST_LOCATION_CODE = 154

inline fun Fragment.checkLocationPermission(
    crossinline onPermissionGranted: Location.() -> Unit
): Unit = when {
    isLocationPermissionGranted() -> checkNullLocation(onPermissionGranted)
    shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> showRationaleDialog()
    else -> requestLocationPermission()
}

fun Fragment.isLocationPermissionGranted(): Boolean = PackageManager.PERMISSION_GRANTED ==
        ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)

inline fun Fragment.checkNullLocation(onPermissionGranted: Location.() -> Unit) {
    val location: Location? = getLocation()
    if (location != null) onPermissionGranted(location) else toast(R.string.error_location)
}

@SuppressLint("MissingPermission")
fun Fragment.getLocation(): Location? =
    with(requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager) {
        getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

fun Fragment.toast(resource: Int): Unit =
    Toast.makeText(requireContext(), resource, Toast.LENGTH_LONG).show()

fun Fragment.toast(message: String?): Unit =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun Fragment.showRationaleDialog(): Unit =
    AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.access_to_location))
        .setMessage(getString(R.string.explanation_get_location))
        .setPositiveButton(getString(R.string.grant_access)) { _, _ -> requestLocationPermission() }
        .setNegativeButton(getString(R.string.do_not)) { dialog, _ -> dialog.dismiss() }
        .create()
        .show()

fun Fragment.requestLocationPermission(): Unit =
    ActivityCompat.requestPermissions(
        requireActivity(),
        arrayOf(ACCESS_FINE_LOCATION),
        REQUEST_LOCATION_CODE
    )