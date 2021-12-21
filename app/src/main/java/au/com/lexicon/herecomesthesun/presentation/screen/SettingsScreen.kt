package au.com.lexicon.herecomesthesun.presentation.screen

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.presentation.viewmodel.SettingsViewModelContract
import java.util.*

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModelContract,
    goBack: () -> Unit
) {
    val isClicked = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val value = remember { mutableStateOf("") }

    val time = remember { mutableStateOf("") }
    val timePicker = TimePickerDialog(
            LocalContext.current,
            { _, hours: Int, minutes: Int ->
                time.value = "$hours:${if (minutes.toString().length == 1) "0$minutes" else minutes}"
            }, hour, minute, false
    )
    Scaffold(topBar = {
        SettingsTopBar(color = Color(0xFF75243D)) {
            goBack()
        }
    }) {
        Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
        ) {
            Text("Location", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Image(
                        modifier = Modifier.height(56.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                )
                TextField(
                        value = value.value,
                        modifier = Modifier
                                .height(56.dp)
                                .padding(start = 8.dp),
                        onValueChange = { text ->
                            if (text.length <= 4) {
                                value.value = text
                            }
                        },
                        textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start,
                                lineHeight = 24.sp
                        ),
                        label = { Text("Postcode") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Go,
                                keyboardType = KeyboardType.Number
                        )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Notifications", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.height(64.dp)
            ) {
                Checkbox(
                    checked = isClicked.value,
                    onCheckedChange = {
                        isClicked.value = !isClicked.value
                    }
                )
                Text(
                    text = "Would you like daily efficiency notifications?",
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                )
            }
            if (isClicked.value) {
                Button(onClick = { timePicker.show() }) {
                    Text("Pick time")
                }
                Text("Selected Time: ${time.value}")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_logo_black),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                )
                Text("Here comes the sun doo da doo doo", fontWeight = FontWeight.Bold)
                Text("Adrian")
                Text("Andy")
                Text("Brad")
                Text("Cynthia")
                Text("Edric")
                Text("George")
                Text("Johnny")
                Text("Nicole")
            }
        }
    }
}

@Composable
fun SettingsTopBar(
        color: Color,
        goBack: () -> Unit
) {
    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = color)
                    .padding(horizontal = 24.dp)
    ) {
        Column(
                modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
        ) {
            Image(
                    modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable {
                                goBack()
                            },
                    painter = painterResource(id = R.drawable.ic_arrow_back_white_24dp),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
            )
        }
        Column(
                modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", fontSize = 24.sp, color = Color.White)
        }
        Column(
                modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
        ) {

        }
    }

}
