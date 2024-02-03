package com.example.mostridatasca.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.ui.ImageFromBase64
import com.example.mostridatasca.ui.objects.defaultImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState


@Composable
fun MapScreen(
    viewModel: MapViewModel, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    GoogleMap(
        modifier = modifier, properties = MapProperties(
            isMyLocationEnabled = true, minZoomPreference = 12.0f
        )
    ) {
        uiState.objects.forEach { virtualObject ->

            MarkerInfoWindowContent(
                state = MarkerState(
                    position = LatLng(
                        virtualObject.lat, virtualObject.lon
                    )
                ), icon = BitmapDescriptorFactory.fromResource(
                    when (virtualObject.type) {
                        "candy" -> R.drawable.candy
                        "weapon" -> R.drawable.weapon
                        "amulet" -> R.drawable.amulet
                        "armor" -> R.drawable.armor
                        else -> R.drawable.monster
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .width(180.dp)
                        .padding(10.dp)
                ) {
                    Text(
                        text = virtualObject.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    ImageFromBase64(
                        image = virtualObject.image ?: defaultImage(virtualObject.type),
                        modifier = Modifier
                            .size(90.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                    // 2 Text allineati a sinistra e uno a destra
                    Row {
                        Text(
                            text = "Type:",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = virtualObject.type,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row {
                        Text(
                            text = "Level:",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = virtualObject.level.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        uiState.users.forEach { user ->

            MarkerInfoWindowContent(
                state = MarkerState(
                    position = LatLng(
                        user.lat!!, user.lon!!
                    )
                ), icon = BitmapDescriptorFactory.fromResource(R.drawable.player)
            ) {
                Column(
                    modifier = Modifier
                        .width(180.dp)
                        .padding(10.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    ImageFromBase64(
                        image = user.picture ?: stringResource(id = R.string.default_user_image),
                        modifier = Modifier
                            .size(90.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                    Row {
                        Text(
                            text = "Life points:",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = user.life.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row {
                        Text(
                            text = "Experience:",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = user.experience.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}