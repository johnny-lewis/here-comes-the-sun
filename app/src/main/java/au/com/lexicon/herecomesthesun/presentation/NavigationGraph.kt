package au.com.lexicon.herecomesthesun.presentation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import au.com.lexicon.herecomesthesun.presentation.screen.HomeScreen
import au.com.lexicon.herecomesthesun.presentation.screen.SettingsScreen
import au.com.lexicon.herecomesthesun.presentation.viewmodel.HomeViewModel
import au.com.lexicon.herecomesthesun.presentation.viewmodel.SettingsViewModel

class NavigationGraph(
    activity: MainActivity
) {
    private val homeViewModel: HomeViewModel by activity.viewModels()
    private val settingsViewModel: SettingsViewModel by activity.viewModels()

    @Composable
    fun Start(destination: Routes) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = destination.route
        ) {
            addHomeScreen(navController = navController)
            addSettingsScreen(navController = navController)
        }
    }

    private fun NavGraphBuilder.addHomeScreen(navController: NavController) {
        composable(route = Routes.HomeScreen.route) {
            homeViewModel.setSettingsScreen {
                navController.navigate(route = Routes.SettingsScreen.route)
            }
            HomeScreen(
                viewModel = homeViewModel
            )
        }
    }

    private fun NavGraphBuilder.addSettingsScreen(navController: NavController) {
        composable(route = Routes.SettingsScreen.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                goBack = { navController.popBackStack() }
            )
        }
    }

    enum class Routes(val route: String) {
        HomeScreen(route = "HomeScreen"),
        SettingsScreen(route = "SettingsScreen")
    }
}
