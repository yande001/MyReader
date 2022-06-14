package com.example.darren.myreader.navigation

import com.example.darren.myreader.screens.home.ReaderHomeScreen

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReaderHomeScreen,
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
            ReaderHomeScreen.name -> ReaderHomeScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            ReaderStatsScreen.name -> ReaderStatsScreen
            null -> ReaderHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")

        }
    }
}