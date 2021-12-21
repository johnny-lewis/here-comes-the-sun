package au.com.lexicon.herecomesthesun.presentation.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import au.com.lexicon.herecomesthesun.R
import au.com.lexicon.herecomesthesun.presentation.viewmodel.LaunchViewModelContract
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(
        viewModel: LaunchViewModelContract
) {
    val isRotated = remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
            targetValue = if (isRotated.value) 0f else 360f,
            animationSpec = infiniteRepeatable(
                    tween(
                            durationMillis = 4500,
                            easing = LinearEasing
                    )
            )
    )

    LaunchedEffect(key1 = true) {
        isRotated.value = true
        delay(2000)
        viewModel.goToHomeScreen()
    }

    Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFA52A2A))
                    .rotate(angle)) {
        Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_splash),
                contentDescription = null,
                contentScale = ContentScale.Fit
        )
    }
}