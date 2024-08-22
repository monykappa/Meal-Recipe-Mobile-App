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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.text.AnnotatedString

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController



@OptIn(UnstableApi::class) @kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(mealName: String, navController: NavController, viewModel: MealViewModel = viewModel()) {
    val mealState = remember { mutableStateOf<Meal?>(null) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                title = {
                    Text(
                        text = mealState.value?.strMeal ?: "Meal Details",
                        color = Color(0xFFECDFCC),
                        style = TextStyle(
                            fontFamily = ThirdFont,
                            fontSize = 24.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E201E) // Set background color of top bar
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF3C3D37)) // Set background color of the screen
                .padding(paddingValues)
        ) {
            if (mealState.value == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF3C3D37)), // Match background color
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
                        .verticalScroll(scrollState)
                        .padding(10.dp)
                ) {
                    mealState.value?.let { selectedMeal ->
                        AsyncImage(
                            model = selectedMeal.strMealThumb,
                            contentDescription = selectedMeal.strMeal,
                            modifier = Modifier
                                .fillMaxWidth() // Make the image full width
                                .height(300.dp) // Set a fixed height for the image
                                .padding(0.dp), // No padding around the image
                            contentScale = ContentScale.Crop // Crop to fit the container
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // Adjusted spacing for better layout

                        Text(
                            text = selectedMeal.strMeal,
                            style = TextStyle(
                                fontFamily = ThirdFont,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFECDFCC), // Set text color
                                textAlign = TextAlign.Center // Center the text
                            ),
                            modifier = Modifier.fillMaxWidth() // Ensure text takes full width for centering
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Area: ${selectedMeal.strArea}",
                            style = TextStyle(
                                fontFamily = FourthFont, // Use FourthFont for this text
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFECDFCC) // Set text color
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Category: ${selectedMeal.strCategory}",
                            style = TextStyle(
                                fontFamily = FourthFont,
                                fontSize = 16.sp,
                                color = Color(0xFFECDFCC) // Set text color
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Instructions:",
                            style = TextStyle(
                                fontFamily = MainFont,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFECDFCC) // Set text color
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedMeal.strInstructions ?: "Instructions not available",
                            style = TextStyle(
                                fontFamily = FourthFont,
                                fontSize = 16.sp,
                                color = Color(0xFFb0b4b7) // Set text color
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ingredients:",
                            style = TextStyle(
                                fontFamily = MainFont,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFECDFCC) // Set text color
                            )
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
                                style = TextStyle(
                                    fontFamily = FourthFont,
                                    fontSize = 16.sp,
                                    color = Color(0xFFb0b4b7) // Set text color
                                ),
                                modifier = Modifier.padding(vertical = 4.dp) // Add vertical padding to separate items
                            )
                            Divider(
                                color = Color(0xFFb0b4b7),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        selectedMeal.strSource?.let { source ->
                            if (source.isNotEmpty()) { // Check if source is not empty
                                Text(
                                    text = "Sources:",
                                    style = TextStyle(
                                        fontFamily = MainFont,
                                        fontSize = 28.sp,
                                        color = Color(0xFFECDFCC) // Set text color
                                    )
                                )

                                ClickableText(

                                    text = AnnotatedString("$source"),
                                    style = TextStyle(
                                        fontFamily = FourthFont,
                                        fontSize = 16.sp,
                                        color = Color(0xFFb0b4b7)
                                    ),
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Log.e("MealDetailScreen", "Error opening URL: $source", e)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        selectedMeal.strYoutube?.let { youtube ->
                            if (youtube.isNotEmpty()) { // Check if YouTube URL is not empty
                                val videoId = Uri.parse(youtube).getQueryParameter("v")
                                if (videoId != null) {
                                    AndroidView(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp), // Adjust height as needed
                                        factory = { context ->
                                            WebView(context).apply {
                                                webViewClient = WebViewClient()
                                                webChromeClient = WebChromeClient()
                                                settings.javaScriptEnabled = true
                                                settings.domStorageEnabled = true
                                                loadUrl(youtube) // Load the full YouTube URL
                                            }
                                        }
                                    )
                                } else {
                                    Text("Invalid YouTube URL")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun IngredientItem(ingredient: String, measure: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$ingredient: $measure",
            style = TextStyle(
                fontFamily = FourthFont,
                fontSize = 16.sp,
                color = Color(0xFFb0b4b7) // Set text color
            ),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Adjust space before the dot
        DotDivider() // Add the dot divider
    }
}

@Composable
fun DotDivider() {
    Box(
        modifier = Modifier
            .size(4.dp)
            .background(Color(0xFFb0b4b7), shape = CircleShape)
    )
}
