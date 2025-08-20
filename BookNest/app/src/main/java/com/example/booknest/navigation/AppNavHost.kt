package com.example.booknest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.booknest.ui.screens.CheckoutScreen
import com.example.booknest.ui.screens.FindRoomScreen
import com.example.booknest.ui.screens.PlaceDetailsScreen
import com.example.booknest.ui.screens.SelectHotelScreen
import com.example.booknest.ui.screens.SelectRoomScreen
import com.example.booknest.ui.screens.SendOtpScreen
import com.example.booknest.ui.screens.SplashScreen
import com.example.booknest.ui.screens.VerifyOtpScreen
import com.example.booknest.ui.screens.Where2GoScreen

@Composable
fun AppNavHost(navController: NavHostController) {
	NavHost(navController = navController, startDestination = NavRoute.Splash.route) {
		composable(NavRoute.Splash.route) {
			SplashScreen(navController)
		}
		composable(NavRoute.SendOtp.route) {
			SendOtpScreen(navController,)
		}
		composable(NavRoute.VerifyOtp.route) {
			VerifyOtpScreen(navController)
		}
		composable(NavRoute.FindRoom.route) {
			FindRoomScreen(navController)
		}
		composable(NavRoute.Where2Go.route) {
			Where2GoScreen(navController)
		}
		composable(NavRoute.SelectHotel.route) {
			SelectHotelScreen(navController)
		}
		composable(NavRoute.SelectRoom.route) {
			SelectRoomScreen(navController)
		}
		composable(NavRoute.Checkout.route) {
			CheckoutScreen(navController)
		}
		composable(NavRoute.PlaceDetails.route) { backStackEntry ->
			val placeId = backStackEntry.arguments?.getString("placeId").orEmpty()
			PlaceDetailsScreen(navController, placeId)
		}
	}
}


