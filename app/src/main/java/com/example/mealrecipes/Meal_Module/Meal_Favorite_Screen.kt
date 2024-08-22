package com.example.mealrecipes.Meal_Module


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@Composable
fun FavoriteScreen(viewModel: MealViewModel = viewModel(), navController: NavHostController) {
    val favoriteMeals by viewModel.favoriteMeals.collectAsState()

    // Background color
    val backgroundColor = Color(0xFF3C3D37)
    // Top bar text background color
    val topBarTextBackgroundColor = Color(0xFF1E201E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(topBarTextBackgroundColor)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentHeight() // Ensures the Box wraps its content height
            ) {
                // Center the text horizontally within the Box
                Text(
                    text = "Favorite Meals",
                    style = TextStyle(
                        fontFamily = MainFont,
                        fontSize = 34.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.Center) // Align the Text in the center of the Box
                        .padding(vertical = 8.dp) // Adjust vertical padding if needed
                )
            }

            if (favoriteMeals.isEmpty()) {
                Text(
                    text = "You haven't added any favorites yet.",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn {
                    items(favoriteMeals) { favorite ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable { navController.navigate("MealDetail/${favorite.name}") },
//                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier

                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = favorite.thumbnail,
                                    contentDescription = favorite.name,
                                    modifier = Modifier
                                        .size(100.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column {
                                    Text(
                                        text = favorite.name,
                                        style = TextStyle(
                                            fontFamily = MainFont,
                                            fontSize = 24.sp,
                                        ),
                                        modifier = Modifier.padding(start = 20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Area: ${favorite.area}",
                                        style = TextStyle(
                                            fontFamily = FourthFont,
                                            fontSize = 14.sp,
                                        ),
                                        modifier = Modifier.padding(start = 20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { viewModel.removeFavorite(favorite.id) },
                                    modifier = Modifier.background(Color.Transparent)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Remove from favorites",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
