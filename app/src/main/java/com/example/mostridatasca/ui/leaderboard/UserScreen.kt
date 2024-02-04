package com.example.mostridatasca.ui.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.model.User
import com.example.mostridatasca.ui.ImageFromBase64
import com.example.mostridatasca.ui.map.MapStyle
import com.example.mostridatasca.ui.profile.UserInformation2
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun UserScreen(
    user: User?,
    selectUser: (User?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { selectUser(null) },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user?.name ?: "No user selected",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                ImageFromBase64(
                    image = user?.picture ?: stringResource(id = R.string.default_user_image),
                    modifier = Modifier
                        .size(180.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Divider()
        UserInformation2(
            lifePoints = user?.life.toString(),
            experience = user?.experience.toString()
        )
        Divider()
        if (user?.positionshare!!) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                properties = MapProperties(
                    minZoomPreference = 12.0f,
                    mapStyleOptions = MapStyleOptions(MapStyle.json)
                ),
                cameraPositionState = CameraPositionState(
                    position = CameraPosition(LatLng(user.lat!!, user.lon!!), 0.0f, 0.0f, 0.0f)
                )
            ) {
                Marker(
                    state = MarkerState(
                        position = LatLng(user.lat, user.lon)
                    ),
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.player)
                )
            }
        }
    }
}