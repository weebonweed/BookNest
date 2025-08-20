package com.example.booknest.navigation

enum class NavRoute(val route: String) {
	Splash("splash"),
	SendOtp("send_otp"),
	VerifyOtp("verify_otp"),
	FindRoom("find_room"),
	SelectHotel("select_hotel"),
	SelectRoom("select_room"),
	Checkout("checkout"),
	Where2Go("where2go"),
	PlaceDetails("place_details/{placeId}");

	companion object {
		fun placeDetails(placeId: String): String = "place_details/$placeId"
	}
}


