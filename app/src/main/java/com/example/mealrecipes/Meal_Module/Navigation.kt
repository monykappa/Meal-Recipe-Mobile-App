package com.example.mealrecipes.Meal_Module

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun MyApp() {
    val navController = rememberNavController()
    val viewModel: MealViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            Modifier.padding(innerPadding)
        ) {
            composable("Home") { MealScreen(navController, viewModel) }
            composable("Search") { SearchScreen(navController, viewModel) }
            composable("Favorite") { FavoriteScreen(viewModel, navController) }
            // composable("Settings") { SettingsScreen() }
            composable("MealDetail/{mealName}") { backStackEntry ->
                val mealName = backStackEntry.arguments?.getString("mealName") ?: return@composable
                MealDetailScreen(mealName = mealName, navController, viewModel)
            }
        }
    }
}



@Composable
fun BottomNavBar(navController: NavHostController) {
    // Define your custom text style
    val customTextStyle = TextStyle(
        fontFamily = MainFont,
        fontSize = 16.sp
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        val items = listOf("Home", "Search", "Favorite", "Settings")

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = getIcon(screen),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = screen,
                        style = customTextStyle // Apply the custom text style here
                    )
                },
                selected = false, // handle selection state if needed
                onClick = {
                    navController.navigate(screen)
                }
            )
        }
    }
}


@Composable
fun getIcon(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Filled.Home
        "Search" -> Icons.Filled.Search
        "Favorite" -> Icons.Filled.Favorite
        "Settings" -> Icons.Filled.Settings
        else -> Icons.Filled.Home
    }
}


