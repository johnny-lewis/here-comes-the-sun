package au.com.lexicon.herecomesthesun.presentation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import au.com.lexicon.herecomesthesun.presentation.screen.HomeScreen
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModel

class NavigationGraph(
    activity: MainActivity
) {
    private val homeViewModel: HomeViewModel by activity.viewModels()

    @Composable
    fun Start(destination: Routes) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = destination.route
        ) {
            addHomeScreen()
        }
    }

    private fun NavGraphBuilder.addHomeScreen() {
        composable(route = Routes.HomeScreen.route) {
            HomeScreen(
                viewModel = homeViewModel
            )
        }
    }

    enum class Routes(val route: String) {
        HomeScreen(route = "HomeScreen")
    }
}
