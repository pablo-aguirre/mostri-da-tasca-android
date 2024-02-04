package com.example.mostridatasca.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    GoogleMap(
        modifier = modifier,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapStyleOptions = MapStyleOptions(MapStyle.jsonBlue)
        )
    ) {
        uiState.objects.forEach { virtualObject ->
            MarkerInfoWindowContent(
                state = MarkerState(
                    position = LatLng(virtualObject.lat, virtualObject.lon)
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
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MarkerTitle(virtualObject.name)
                    MarkerImage(virtualObject.image ?: defaultImage(virtualObject.type))
                    MarkerInformation(left = "Type:", right = virtualObject.type)
                    MarkerInformation(left = "Level:", right = virtualObject.level.toString())
                }
            }
        }
        uiState.users.forEach { user ->
            MarkerInfoWindowContent(
                state = MarkerState(
                    position = LatLng(user.lat!!, user.lon!!)
                ), icon = BitmapDescriptorFactory.fromResource(R.drawable.player)
            ) {
                Column(
                    modifier = Modifier
                        .width(180.dp)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MarkerTitle(user.name)
                    MarkerImage(user.picture ?: stringResource(R.string.default_user_image))
                    MarkerInformation(left = "Life points:", right = user.life.toString())
                    MarkerInformation(left = "Experience:", right = user.experience.toString())
                }
            }
        }
    }
    if (uiState.errorMessage.isNotBlank()) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Attention") },
            text = { Text(uiState.errorMessage) },
            confirmButton = {
                Button(onClick = { viewModel.deleteError() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun MarkerTitle(title: String) {
    Text(
        text = title, style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun MarkerImage(image: String) {
    ImageFromBase64(
        image = image,
        modifier = Modifier
            .size(90.dp)
            .padding(10.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MarkerInformation(left: String, right: String) {
    Row {
        Text(
            text = left, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f)
        )
        Text(
            text = right,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(1f)
        )
    }
}
