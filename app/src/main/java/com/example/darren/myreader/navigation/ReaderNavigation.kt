package com.example.darren.myreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.darren.myreader.screens.account.ReaderCreateAccountScreen
import com.example.darren.myreader.screens.details.ReaderBookDetailsScreen
import com.example.darren.myreader.screens.home.ReaderHomeScreen
import com.example.darren.myreader.screens.login.ReaderLoginScreen
import com.example.darren.myreader.screens.search.BookSearchViewModel
import com.example.darren.myreader.screens.search.ReaderSearchScreen
import com.example.darren.myreader.screens.splash.ReaderSplashScreen
import com.example.darren.myreader.screens.stats.ReaderStatsScreen
import com.example.darren.myreader.screens.update.ReaderUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name
    ){
        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHoneScreen.name){
            ReaderHomeScreen(navController = navController)
        }
        composable(ReaderScreens.CreateAccountScreen.name){
            ReaderCreateAccountScreen(navController = navController)
        }
        composable(ReaderScreens.DetailsScreen.name){
            ReaderBookDetailsScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name){
            val viewModel = hiltViewModel<BookSearchViewModel>()
            ReaderSearchScreen(navController = navController, viewModel = viewModel)
        }
        composable(ReaderScreens.ReaderStatsScreen.name){
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreens.UpdateScreen.name){
            ReaderUpdateScreen(navController = navController)
        }
    }
}