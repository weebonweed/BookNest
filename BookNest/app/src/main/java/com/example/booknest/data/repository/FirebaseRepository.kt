package com.example.booknest.data.repository

import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Place
import com.example.booknest.data.model.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
	private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
	private val hotelsRef: DatabaseReference = db.getReference("hotels")
	private val placesRef: DatabaseReference = db.getReference("places")
	private val roomsRef: DatabaseReference = db.getReference("rooms")

	suspend fun fetchHotels(): List<Hotel> {
		val snapshot = hotelsRef.get().await()
		return snapshot.children.mapNotNull { it.getValue(Hotel::class.java)?.copy(id = it.key.orEmpty()) }
	}

	suspend fun fetchRoomsForHotel(hotelId: String): List<Room> {
		val snapshot = roomsRef.orderByChild("hotelId").equalTo(hotelId).get().await()
		return snapshot.children.mapNotNull { it.getValue(Room::class.java)?.copy(id = it.key.orEmpty()) }
	}

	suspend fun fetchPlaces(): List<Place> {
		val snapshot = placesRef.get().await()
		return snapshot.children.mapNotNull { it.getValue(Place::class.java)?.copy(id = it.key.orEmpty()) }
	}

	suspend fun fetchPlace(placeId: String): Place? {
		val snapshot = placesRef.child(placeId).get().await()
		return snapshot.getValue(Place::class.java)?.copy(id = snapshot.key.orEmpty())
	}
}


