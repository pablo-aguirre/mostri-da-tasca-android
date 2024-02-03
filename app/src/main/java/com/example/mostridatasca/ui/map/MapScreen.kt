package com.example.mostridatasca.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mostridatasca.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker


@Composable
fun MapScreen(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    GoogleMap(
        modifier = modifier,
        properties = MapProperties(
            isMyLocationEnabled = true,
            minZoomPreference = 12.0f
        )
    ) {
        uiState.objects.forEach { virtualObject ->
            Marker(
                position = LatLng(virtualObject.lat, virtualObject.lon),
                title = virtualObject.name,
                snippet = "Long press to open details",
                onInfoWindowLongClick = {},
                onClick = {
                    it.showInfoWindow()
                    true
                },
                icon = BitmapDescriptorFactory.fromResource(
                    when (virtualObject.type) {
                        "candy" -> R.drawable.candy
                        "weapon" -> R.drawable.weapon
                        "amulet" -> R.drawable.amulet
                        "armor" -> R.drawable.armor
                        else -> R.drawable.monster
                    }
                )
            )
        }
    }
}