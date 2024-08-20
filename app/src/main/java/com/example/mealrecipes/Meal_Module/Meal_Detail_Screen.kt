package com.example.mealrecipes.Meal_Module

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController



@OptIn(UnstableApi::class) @kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(mealName: String, navController: NavController, viewModel: MealViewModel = viewModel()) {
    val mealState = remember { mutableStateOf<Meal?>(null) }
    val scrollState = rememberScrollState()

    LaunchedEffect(mealName) {
        mealState.value = viewModel.getCachedMealByName(mealName)
        if (mealState.value == null) {
            Log.d("MealDetailScreen", "Meal not found in cache, fetching from API...")
            val meal = viewModel.fetchMealByName(mealName)
            if (meal != null) {
                Log.d("MealDetailScreen", "Meal fetched from API: ${meal.strMeal}")
                mealState.value = meal
            } else {
                Log.d("MealDetailScreen", "Failed to fetch meal from API")
            }
        } else {
            Log.d("MealDetailScreen", "Meal found in cache: ${mealState.value?.strMeal}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = mealState.value?.strMeal ?: "Meal Details", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            if (mealState.value == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    mealState.value?.let { selectedMeal ->
                        AsyncImage(
                            model = selectedMeal.strMealThumb,
                            contentDescription = selectedMeal.strMeal,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 16.dp)
                        )
                        Text(
                            text = selectedMeal.strMeal,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Area: ${selectedMeal.strArea}",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Category: ${selectedMeal.strCategory}",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Instructions:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedMeal.strInstructions ?: "Instructions not available",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ingredients:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        listOf(
                            selectedMeal.strIngredient1 to selectedMeal.strMeasure1,
                            selectedMeal.strIngredient2 to selectedMeal.strMeasure2,
                            selectedMeal.strIngredient3 to selectedMeal.strMeasure3,
                            selectedMeal.strIngredient4 to selectedMeal.strMeasure4,
                            selectedMeal.strIngredient5 to selectedMeal.strMeasure5,
                            selectedMeal.strIngredient6 to selectedMeal.strMeasure6,
                            selectedMeal.strIngredient7 to selectedMeal.strMeasure7,
                            selectedMeal.strIngredient8 to selectedMeal.strMeasure8,
                            selectedMeal.strIngredient9 to selectedMeal.strMeasure9,
                            selectedMeal.strIngredient10 to selectedMeal.strMeasure10,
                            selectedMeal.strIngredient11 to selectedMeal.strMeasure11,
                            selectedMeal.strIngredient12 to selectedMeal.strMeasure12,
                            selectedMeal.strIngredient13 to selectedMeal.strMeasure13,
                            selectedMeal.strIngredient14 to selectedMeal.strMeasure14,
                            selectedMeal.strIngredient15 to selectedMeal.strMeasure15,
                            selectedMeal.strIngredient16 to selectedMeal.strMeasure16,
                            selectedMeal.strIngredient17 to selectedMeal.strMeasure17,
                            selectedMeal.strIngredient18 to selectedMeal.strMeasure18,
                            selectedMeal.strIngredient19 to selectedMeal.strMeasure19,
                            selectedMeal.strIngredient20 to selectedMeal.strMeasure20
                        ).filter { it.first.isNotEmpty() }.forEach { (ingredient, measure) ->
                            Text(
                                text = "$ingredient: $measure",
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        selectedMeal.strSource?.let {
                            Text(
                                text = "Source: $it",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        selectedMeal.strYoutube?.let {
                            Text(
                                text = "YouTube: $it",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
