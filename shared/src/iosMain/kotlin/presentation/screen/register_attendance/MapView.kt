package com.attendace.leopard.presentation.screen.register_attendance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.util.location.NSSLocationRepository
import com.attendace.leopard.data.model.Workplace
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.CoreGraphics.CGPointMake
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKPinAnnotationView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKUserLocation
import platform.MapKit.MKUserLocationView
import platform.UIKit.UIButton
import platform.UIKit.UIButtonType
import platform.UIKit.UIButtonTypeDetailDisclosure
import platform.UIKit.UIControl
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
fun MapView(
    nearWorkplaces: LoadableData<List<Workplace>>,
    selectedWorkplace: Workplace?,
    onCurrentLocationSet: (lat: Double, lon: Double) -> Unit,
    selectWorkplace: (Workplace) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val annotationList = arrayListOf<MKPointAnnotation>()

    var location = remember {
        if (selectedWorkplace != null) {
            CLLocationCoordinate2DMake(selectedWorkplace.latitude, selectedWorkplace.longitude)
        } else {
            nearWorkplaces.data?.getOrNull(0).let {
                if (it != null) {
                    CLLocationCoordinate2DMake(it.latitude, it.longitude)
                } else {
                    CLLocationCoordinate2DMake(48.741895, 10.989308)
                }
            }
        }
    }

    val mkMapView = remember {
        MKMapView()
    }

    LaunchedEffect(true) {
        coroutineScope.launch {
            val userLocationManager = NSSLocationRepository {
                coroutineScope.launch {
                    val userLocation = NSSLocationRepository {}.getCurrentLocation()

                    location =
                        CLLocationCoordinate2DMake(userLocation.latitude, userLocation.longitude)
                    mkMapView.setRegion(
                        MKCoordinateRegionMakeWithDistance(
                            centerCoordinate = location,
                            150.0, 150.0
                        ),
                        animated = false
                    )
                    onCurrentLocationSet(userLocation.latitude, userLocation.longitude)
                }
            }

            val userLocation = userLocationManager.getCurrentLocation()

            location = CLLocationCoordinate2DMake(userLocation.latitude, userLocation.longitude)
            mkMapView.setRegion(
                MKCoordinateRegionMakeWithDistance(
                    centerCoordinate = location,
                    150.0, 150.0
                ),
                animated = false
            )
            onCurrentLocationSet(userLocation.latitude, userLocation.longitude)
        }
    }

    LaunchedEffect(true) {
        mkMapView.showsUserLocation = true
        mkMapView.delegate = MKDelegate {
            nearWorkplaces.data?.find { workplace -> workplace.name == it?.title }
                ?.let { it1 -> selectWorkplace.invoke(it1) }
        }
        mkMapView.setRegion(
            MKCoordinateRegionMakeWithDistance(
                centerCoordinate = location,
                150.0, 150.0
            ),
            animated = false
        )
    }

    LaunchedEffect(nearWorkplaces) {
        if (nearWorkplaces is Loaded) {
            annotationList.clear()
            nearWorkplaces.data.map {
                val annotation = MKPointAnnotation(
                    CLLocationCoordinate2DMake(
                        it.latitude,
                        it.longitude
                    ),
                    title = it.name,
                    subtitle = null,
                )
                mkMapView.addAnnotation(
                    annotation
                )
                annotationList.add(annotation)
            }
        }
    }

    LaunchedEffect(selectedWorkplace) {
        mkMapView.annotations.map {
            if (it is MKUserLocation) return@map
            if (selectedWorkplace?.name == (it as MKPointAnnotation).title) {
                mkMapView.selectAnnotation(it, true)
            }
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mkMapView
        }
    )

}

@Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
private class MKDelegate(
    private val onAnnotationClicked: (MKAnnotationProtocol?) -> Unit
) : NSObject(), MKMapViewDelegateProtocol {

    // custom view for annotation (worked but commented cuz still we cant add title for custom annotation view)
    /*    @OptIn(ExperimentalForeignApi::class)
        override fun mapView(
            mapView: MKMapView,
            viewForAnnotation: MKAnnotationProtocol
        ): MKAnnotationView? {

            val reuseId = "maker"

            var view = mapView.dequeueReusableAnnotationViewWithIdentifier(reuseId)

            if (view == null) {
                view = MKAnnotationView(
                    MKPointAnnotation(
                        CLLocationCoordinate2DMake(32.6232205, 51.6258955),
                        title = null,
                        subtitle = null,
                    ), reuseId
                )
            }

            view.canShowCallout = true
            view.accessibilityLabel = "hello"
            view.tintColor = UIColor.redColor
            view.draggable = true
            view.image = UIImage.imageNamed("Workplace")
            val btn = UIButton()
            view.rightCalloutAccessoryView = btn

            return view
        }*/

    @OptIn(ExperimentalForeignApi::class)
    override fun mapView(
        mapView: MKMapView,
        viewForAnnotation: MKAnnotationProtocol
    ): MKAnnotationView? {

        val reuseId = "maker"

        val view = mapView.dequeueReusableAnnotationViewWithIdentifier(reuseId)

        if (view is MKUserLocationView) {
            return null
        }

        return view
    }

    override fun mapView(
        mapView: MKMapView,
        annotationView: MKAnnotationView,
        calloutAccessoryControlTapped: UIControl
    ) {
        onAnnotationClicked(annotationView.annotation)
    }

    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        val annotation = didSelectAnnotationView.annotation as MKPointAnnotation
        onAnnotationClicked(annotation)
    }

}

// region try for create title for custom annotation view

//private fun titleLabel(): UILabel {
//    val label = UILabel()
//    label.textColor = UIColor.redColor
//    label.textAlignment = NSTextAlignmentCenter
//    label.text = "Horaa"
//    label.backgroundColor = UIColor.white
//    label.layer.cornerRadius = 15
//    label.clipsToBounds = true
//    return label
//}

//class LEOMarker : UIView() {
//
//    private var markerImageView: UIImageView = UIImageView(UIImage.imageNamed("workplace"))
//
//    private fun titleLabel(): UILabel {
//        val lablel = UILabel()
//        lablel.textColor = UIColor.whiteColor
//        lablel.textAlignment = NSTextAlignmentCenter
//        lablel.text = "Horaa"
//        return lablel
//    }
//
//    init {
//        addSubview(markerImageView)
//        addSubview(titleLabel())
//    }
//
//}

//@OptIn(BetaInteropApi::class)
//class WorkplaceAnnotationView: MKAnnotationView(), ObjCClass {
//
//    private var marker = LEOMarker()
//
//    override fun initWithAnnotation(
//        annotation: MKAnnotationProtocol?,
//        reuseIdentifier: String?
//    ): MKAnnotationView {
//
//        addSubview(marker)
//        marker.translatesAutoresizingMaskIntoConstraints = false
//        translatesAutoresizingMaskIntoConstraints = false
//
//        return MKAnnotationView(annotation, reuseIdentifier)
//    }
//
//
//}

// endregion