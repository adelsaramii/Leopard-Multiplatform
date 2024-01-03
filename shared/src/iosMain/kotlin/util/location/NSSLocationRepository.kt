package com.attendace.leopard.util.location

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.ClassKit.CLSErrorCodeAuthorizationDenied
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.darwin.NSObject
import kotlin.coroutines.resume

class NSSLocationRepository(private val onPermissionGrant: () -> Unit) {

    private val locationManager: CLLocationManager by lazy { CLLocationManager() }
    private var runner: Boolean = true

    @OptIn(ExperimentalForeignApi::class)
    suspend fun getCurrentLocation(): LocationEntity {
        return suspendCancellableCoroutine { continuation ->
            locationManager.setDelegate(LocationManagerDelegates({
                onPermissionGrant()
            }) { locations ->
                locationManager.stopUpdatingLocation()

                locations.lastOrNull()?.coordinate?.useContents {
                    if (runner) {
                        continuation.resume(LocationEntity(latitude, longitude))
                        runner = false
                    }
                }
            })
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.requestWhenInUseAuthorization()
            locationManager.startUpdatingLocation()
        }
    }
}

data class LocationEntity(
    val latitude: Double,
    val longitude: Double
)

class LocationManagerDelegates(
    private val onPermissionGrant: () -> Unit,
    private val onLocationUpdate: (List<CLLocation>) -> Unit,
) :
    NSObject(), CLLocationManagerDelegateProtocol {

    private var locations: List<CLLocation> = listOf<CLLocation>()

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        @Suppress("UNCHECKED_CAST")
        locations = didUpdateLocations as List<CLLocation>
        onLocationUpdate(locations)
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        if (manager.authorizationStatus() == 3 || manager.authorizationStatus() == 4) {
            onPermissionGrant()
        }
    }

}