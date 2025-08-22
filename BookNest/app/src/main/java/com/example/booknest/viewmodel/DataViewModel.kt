package com.example.booknest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Place
import com.example.booknest.data.model.Room
import com.example.booknest.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DataState<T>(val loading: Boolean = false, val data: T? = null, val error: String? = null)

class DataViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
	private val _hotels = MutableStateFlow(DataState<List<Hotel>>(loading = true))
	val hotels: StateFlow<DataState<List<Hotel>>> = _hotels

	private val _places = MutableStateFlow(DataState<List<Place>>(loading = true))
	val places: StateFlow<DataState<List<Place>>> = _places

	private val _rooms = MutableStateFlow(DataState<List<Room>>(loading = false))
	val rooms: StateFlow<DataState<List<Room>>> = _rooms

	private val _selectedPlace = MutableStateFlow(DataState<com.example.booknest.data.model.Place?>(loading = false))
	val selectedPlace: StateFlow<DataState<com.example.booknest.data.model.Place?>> = _selectedPlace

	init {
		refreshHotels()
		refreshPlaces()
	}

	fun refreshHotels() {
		viewModelScope.launch {
			try {
				_hotels.value = DataState(loading = true)
				_hotels.value = DataState(data = repository.fetchHotels())
			} catch (e: Exception) {
				_hotels.value = DataState(error = e.localizedMessage)
			}
		}
	}

	fun refreshPlaces() {
		viewModelScope.launch {
			try {
				_places.value = DataState(loading = true)
				_places.value = DataState(data = repository.fetchPlaces())
			} catch (e: Exception) {
				_places.value = DataState(error = e.localizedMessage)
			}
		}
	}

	fun loadRooms(hotelId: String) {
		viewModelScope.launch {
			try {
				_rooms.value = DataState(loading = true)
				_rooms.value = DataState(data = repository.fetchRoomsForHotel(hotelId))
			} catch (e: Exception) {
				_rooms.value = DataState(error = e.localizedMessage)
			}
		}
	}

	fun loadPlace(placeId: String) {
		viewModelScope.launch {
			try {
				_selectedPlace.value = DataState(loading = true)
				_selectedPlace.value = DataState(data = repository.fetchPlace(placeId))
			} catch (e: Exception) {
				_selectedPlace.value = DataState(error = e.localizedMessage)
			}
		}
	}
}


