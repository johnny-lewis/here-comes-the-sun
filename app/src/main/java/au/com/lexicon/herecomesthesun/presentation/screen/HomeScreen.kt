package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.presentation.component.DrawSensorGraph
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModelContract
import au.com.lexicon.herecomesthesun.presentation.viewmodel.UVRatingGrades

@Composable
fun HomeScreen(
    viewModel: HomeViewModelContract
) {
    val message by viewModel.messageFlow.collectAsState(initial = "")
    val UVRating by viewModel.UVFlow.collectAsState()
    var tabIndex by remember { mutableStateOf(0)}

    val color = when (UVRating) {
        UVRatingGrades.UNKNOWN -> Color.Cyan
        UVRatingGrades.NIGHT -> Color.Green
        UVRatingGrades.BAD -> Color.Magenta
        UVRatingGrades.OK -> Color.Yellow
        UVRatingGrades.GOOD -> Color.Red
    }

    Scaffold(topBar = {
        TopBar(color = color) { viewModel.goToSettingsScreen() }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color.copy(alpha = 0f))
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.h5
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                DrawSensorGraph(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TopBar(
    color: Color,
    navigateToSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = color)
            .padding(horizontal = 24.dp)
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //insert logo
        }
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            //insert settings
            Image(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable {
                        navigateToSettings()
                    },
                painter = painterResource(id = R.drawable.ic_settings_black),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }

}
