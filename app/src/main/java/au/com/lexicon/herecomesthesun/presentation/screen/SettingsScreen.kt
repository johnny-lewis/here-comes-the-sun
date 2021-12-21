package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.presentation.viewmodel.SettingsViewModelContract

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModelContract,
    goBack: () -> Unit
) {
    val isClicked = remember { mutableStateOf(false) }

    Scaffold(topBar = {
        SettingsTopBar(color = Color.Gray) {
            goBack()
        }
    }) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Column {
                Text("Location")
                TextField(value = "", onValueChange = {})
            }
            Column {
                Text("Notification")
                Row {
                    Checkbox(
                        checked = isClicked.value,
                        onCheckedChange = {
                            isClicked.value = !isClicked.value
                        }
                    )
                    Text(
                        text = "Would you like daily efficiency notifications?",
                        modifier = Modifier.height(48.dp),
                        textAlign = TextAlign.Center
                    )
                }
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
                    painter = painterResource(id = R.drawable.ic_arrow_back_black_24dp),
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
            Text("Settings")
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
