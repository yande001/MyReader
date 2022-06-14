package com.example.darren.myreader.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.darren.myreader.screens.account.ReaderCreateAccountScreen
import com.example.darren.myreader.screens.details.ReaderBookDetailsScreen
import com.example.darren.myreader.screens.home.HomeScreenViewModel
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
        composable(ReaderScreens.ReaderHomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(ReaderScreens.CreateAccountScreen.name){
            ReaderCreateAccountScreen(navController = navController)
        }
        val detailName = ReaderScreens.DetailsScreen.name
        composable(
            route = "$detailName/{bookId}",
            arguments = listOf(navArgument("bookId"){
                type = NavType.StringType
            })
        ){
            backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                ReaderBookDetailsScreen(navController = navController, bookId = it.toString())
            }
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
        val updateName = ReaderScreens.UpdateScreen.name
        composable(
            route = "$updateName/{googleBookId}",
            arguments = listOf(navArgument("googleBookId"){
                type = NavType.StringType
            })
        ){
            backStackEntry ->
            backStackEntry.arguments?.getString("googleBookId").let {
                ReaderUpdateScreen(navController = navController, googleBookId = it.toString())
            }
        }
    }
}