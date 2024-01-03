package com.attendace.leopard.presentation.screen.register_attendance

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.utsman.osmandcompose.CameraProperty
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.InfoWindowData
import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.MarkerState
import com.utsman.osmandcompose.OsmAndroidComposable
import com.utsman.osmandcompose.OsmAndroidScope
import com.utsman.osmandcompose.OsmInfoWindow
import com.utsman.osmandcompose.OverlayManagerState
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberMapViewWithLifecycle
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.rememberOverlayManagerState
import kotlinx.coroutines.awaitCancellation
import org.osmdroid.api.IMapController
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

/*
* rewritten cuz wierd auto zoom in main library. by Adel :D
* */

@Composable
fun OsmMap(
    modifier: Modifier = Modifier,
    cameraState: CameraState = rememberCameraState(),
    overlayManagerState: OverlayManagerState = rememberOverlayManagerState(),
    properties: MapProperties = MapProperties(zoomButtonVisibility = ZoomButtonVisibility.NEVER),
    onMapClick: (GeoPoint) -> Unit = {},
    onMapLongClick: (GeoPoint) -> Unit = {},
    onFirstLoadListener: () -> Unit = {},
    content: (@Composable @OsmAndroidComposable OsmAndroidScope.() -> Unit)? = null
) {

    val mapView = rememberMapViewWithLifecycle()

    val mapListeners = remember {
        MapListeners()
    }.also {
        it.onMapClick = onMapClick
        it.onMapLongClick = onMapLongClick
        it.onFirstLoadListener = {
            onFirstLoadListener.invoke()
        }
    }

    val mapProperties by rememberUpdatedState(properties)

    val parentComposition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)

    LaunchedEffect(Unit) {
        disposingComposition {
            mapView.newComposition(parentComposition) {
                MapViewUpdater(mapProperties, mapListeners, cameraState, overlayManagerState)
                currentContent?.invoke(object : OsmAndroidScope {})
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView
        },
    )
}

class MapListeners {
    var onMapClick: (GeoPoint) -> Unit by mutableStateOf({})
    var onMapLongClick: (GeoPoint) -> Unit by mutableStateOf({})
    var onFirstLoadListener: (String) -> Unit by mutableStateOf({})
}

suspend inline fun disposingComposition(factory: () -> Composition) {
    val composition = factory()
    try {
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}

private fun MapView.newComposition(
    parent: CompositionContext,
    content: @Composable () -> Unit
): Composition {
    return Composition(
        MapApplier(this), parent
    ).apply {
        setContent(content)
    }
}

class MapApplier(
    val mapView: MapView
) : AbstractApplier<OsmAndNode>(OsmNodeRoot) {

    private val decorations = mutableListOf<OsmAndNode>()

    override fun insertBottomUp(index: Int, instance: OsmAndNode) {
        decorations.add(index, instance)
        instance.onAttached()
    }

    override fun insertTopDown(index: Int, instance: OsmAndNode) {
    }

    override fun move(from: Int, to: Int, count: Int) {
        decorations.move(from, to, count)
    }

    override fun onClear() {
        mapView.overlayManager.clear()
        decorations.forEach { it.onCleared() }
        decorations.clear()
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            decorations[index + it].onRemoved()
        }
        decorations.remove(index, count)
    }

    internal fun invalidate() = mapView.postInvalidate()

}

interface OsmAndNode {
    fun onAttached() {}
    fun onRemoved() {}
    fun onCleared() {}
}

object OsmNodeRoot : OsmAndNode

@Composable
internal fun MapViewUpdater(
    mapProperties: MapProperties,
    mapListeners: MapListeners,
    cameraState: CameraState,
    overlayManagerState: OverlayManagerState
) {
    val mapViewComposed = (currentComposer.applier as MapApplier).mapView

    ComposeNode<MapPropertiesNode, MapApplier>(factory = {
        MapPropertiesNode(mapViewComposed, mapListeners, cameraState, overlayManagerState)
    }, update = {

        set(mapProperties.mapOrientation) { mapViewComposed.mapOrientation = it }
        set(mapProperties.isMultiTouchControls) { mapViewComposed.setMultiTouchControls(it) }
        set(mapProperties.minZoomLevel) { mapViewComposed.minZoomLevel = it }
        set(mapProperties.maxZoomLevel) { mapViewComposed.maxZoomLevel = it }
        set(mapProperties.isFlingEnable) { mapViewComposed.isFlingEnabled = it }
        set(mapProperties.isUseDataConnection) { mapViewComposed.setUseDataConnection(it) }
        set(mapProperties.isTilesScaledToDpi) { mapViewComposed.isTilesScaledToDpi = it }
        set(mapProperties.tileSources) { if (it != null) mapViewComposed.setTileSource(it) }
        set(mapProperties.overlayManager) { if (it != null) mapViewComposed.overlayManager = it }

        set(mapProperties.isEnableRotationGesture) {
            val rotationGesture = RotationGestureOverlay(mapViewComposed)
            rotationGesture.isEnabled = it
            mapViewComposed.overlayManager.add(rotationGesture)
        }

        set(mapProperties.zoomButtonVisibility) {
            val visibility = when (it) {
                ZoomButtonVisibility.ALWAYS -> CustomZoomButtonsController.Visibility.ALWAYS
                ZoomButtonVisibility.SHOW_AND_FADEOUT -> CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
                ZoomButtonVisibility.NEVER -> CustomZoomButtonsController.Visibility.NEVER
            }

            mapViewComposed.zoomController.setVisibility(visibility)
        }
    })
}

internal class MapPropertiesNode(
    val mapViewComposed: MapView,
    val mapListeners: MapListeners,
    private val cameraState: CameraState,
    overlayManagerState: OverlayManagerState
) : OsmAndNode {

    private var delayedMapListener: DelayedMapListener? = null
    private var eventOverlay: MapEventsOverlay? = null

    init {
        overlayManagerState.setMap(mapViewComposed)
        cameraState.setMap(mapViewComposed)
    }

    override fun onAttached() {
        mapViewComposed.controller.setCenter(cameraState.geoPoint)
        mapViewComposed.controller.setZoom(cameraState.zoom)

        delayedMapListener = DelayedMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                val currentGeoPoint =
                    mapViewComposed.let { GeoPoint(it.mapCenter.latitude, it.mapCenter.longitude) }
                cameraState.geoPoint = currentGeoPoint
                return false
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                val currentZoom = mapViewComposed.zoomLevelDouble
                cameraState.zoom = currentZoom
                return false
            }
        }, 1000L)

        mapViewComposed.addMapListener(delayedMapListener)

        val eventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let { mapListeners.onMapClick.invoke(it) }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let { mapListeners.onMapLongClick.invoke(it) }
                return true
            }
        }

        eventOverlay = MapEventsOverlay(eventsReceiver)

        mapViewComposed.overlayManager.add(eventOverlay)

        if (mapViewComposed.isLayoutOccurred) {
            mapListeners.onFirstLoadListener.invoke("")
        }
    }

    override fun onCleared() {
        super.onCleared()
        delayedMapListener?.let { mapViewComposed.removeMapListener(it) }
        eventOverlay?.let { mapViewComposed.overlayManager.remove(eventOverlay) }
    }

    override fun onRemoved() {
        super.onRemoved()
        delayedMapListener?.let { mapViewComposed.removeMapListener(it) }
        eventOverlay?.let { mapViewComposed.overlayManager.remove(eventOverlay) }
    }
}

class CameraState(cameraProperty: CameraProperty) {

    var geoPoint: GeoPoint by mutableStateOf(cameraProperty.geoPoint)
    var zoom: Double by mutableStateOf(cameraProperty.zoom)
    var speed: Long by mutableStateOf(cameraProperty.speed)

    private var map: MapView? = null

    private var prop: CameraProperty
        get() {
            val currentGeoPoint =
                map?.let { GeoPoint(it.mapCenter.latitude, it.mapCenter.longitude) } ?: geoPoint
            val currentZoom = map?.zoomLevelDouble ?: zoom
            return CameraProperty(currentGeoPoint, currentZoom, speed)
        }
        set(value) {
            synchronized(Unit) {
                geoPoint = value.geoPoint
                zoom = value.zoom
                speed = value.speed
            }
        }

    internal fun setMap(osmMapView: MapView) {
        map = osmMapView
    }

    private fun getController(): IMapController {
        return map?.controller ?: throw IllegalStateException("Invalid Map attached!")
    }

    fun animateTo(geoPoint: GeoPoint) = getController().animateTo(geoPoint)
    fun animateTo(x: Int, y: Int) = getController().animateTo(x, y)
    fun scrollBy(x: Int, y: Int) = getController().scrollBy(x, y)
    fun setCenter(point: GeoPoint) = getController().setCenter(point)
    fun setZoom(pZoomLevel: Double): Double = getController().setZoom(pZoomLevel)
    fun stopAnimation(jumpToFinish: Boolean) = getController().stopAnimation(jumpToFinish)
    fun stopPanning() = getController().stopPanning()
    fun zoomIn(animationSpeed: Long? = null) = getController().zoomIn(animationSpeed)
    fun zoomInFixing(xPixel: Int, yPixel: Int, zoomAnimation: Long?): Boolean =
        getController().zoomInFixing(xPixel, yPixel, zoomAnimation)

    fun zoomInFixing(xPixel: Int, yPixel: Int): Boolean =
        getController().zoomInFixing(xPixel, yPixel)

    fun zoomOut(animationSpeed: Long? = null) = getController().zoomOut(animationSpeed)
    fun zoomOutFixing(xPixel: Int, yPixel: Int): Boolean =
        getController().zoomOutFixing(xPixel, yPixel)

    fun zoomToFixing(zoomLevel: Int, xPixel: Int, yPixel: Int, zoomAnimationSpeed: Long?): Boolean =
        getController().zoomToFixing(zoomLevel, xPixel, yPixel, zoomAnimationSpeed)

    fun zoomTo(pZoomLevel: Double, animationSpeed: Long? = null): Boolean =
        getController().zoomTo(pZoomLevel, animationSpeed)

    fun zoomToSpan(latSpan: Double, lonSpan: Double) = getController().zoomToSpan(latSpan, lonSpan)

    fun animateTo(point: GeoPoint, pZoom: Double? = null, pSpeed: Long? = null) =
        getController().animateTo(point, pZoom, pSpeed)

    fun animateTo(point: GeoPoint, pZoom: Double? = null, pSpeed: Long? = null, pOrientation: Float = 0f) =
        getController().animateTo(point, pZoom, pSpeed, pOrientation)

    fun animateTo(
        point: GeoPoint,
        pZoom: Double? = null,
        pSpeed: Long? = null,
        pOrientation: Float = 0f,
        pClockwise: Boolean = false
    ) = getController().animateTo(point, pZoom, pSpeed, pOrientation, pClockwise)

    fun normalizeRotation() {
        getController().animateTo(geoPoint, zoom, null, 0f)
    }

    companion object {
        val Saver: Saver<CameraState, CameraProperty> = Saver(
            save = { it.prop },
            restore = { CameraState(it) }
        )
    }
}

@Composable
fun rememberCameraState(
    key: String? = null,
    cameraProperty: CameraProperty.() -> Unit = {}
): CameraState = rememberSaveable(key = key, saver = CameraState.Saver) {
    val prop = CameraProperty().apply(cameraProperty)
    CameraState(prop)
}

@Composable
@OsmAndroidComposable
fun Marker(
    state: MarkerState = rememberMarkerState(),
    icon: Drawable? = null,
    visible: Boolean = true,
    title: String? = null,
    snippet: String? = null,
    onClick: (Marker) -> Boolean = { false },
    id: String? = null,
    infoWindowContent: @Composable (InfoWindowData) -> Unit = {}
) {

    val context = LocalContext.current
    val applier = currentComposer.applier as? MapApplier ?: throw IllegalStateException("Invalid Applier")

    ComposeNode<MarkerNode, MapApplier>(
        factory = {
            val mapView = applier.mapView
            val marker = Marker(mapView).apply {
                position = state.geoPoint
                rotation = state.rotation

                setVisible(visible)
                icon?.let { this.icon = it }
                id?.let { this.id = it }
            }

            mapView.overlayManager.add(marker)

            val composeView = ComposeView(context)
                .apply {
                    setContent {
                        infoWindowContent.invoke(InfoWindowData(title.orEmpty(), snippet.orEmpty()))
                    }
                }

            val infoWindow = OsmInfoWindow(composeView, mapView)
            marker.infoWindow = infoWindow
            infoWindow.view.setOnClickListener {
                onClick.invoke(marker)
            }

            marker.showInfoWindow()
            MarkerNode(
                mapView = mapView,
                markerState = state,
                marker = marker,
                onMarkerClick = onClick
            )
        },
        update = {
            update(state.geoPoint) {
                marker.position = it
            }
            update(state.rotation) {
                marker.rotation = it
            }
            update(icon) {
                if (it == null) {
                    marker.setDefaultIcon()
                } else {
                    marker.icon = it
                }
            }
            update(visible) {
                marker.setVisible(it)
            }
            applier.invalidate()
        })
}

internal class MarkerNode(
    val mapView: MapView,
    val markerState: MarkerState,
    val marker: Marker,
    var onMarkerClick: (Marker) -> Boolean
) : OsmAndNode {

    override fun onAttached() {
        markerState.marker = marker
    }

    override fun onRemoved() {
        markerState.marker = null
        marker.remove(mapView)
    }

    override fun onCleared() {
        markerState.marker = null
        marker.remove(mapView)
    }

}