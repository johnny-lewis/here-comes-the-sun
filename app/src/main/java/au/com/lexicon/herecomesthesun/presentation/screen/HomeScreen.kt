package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModelContract

@Composable
fun HomeScreen(
    viewModel: HomeViewModelContract
) {
    val message by viewModel.messageFlow.collectAsState(initial = "")

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
