package com.example.mealrecipes.Meal_Module

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController, viewModel: MealViewModel) {
    val meals by viewModel.meals.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = searchQuery.value,
                onValueChange = { query ->
                    searchQuery.value = query
                    viewModel.searchMeals(query)
                },
                label = { Text("Search Meals") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LazyColumn {
                if (meals.isEmpty() && searchQuery.value.isNotEmpty()) {
                    item {
                        Text(
                            "No meals found",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                } else {
                    items(meals) { meal ->
                        MealItems(
                            meal = meal,
                            viewModel = viewModel,
                            onClick = {
                                // Navigate to the detail screen with the meal ID
                                navController.navigate("MealDetail/${meal.strMeal}")
                            },
                            onFavoriteChanged = { isFavorite ->
                                coroutineScope.launch {
                                    val message = if (isFavorite) {
                                        "Added to favorites"
                                    } else {
                                        "Removed from favorites"
                                    }
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MealItems(
    meal: Meal?,
    viewModel: MealViewModel,
    onClick: (Meal) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit
) {
    meal?.let {
        var isFavorite by remember { mutableStateOf(viewModel.isFavorite(meal.idMeal)) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(it) }
                .padding(8.dp)
        ) {
            AsyncImage(
                model = it.strMealThumb,
                contentDescription = it.strMeal,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = it.strMeal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = it.strCategory,
                    fontSize = 14.sp
                )
            }
            IconButton(
                onClick = {
                    viewModel.toggleFavorite(it)
                    isFavorite = !isFavorite
                    onFavoriteChanged(isFavorite)
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}
