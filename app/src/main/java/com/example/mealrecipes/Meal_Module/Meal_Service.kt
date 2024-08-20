package com.example.mealrecipes.Meal_Module


import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val Meal_Base_Url = "https://www.themealdb.com/api/json/v1/1/"
interface TheMealService {
    @GET("search.php?s=")
    suspend fun searchMeals(@Query("s") query: String): MealsResponse

    @GET("filter.php?c=")
    suspend fun searchMealsByCategory(@Query("c") category: String): MealsResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php?a=")
    suspend fun searchMealsByArea(@Query("a") area: String): MealsResponse



    companion object {
        private var apiService: TheMealService? = null
        fun getInstance(): TheMealService {
            if (apiService == null) {
                val gson = GsonBuilder().setLenient().create()
                apiService = Retrofit.Builder()
                    .baseUrl(Meal_Base_Url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(TheMealService::class.java)
            }
            return apiService!!
        }
    }
}

