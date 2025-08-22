package com.example.booknest.data.model

data class Hotel(
	val id: String = "",
	val name: String = "",
	val city: String = "",
	val rating: Double = 0.0,
	val pricePerNight: Double = 0.0,
	val imageUrl: String = ""
)

data class Room(
	val id: String = "",
	val hotelId: String = "",
	val type: String = "",
	val capacity: Int = 0,
	val price: Double = 0.0,
	val imageUrl: String = ""
)

data class Place(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val imageUrl: String = ""
)


