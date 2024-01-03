package com.attendace.leopard.presentation.screen.register_attendance

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.R
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.util.theme.neutralLight0Dark10
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.secondaryColor
import com.attendace.leopard.data.model.Workplace
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel
import com.utsman.osmandcompose.rememberMarkerState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MapView(
    nearWorkplaces: LoadableData<List<Workplace>>,
    selectedWorkplace: Workplace?,
    location: RegisterAttendanceViewModel.Location?,
    selectWorkplace: (Workplace) -> Unit
) {
    val cameraState = rememberCameraState {
        if (location != null) {
            geoPoint = GeoPoint(location.lat, location.lang)
        } else {
            GeoPoint(48.741895, 10.989308)
        }
        zoom = 19.0
    }

    if (nearWorkplaces is Loaded) {
        OsmMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
        ) {
            if (location != null) {
                Marker(
                    state = rememberMarkerState(
                        geoPoint = GeoPoint(location.lat, location.lang)
                    ),
                    title = null,
                    icon = LocalContext.current.getDrawable(R.drawable.ic_user_location),
                )

                nearWorkplaces.data.map {
                    Marker(
                        state = rememberMarkerState(
                            geoPoint = GeoPoint(it.latitude, it.longitude)
                        ),
                        onClick = {
                            val workplace = nearWorkplaces.data.find { workplace ->
                                workplace.id == it.id
                            }
                            if (workplace != null) {
                                selectWorkplace.invoke(workplace)
                            }
                            true
                        },
                        title = it.name,
                        icon =
                        if (selectedWorkplace?.id == it.id) LocalContext.current.getDrawable(R.drawable.ic_tringle)
                        else LocalContext.current.getDrawable(R.drawable.ic_tringle_selected),
                        id = it.id
                    ) { _ ->
                        Row(
                            modifier = Modifier.background(
                                if (selectedWorkplace?.id == it.id) primaryColor
                                else secondaryColor
                            ).padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource("ic_location.xml"),
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(it.name, color = neutralLight0Dark10, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        LaunchedEffect(location) {
            if (location != null) {
                cameraState.animateTo(GeoPoint(location.lat, location.lang))
            }
        }
    }
}