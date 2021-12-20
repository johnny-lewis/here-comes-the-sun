package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModelContract

@Composable
fun HomeScreen(
    viewModel: HomeViewModelContract
) {
    val message by viewModel.messageFlow.collectAsState(initial = "")
    var tabIndex by remember { mutableStateOf(0)}

    Scaffold(topBar = {
        TopBar { viewModel.goToSettingsScreen() }
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
fun TopBar(
    navigateToSettings: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //insert logo
        }
        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //insert settings
            Image(
                modifier = Modifier.clickable {
                    navigateToSettings()
                },
                painter = painterResource(id = R.drawable.outline_settings_black),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }

}
