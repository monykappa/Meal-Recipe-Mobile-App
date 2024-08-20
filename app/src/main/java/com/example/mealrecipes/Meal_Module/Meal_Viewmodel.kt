 package com.example.mealrecipes.Meal_Module



import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException



class MealViewModel : ViewModel() {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _favoriteMeals = MutableStateFlow<List<FavoriteMeal>>(emptyList())
    val favoriteMeals: StateFlow<List<FavoriteMeal>> = _favoriteMeals

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _areaMeals = MutableStateFlow<List<Meal>>(emptyList())
    val areaMeals: StateFlow<List<Meal>> = _areaMeals


    private val mealCache = mutableMapOf<String, Meal?>()



    fun fetchMealsByArea(area: String) {
        viewModelScope.launch {
            try {
                val response = TheMealService.getInstance().searchMealsByArea(area)
                _areaMeals.value = response.meals ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
                _areaMeals.value = emptyList()
            }
        }
    }

    // Function to get a cached meal by name
    fun getCachedMealByName(mealName: String): Meal? {
        return mealCache[mealName]
    }


    // Function to fetch a meal by its name and update the cache
    @OptIn(UnstableApi::class) suspend fun fetchMealByName(mealName: String): Meal? {
        return try {
            val response = TheMealService.getInstance().searchMeals(mealName)
            val meal = response.meals?.firstOrNull { it.strMeal == mealName }
            mealCache[mealName] = meal // Cache the meal by name
            meal
        } catch (e: Exception) {
            androidx.media3.common.util.Log.e("MealViewModel", "Error fetching meal by name: ${e.message}")
            null
        }
    }



    init {
        fetchMeals() // Initial fetch
        fetchCategories() // Fetch categories initially
    }

    @OptIn(UnstableApi::class)
    fun toggleFavorite(meal: Meal) {
        val area = meal.strArea ?: "Unknown" // Default value if null
        val favorite = FavoriteMeal(meal.idMeal, meal.strMeal, meal.strMealThumb, area)
        val currentFavorites = _favoriteMeals.value.toMutableList()
        Log.d("MealViewModel", "Toggling favorite for meal: ${meal.strMeal}")
        if (currentFavorites.any { it.id == favorite.id }) {
            Log.d("MealViewModel", "Removing from favorites: ${meal.strMeal}")
            currentFavorites.removeAll { it.id == favorite.id }
        } else {
            Log.d("MealViewModel", "Adding to favorites: ${meal.strMeal}")
            currentFavorites.add(favorite)
        }
        _favoriteMeals.value = currentFavorites
    }



    fun removeFavorite(mealId: String) {
        val currentFavorites = _favoriteMeals.value.toMutableList()
        currentFavorites.removeAll { it.id == mealId }
        _favoriteMeals.value = currentFavorites
    }

    fun isFavorite(mealId: String): Boolean {
        return _favoriteMeals.value.any { it.id == mealId }
    }

    @OptIn(UnstableApi::class)
    fun fetchMeals(query: String = "", category: String = "") {
        viewModelScope.launch {
            try {
                val meals = if (category.isNotEmpty()) {
                    val categoryResponse = TheMealService.getInstance().searchMealsByCategory(category)
                    categoryResponse.meals ?: emptyList()
                } else {
                    val response = TheMealService.getInstance().searchMeals(query)
                    response.meals ?: emptyList()
                }
                _meals.value = meals
                Log.d("MealViewModel", "Fetched meals: $meals")
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
                _meals.value = emptyList()
            }
        }
    }



    // Function to search for meals based on a query
    fun searchMeals(query: String) {
        viewModelScope.launch {
            try {
                if (query.isNotEmpty()) {
                    val response = TheMealService.getInstance().searchMeals(query)
                    _meals.value = response.meals ?: emptyList()
                } else {
                    _meals.value = emptyList()
                }
                _error.value = null // Clear any previous errors
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
                _meals.value = emptyList()
            }
        }
    }


    // Function to fetch meal categories
    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = TheMealService.getInstance().getCategories()
                _categories.value = response.categories
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
        }
    }

}

