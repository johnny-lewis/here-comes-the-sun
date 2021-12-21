package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun HomeScreen(
    viewModel: HomeViewModelContract
) {
    val context = rememberCoroutineScope()
    val UVRating by viewModel.UVFlow.collectAsState()
    val timeFlow by viewModel.timeFlow.collectAsState()
    val dayFlow by viewModel.dayFlow.collectAsState()
    val dayData by viewModel.dataDayFlow.collectAsState()
    val timeData by viewModel.dataTimeFlow.collectAsState()

    val locationFlow by viewModel.locationFlow.collectAsState()
    val topEfficienciesFlow by viewModel.topEfficienciesFlow.collectAsState()

    val color = when (UVRating) {
        UVRatingGrades.UNKNOWN -> Color.Cyan
        UVRatingGrades.NIGHT -> Color.Green
        UVRatingGrades.BAD -> Color.Magenta
        UVRatingGrades.OK -> Color.Yellow
        UVRatingGrades.GOOD -> Color.Red
    }

    val icon = when (UVRating) {
        UVRatingGrades.UNKNOWN -> R.drawable.ic_sunny
        UVRatingGrades.NIGHT -> R.drawable.ic_clear_night
        UVRatingGrades.BAD -> R.drawable.ic_partly_sunny
        UVRatingGrades.OK -> R.drawable.ic_mostly_sunny
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
                        .background(Color(0xFFF4E1E2))
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(2f)
                            .padding(top = 10.dp),
                    ) {
                        Text(
                            text = locationFlow,
                            style = TextStyle(
                                color = Color(0xFFC23C64),
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Peak Times",
                            style = TextStyle(
                                color = Color(0xFF754855),
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
                                    text = if (topEfficienciesFlow.count() == 3)
                                        topEfficienciesFlow[0].first
                                    else "-",
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (topEfficienciesFlow.count() == 3)
                                        topEfficienciesFlow[0].second
                                    else "-",
                                    style = TextStyle(
                                        color = Color(0xFF754855),
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
                                    text = if (topEfficienciesFlow.count() == 3)
                                        topEfficienciesFlow[1].first
                                    else "-",
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (topEfficienciesFlow.count() == 3)
                                        topEfficienciesFlow[1].second
                                    else "-",
                                    style = TextStyle(
                                        color = Color(0xFF754855),
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
                                    text = if (topEfficienciesFlow.count() == 3)
                                        topEfficienciesFlow[2].first
                                    else "-",
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (topEfficienciesFlow.count() == 3)
                                        topEfficienciesFlow[2].second
                                    else "-",
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
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

                if (timeData.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = when (dayFlow) {
                                0 -> "Today"
                                1 -> "Thursday"
                                2 -> "Friday"
                                else -> ""
                                                  }, // today
                            style = TextStyle(
                                color = Color(0xFF754855),
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
                                    .padding(top = 8.dp)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            context.launch {
                                                viewModel.goPreviousTime()
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_left_arrow_red),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (timeFlow) {
                                        0 -> "12am"
                                        1 -> "6am"
                                        2 -> "12pm"
                                        else -> "6pm"
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (timeFlow) {
                                        0 -> "1am"
                                        1 -> "7am"
                                        2 -> "1pm"
                                        else -> "7pm"
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (timeFlow) {
                                        0 -> "2am"
                                        1 -> "8am"
                                        2 -> "2pm"
                                        else -> "8pm"
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (timeFlow) {
                                        0 -> "3am"
                                        1 -> "9am"
                                        2 -> "3pm"
                                        else -> "9pm"
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (timeFlow) {
                                        0 -> "4am"
                                        1 -> "10am"
                                        2 -> "4pm"
                                        else -> "10pm"
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (timeFlow) {
                                        0 -> "5am"
                                        1 -> "11am"
                                        2 -> "5pm"
                                        else -> "11pm"
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(top = 8.dp)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            context.launch {
                                                viewModel.goNextTime()
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_right_arrow_red),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit
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
                                    .fillMaxHeight(),
                                horizontalAlignment= Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeData[0].toString(),
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment= Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeData[1].toString(),
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment= Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeData[2].toString(),
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment= Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeData[3].toString(),
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment= Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeData[4].toString(),
                                    style = TextStyle(
                                        color = Color(0xFF754855),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                horizontalAlignment= Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeData[5].toString(),
                                    style = TextStyle(
                                        color = Color(0xFF754855),
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

                TableScreen(
                    viewModel = viewModel,
                    context = context,
                    data = dayData
                )
            }

        }
    }
}



@Composable
fun TableScreen(viewModel: HomeViewModelContract, context: CoroutineScope, data: List<Pair<ForecastDay, Int>>) {
    val column1Weight = .3f // 30%

    if (data.isNotEmpty()) {
        Row {
            data.indices.forEach {
                TableCell(text = when (it) { // change to dynamic list with order based on current day if time
                    0 -> "Wed"
                    1 -> "Thu"
                    2 -> "Fri"
                    3 -> "Sat"
                    4 -> "Sun"
                    5 -> "Mon"
                    else -> "Tue"
                },
                    weight = column1Weight,
                    index = it,
                    viewModel = viewModel,
                    context = context
                )
            }
        }
        Row {
            data.indices.forEach {
                TableCell(
                    text = "${data[it].second}",
                    weight = column1Weight,
                    index = it,
                    viewModel = viewModel,
                    context = context
                )

            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    index: Int,
    viewModel: HomeViewModelContract,
    context: CoroutineScope
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .clickable {
                context.launch {
                    viewModel.changeDay(index)
                }
            }
            .padding(8.dp),
        fontSize = 10.sp,
        style = TextStyle(
            color = Color(0xFF754855)
        )
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
                painter = painterResource(id = R.drawable.ic_settings_white),
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
