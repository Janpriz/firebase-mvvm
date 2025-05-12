package com.dang.boswos_firebase.navigation

import ViewUploadScreen
//import com.dang.boswos_firebase.ui.theme.screens.profiles.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dang.boswos_firebase.ui.theme.screens.StartScreen
import com.dang.boswos_firebase.ui.theme.screens.home.Homescreen

import com.dang.boswos_firebase.ui.theme.screens.login.LoginScreen
import com.dang.boswos_firebase.ui.theme.screens.products.AddProductsScreen
//import com.dang.boswos_firebase.ui.theme.screens.products.AddProductsScreen
import com.dang.boswos_firebase.ui.theme.screens.products.UpdateProductsScreen
import com.dang.boswos_firebase.ui.theme.screens.products.UploadScreen
import com.dang.boswos_firebase.ui.theme.screens.products.ViewProductsScreen
import com.dang.boswos_firebase.ui.theme.screens.profiles.ProfileScreen
//import com.dang.boswos_firebase.ui.theme.screens.products.ViewUploadsScreen
import com.dang.boswos_firebase.ui.theme.screens.register.RegisterScreen



@Composable
fun AppNavHost(modifier: Modifier=Modifier,
               navController:NavHostController= rememberNavController(),
               startDestination:String= ROUTE_START) {

    NavHost(navController = navController,
        modifier=modifier,
        startDestination = startDestination )
    {
        composable(ROUTE_START){
            StartScreen(navController)
        }
        composable(ROUTE_LOGIN){
            LoginScreen(navController)
        }
        composable(ROUTE_REGISTER){
            RegisterScreen(navController)
        }

        composable(ROUTE_HOME){
            Homescreen(navController)
        }
        composable(ROUTE_ADD_PRODUCT) {
            AddProductsScreen(navController)
        }
        composable(ROUTE_VIEW_PRODUCT){
            ViewProductsScreen(navController)
        }
        composable(ROUTE_UPDATE_PRODUCT+ "/{id}"){passedData ->
            UpdateProductsScreen(navController,passedData.arguments?.getString("id")!!)
        }
        composable(ROUTE_VIEW_UPLOAD){
            ViewUploadScreen(navController.toString())
        }
        composable(ROUTE_PROFILE) {
            ProfileScreen(navController)
        }
        composable(ROUTE_UPLOAD) {
            UploadScreen(navController)
        }

    }
//    Scaffold(
//        bottomBar = {
//            BottomNavBar(navController)
//        }
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = "home",
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable(ROUTE_HOME) {
//                HomeScreen(navController)
//            }
//
//            composable(ROUTE_PROFILE) {
//                ProfileScreen()
//            }
//        }
//    }

}
//package com.dang.boswos_firebase.navigation
//
//import BottomNavBar
//import ProfileScreen
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.dang.boswos_firebase.ui.theme.screens.home.HomeScreen
//import com.dang.boswos_firebase.ui.theme.screens.login.LoginScreen
//import com.dang.boswos_firebase.ui.theme.screens.products.AddProductsScreen
//import com.dang.boswos_firebase.ui.theme.screens.products.UpdateProductsScreen
//import com.dang.boswos_firebase.ui.theme.screens.products.ViewProductsScreen
//import com.dang.boswos_firebase.ui.theme.screens.products.ViewUploadsScreen
//import com.dang.boswos_firebase.ui.theme.screens.register.RegisterScreen
//
//// Define your route constants
//
//
//@Composable
//fun AppNavHost(modifier: Modifier = Modifier,
//               navController: NavHostController = rememberNavController(),
//               startDestination:String= ROUTE_LOGIN) {
//    // Wrap everything in a Scaffold to provide BottomNavigationBar at the bottom
//    Scaffold(
//        bottomBar = {
//            BottomNavBar(navController)
//        }
//    ) { paddingValues ->
//        // Define a single NavHost to handle all routes
//        NavHost(
//            navController = navController,
//            startDestination = startDestination,  // Start at login screen
//            modifier = modifier.padding(paddingValues)
//        ) {
//            // Define all the composables and routes in one place
//
//            composable(ROUTE_LOGIN) {
//                LoginScreen(navController)
//            }
//
//            composable(ROUTE_REGISTER) {
//                RegisterScreen(navController)
//            }
//
//            composable(ROUTE_HOME) {
//                HomeScreen(navController)
//            }
//
//            composable(ROUTE_ADD_PRODUCT) {
//                AddProductsScreen(navController)
//            }
//
//            composable(ROUTE_VIEW_PRODUCT) {
//                ViewProductsScreen(navController)
//            }
//
//            composable("$ROUTE_UPDATE_PRODUCT/{id}") { backStackEntry ->
//                val productId = backStackEntry.arguments?.getString("id") ?: ""
//                UpdateProductsScreen(navController, productId)
//            }
//
//            composable(ROUTE_VIEW_UPLOAD) {
//                ViewUploadsScreen(navController)
//            }
//
//            composable(ROUTE_PROFILE) {
//                ProfileScreen()
//            }
//        }
//    }
//}
