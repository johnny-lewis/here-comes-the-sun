package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.domain.model.ForecastDay
import au.com.lexicon.herecomesthesun.domain.model.ForecastHour
import au.com.lexicon.herecomesthesun.presentation.component.DrawSensorGraph
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModelContract
import au.com.lexicon.herecomesthesun.presentation.viewmodel.UVRatingGrades
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId

@Composable
fun HomeScreen(
    viewModel: HomeViewModelContract
) {
    val context = rememberCoroutineScope()
    val UVRating by viewModel.UVFlow.collectAsState()
    val dayFlow by viewModel.dayFlow.collectAsState()
    val timeFlow by viewModel.timeFlow.collectAsState()

    val color = when (UVRating) {
        UVRatingGrades.UNKNOWN -> Color.Cyan
        UVRatingGrades.NIGHT -> Color.Green
        UVRatingGrades.BAD -> Color.Magenta
        UVRatingGrades.OK -> Color.Yellow
        UVRatingGrades.GOOD -> Color.Red
    }

    val icon = when (UVRating) {
        UVRatingGrades.UNKNOWN -> R.drawable.ic_sunny
        UVRatingGrades.NIGHT -> R.drawable.ic_sunny
        UVRatingGrades.BAD -> R.drawable.ic_sunny
        UVRatingGrades.OK -> R.drawable.ic_sunny
        UVRatingGrades.GOOD -> R.drawable.ic_sunny
    }

    Scaffold(topBar = {
        TopBar { viewModel.goToSettingsScreen() }
    }) {
        BoxWithConstraints {

            val maxHeight = this.maxHeight

            Column( //screen holder
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = color.copy(alpha = 0f))
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row( // top view
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxHeight / 10 * 3)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(2f)
                            .padding(top = 10.dp),
                    ) {
                        Text(
                            text = "Caufield, VIC",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Peak Times",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "11am",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "40%",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "11am",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "40%",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "1pm",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "30%",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "2pm",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "55%",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier,
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Today", // today
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    )
                    Row( // time
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight / 20)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Image(
                                modifier = Modifier
                                    .clickable {
                                        context.launch {
                                            viewModel.goPreviousTime()
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_arrow_back_black_24dp),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = when (timeFlow) {
                                    0 -> "12am"
                                    1 -> "6am"
                                    2 -> "12pm"
                                    else -> "6pm"
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = when (timeFlow) {
                                    0 -> "1am"
                                    1 -> "7am"
                                    2 -> "1pm"
                                    else -> "7pm"
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = when (timeFlow) {
                                    0 -> "2am"
                                    1 -> "8am"
                                    2 -> "2pm"
                                    else -> "8pm"
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = when (timeFlow) {
                                    0 -> "3am"
                                    1 -> "9am"
                                    2 -> "3pm"
                                    else -> "9pm"
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = when (timeFlow) {
                                    0 -> "4am"
                                    1 -> "10am"
                                    2 -> "4pm"
                                    else -> "10pm"
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = when (timeFlow) {
                                    0 -> "5am"
                                    1 -> "11am"
                                    2 -> "5pm"
                                    else -> "11pm"
                                },
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Image(
                                modifier = Modifier
                                    .clickable {
                                        context.launch {
                                            viewModel.goNextTime()
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_arrow_back_black_24dp),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }

                    Row( //values
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight / 20)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {}
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "10%",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "20%",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "10%",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "10%",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "10%",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "10%",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {}
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    DrawSensorGraph(viewModel = viewModel)
                }
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
    navigateToSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color(0xFFC23C64))
            .padding(start = 24.dp)
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        navigateToSettings()
                    },
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Column (
            modifier = Modifier
                .weight(4f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Image(
                modifier = Modifier
                    .height(90.dp)
                    .width(180.dp)
                    .clickable {
                        navigateToSettings()
                    },
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }

}
