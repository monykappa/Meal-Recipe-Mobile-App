package com.example.mealrecipes.Meal_Module


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mealrecipes.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(navController: NavHostController, viewModel: MealViewModel = viewModel()) {
    val categories by viewModel.categories.collectAsState(emptyList())
    val meals by viewModel.meals.collectAsState(emptyList())
    val error by viewModel.error.collectAsState()
    val isLoadingMeals by viewModel.isLoadingMeals.collectAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val selectedCategory = remember { mutableStateOf<String?>(null) }
    val areaMeals by viewModel.areaMeals.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchMealsByArea("American")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Brand Logo",
                            modifier = Modifier.size(130.dp)
                        )
                    }
                },
                navigationIcon = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF35374B), // Set your custom color here
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEFAF6))
        ) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                // Carousel of meal images and names
                item {
                    Carousel(meals = areaMeals) { meal ->
                        MealCarouselItem(
                            meal = meal,
                            onClick = { selectedMeal ->
                                navController.navigate("MealDetail/${selectedMeal.strMeal}")
                            }
                        )

                    }
                    Spacer(modifier = Modifier.height(15.dp))

                }

                // Category Buttons
                item {
                    LazyRow {
                        items(categories) { category ->
                            val isSelected = category.strCategory == selectedCategory.value
                            Button(
                                onClick = {
                                    selectedCategory.value = category.strCategory
                                    viewModel.clearMeals() // Clear current meals to provide immediate feedback
                                    viewModel.fetchMeals(category = category.strCategory)
                                },
                                modifier = Modifier.padding(horizontal = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF35374B),  // Set background color
                                    contentColor = Color.White           // Set text color to white
                                )
                            ) {
                                AsyncImage(
                                    model = category.strCategoryThumb,
                                    contentDescription = category.strCategory,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = category.strCategory)
                            }
                        }
                    }
                }


                // Error message
                error?.let {
                    item {
                        Text("Error: $it", color = MaterialTheme.colorScheme.error)
                    }
                }


                // Meal list
                items(meals) { meal ->
                    MealItem(
                        meal = meal,
                        isLoading = isLoadingMeals,
                        viewModel = viewModel,
                        onClick = { selectedMeal ->
                            navController.navigate("MealDetail/${selectedMeal.strMeal}")
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(meals: List<Meal>, itemContent: @Composable (Meal) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { meals.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (meals.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEFAE0))
            ) {
                HorizontalPager(state = pagerState) { page ->
                    itemContent(meals[page])
                }
            }
            DotsIndicator(
                totalDots = meals.size,
                selectedIndex = pagerState.currentPage,
                selectedColor = MaterialTheme.colorScheme.primary,
                unSelectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}


@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,   // Added parameter for selected dot color
    unSelectedColor: Color  // Added parameter for unselected dot color
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(8.dp) // Add padding if needed
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp) // Size of the dots
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) selectedColor else unSelectedColor)
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.width(4.dp)) // Adjust spacing between dots
            }
        }
    }
}




@Composable
fun MealCarouselItem(meal: Meal, onClick: (Meal) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(meal) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEFAF6))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(16.dp)  // Ensures the content is centered and padded
            ) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 20f)  // Adjust aspect ratio as needed
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()  // Ensures the background fills the entire width
                        .background(Color(0xFF35374B))  // Semi-transparent dark background (alpha 50%)
                        .padding(8.dp)  // Add some padding around the text
                ) {
                    Text(
                        text = meal.strMeal,
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White), // Text color black
                        modifier = Modifier.align(Alignment.Center) // Center the text within the Box
                    )
                }
            }
        }
    }
}



@Composable
fun MealItem(
    meal: Meal,
    isLoading: Boolean,
    viewModel: MealViewModel,
    onClick: (Meal) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(viewModel.isFavorite(meal.idMeal)) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(meal) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 16.dp)
                )
            } else {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 16.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Area: ${meal.strArea ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = {
                    viewModel.toggleFavorite(meal)
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
