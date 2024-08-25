package com.example.mealrecipes.Meal_Module


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.mealrecipes.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen() {
    val snackbarHostState = remember { SnackbarHostState() }

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E201E),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF3C3D37)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = rememberImagePainter("https://t3.ftcdn.net/jpg/03/12/05/84/360_F_312058427_XcbdLYQcFpGhBPnBlFSGv5c6JQ3mEuQc.jpg"),
                contentDescription = "About Us Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About Us",
                style = TextStyle(
                    fontFamily = MainFont,
                    fontSize = 32.sp,
                    color = Color(0xFFECDFCC)
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Welcome to our mobile meal recipes application! We are thrilled to offer you a comprehensive platform where you can explore a diverse range of meal recipes from various cuisines around the world. Our application is designed to cater to both novice and experienced cooks, providing an easy and intuitive way to discover, save, and share your favorite recipes. Whether you're looking to try a new dish, perfect a classic recipe, or find inspiration for your next meal, our app is here to help.\n\nOur app features a user-friendly interface that allows you to browse through an extensive collection of recipes, categorized by meal type, ingredient, cuisine, and more. Each recipe includes detailed instructions, ingredient lists, and nutritional information to ensure you have everything you need to cook with confidence.\n\nIn addition to browsing and searching, you can save your favorite recipes to a personal collection for easy access later. Sharing recipes with friends and family is a breeze, as you can easily send your favorite dishes via social media or email.\n\nWe understand that cooking is not just about following recipes; it's about creating memorable experiences and enjoying the process. That's why we strive to offer features that make cooking fun and rewarding. From instructional videos and cooking tips to interactive features that let you customize recipes, our app aims to enhance your culinary journey.\n\nOur team is committed to continuously improving and expanding the app, adding new features, and incorporating user feedback. We value your input and encourage you to reach out with any suggestions or questions. Your satisfaction is our priority, and we are dedicated to providing you with the best possible experience.\n\nThank you for choosing our meal recipes application. We hope it becomes an invaluable tool in your kitchen, helping you create delicious meals and explore the world of cooking in new and exciting ways.",
                    style = TextStyle(
                        fontFamily = FourthFont,
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Romdoul Flower: The Romdoul flower is a symbol of beauty and cultural significance in Cambodia. Known for its delicate, sweet fragrance and striking appearance, the Romdoul is often regarded as one of Cambodia's national flowers. It blooms in a variety of colors, including white, pink, and yellow, and is celebrated for its role in traditional Cambodian ceremonies and festivals.\n\nThe Romdoul flower holds a deep connection to Cambodian heritage and is frequently featured in local art, crafts, and floral arrangements. Its enchanting aroma is said to represent purity and elegance, making it a cherished element in cultural rituals and celebrations. In addition to its cultural importance, the Romdoul is also appreciated for its aesthetic appeal, with its vibrant petals and graceful form adding a touch of natural beauty to any setting.\n\nIn Cambodian tradition, the Romdoul is often associated with positive attributes and is believed to bring good fortune and prosperity. It plays a role in various ceremonies, including weddings, religious events, and community gatherings. The flower's presence in these occasions reflects its esteemed status and the reverence with which it is held by the Cambodian people.\n\nBeyond its cultural significance, the Romdoul flower also represents the rich biodiversity of Cambodia's flora. As a native species, it contributes to the country's natural heritage and highlights the importance of preserving and appreciating local plant life. The Romdoul's beauty and symbolism make it a fitting representation of Cambodia's vibrant culture and natural environment.",
                    style = TextStyle(
                        fontFamily = FourthFont,
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
