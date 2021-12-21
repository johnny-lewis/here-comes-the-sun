package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.domain.model.ForecastDay
import au.com.lexicon.herecomesthesun.domain.model.ForecastHour
import au.com.lexicon.herecomesthesun.presentation.component.DrawSensorGraph
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModelContract
import au.com.lexicon.herecomesthesun.presentation.viewmodel.UVRatingGrades
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId

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

            TableScreen()
        }
    }
}



@Composable
fun TableScreen() {
    // Just a fake data... a Pair of Int and String
    val tableData = (1..5).mapIndexed { index, item ->
        index to "Item $index"
    }

    val forecast_hour = ForecastHour(Instant.now(), 23.00, 40, 3.00)

    val list = MutableList(5) {index -> forecast_hour}

//    Timber.i(list.toString())

    val forecast_day = ForecastDay(Instant.now(), 25.00, 19.00, 23.00, 3.00, list)

//    Timber.i(forecast_day.toString())

    val forecast_list = MutableList(5) {index -> forecast_day}

    // Each cell of a column must have the same weight.
    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%
    // The LazyColumn will be our table. Notice the use of the weights below



    Row(Modifier.background(Color.Gray)){

        forecast_list.forEach {
            TableCell(text = "${it.date.atZone(ZoneId.systemDefault()).dayOfWeek}", weight = column1Weight)

        } }
    Row(){

        forecast_list.forEach {
            TableCell(text = "${it.uv}", weight = column1Weight)

        } }
}






@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp),
        fontSize = 10.sp,
        )
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
