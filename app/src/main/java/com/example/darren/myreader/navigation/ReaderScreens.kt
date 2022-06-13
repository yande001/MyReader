package com.example.darren.myreader.navigation

import com.example.darren.myreader.screens.home.ReaderHomeScreen

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReaderHoneScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    ReaderStatsScreen;
    companion object{
        fun fromRoute(route: String?): ReaderScreens
        = when(route?.substringBefore("/"))
        {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            ReaderHoneScreen.name -> ReaderHoneScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            ReaderStatsScreen.name -> ReaderStatsScreen
            null -> ReaderHoneScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")

        }
    }
}