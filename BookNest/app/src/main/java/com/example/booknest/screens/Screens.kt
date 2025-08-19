package com.example.booknest.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.booknest.R
import com.example.booknest.data.model.Hotel
import com.example.booknest.data.model.Place
import com.example.booknest.data.model.Room
import com.example.booknest.navigation.NavRoute
import com.example.booknest.ui.components.FullScreenLoading
import com.example.booknest.viewmodel.AuthUiState
import com.example.booknest.viewmodel.AuthViewModel
import com.example.booknest.viewmodel.DataViewModel

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	LaunchedEffect(Unit) {
		if (authViewModel.isLoggedIn()) navController.navigate(NavRoute.FindRoom.route) { popUpTo(NavRoute.Splash.route) { inclusive = true } }
		else navController.navigate(NavRoute.SendOtp.route) { popUpTo(NavRoute.Splash.route) { inclusive = true } }
	}
	FullScreenLoading()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendOtpScreen(
	navController: NavController,
	authViewModel: AuthViewModel = viewModel(),
	it: Activity
) {
	val context = LocalContext.current
	val uiState by authViewModel.uiState.collectAsState()
	var phone by remember { mutableStateOf("") }
	Scaffold(topBar = { TopAppBar(title = { Text("BookNest - Login") }) }) { padding ->
		Column(Modifier.padding(padding).padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
			OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone number") }, modifier = Modifier.fillMaxWidth())
			Button(onClick = {
				{ authViewModel.sendOtp(it, phone) }
			}) { Text("Send OTP") }
			when (uiState) {
				is AuthUiState.Sending -> FullScreenLoading()
				is AuthUiState.CodeSent -> LaunchedEffect(Unit) { navController.navigate(NavRoute.VerifyOtp.route) }
				is AuthUiState.Error -> Text((uiState as AuthUiState.Error).message, color = MaterialTheme.colorScheme.error)
				else -> {}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOtpScreen(navController: NavController, authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	val uiState by authViewModel.uiState.collectAsState()
	var code by remember { mutableStateOf("") }
	Scaffold(topBar = { TopAppBar(title = { Text("Verify OTP") }) }) { padding ->
		Column(Modifier.padding(padding).padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
			OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Enter code") }, modifier = Modifier.fillMaxWidth())
			Button(onClick = { authViewModel.submitOtp(code) }) { Text("Verify") }
			when (uiState) {
				is AuthUiState.Verifying -> FullScreenLoading()
				is AuthUiState.SignedIn -> LaunchedEffect(Unit) {
					navController.navigate(NavRoute.FindRoom.route) { popUpTo(NavRoute.SendOtp.route) { inclusive = true } }
				}
				is AuthUiState.Error -> Text((uiState as AuthUiState.Error).message, color = MaterialTheme.colorScheme.error)
				else -> {}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(navController: NavController, dataViewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	val hotelsState by dataViewModel.hotels.collectAsState()
	Scaffold(topBar = { TopAppBar(title = { Text("Find Room") }, actions = {
		IconButton(onClick = {
			authViewModel.logout()
			navController.navigate(NavRoute.SendOtp.route) { popUpTo(NavRoute.FindRoom.route) { inclusive = true } }
		}) {
			Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Logout")
		}
	}) }) { padding ->
		Column(Modifier.padding(padding)) {
			if (hotelsState.loading) {
				FullScreenLoading()
			} else hotelsState.data?.let { hotels ->
				LazyColumn(modifier = Modifier.fillMaxSize()) {
					items(hotels) { hotel -> HotelRow(hotel) { navController.navigate(NavRoute.SelectHotel.route) } }
				}
			}
			Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
				Button(onClick = { navController.navigate(NavRoute.Where2Go.route) }) { Text("Where2Go") }
				Button(onClick = { navController.navigate(NavRoute.SelectHotel.route) }) { Text("Select Hotel") }
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectHotelScreen(navController: NavController, dataViewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	val hotelsState by dataViewModel.hotels.collectAsState()
	Scaffold(topBar = { TopAppBar(title = { Text("Select Hotel") }) }) { padding ->
		Column(Modifier.padding(padding)) {
			if (hotelsState.loading) FullScreenLoading() else hotelsState.data?.let { hotels ->
				LazyColumn { items(hotels) { hotel -> HotelRow(hotel) { dataViewModel.loadRooms(hotel.id); navController.navigate(NavRoute.SelectRoom.route) } } }
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRoomScreen(navController: NavController, dataViewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	val roomsState by dataViewModel.rooms.collectAsState()
	Scaffold(topBar = { TopAppBar(title = { Text("Select Room") }) }) { padding ->
		Column(Modifier.padding(padding)) {
			if (roomsState.loading) FullScreenLoading() else roomsState.data?.let { rooms ->
				LazyColumn { items(rooms) { room -> RoomRow(room) { navController.navigate(NavRoute.Checkout.route) } } }
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController) {
	Scaffold(topBar = { TopAppBar(title = { Text("Checkout") }) }) { padding ->
		Column(Modifier.padding(padding).padding(16.dp)) {
			Text("Payment summary and confirmation.")
			Button(onClick = { navController.navigate(NavRoute.FindRoom.route) { popUpTo(NavRoute.FindRoom.route) { inclusive = true } } }) { Text("Done") }
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Where2GoScreen(navController: NavController, dataViewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	val placesState by dataViewModel.places.collectAsState()
	Scaffold(topBar = { TopAppBar(title = { Text("Where2Go") }) }) { padding ->
		Column(Modifier.padding(padding)) {
			if (placesState.loading) FullScreenLoading() else placesState.data?.let { places ->
				LazyColumn { items(places) { place -> PlaceRow(place) { navController.navigate(NavRoute.placeDetails(place.id)) } } }
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(navController: NavController, placeId: String, dataViewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
	val state by dataViewModel.selectedPlace.collectAsState()
	LaunchedEffect(placeId) { dataViewModel.loadPlace(placeId) }
	Scaffold(topBar = { TopAppBar(title = { Text("Place Details") }) }) { padding ->
		Column(Modifier.padding(padding).padding(16.dp)) {
			Text("Details for place: $placeId")
		}
	}
}

@Composable
private fun HotelRow(hotel: Hotel, onClick: () -> Unit) {
	Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		Text(hotel.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
		Text("$${hotel.pricePerNight}")
	}
}

@Composable
private fun RoomRow(room: Room, onClick: () -> Unit) {
	Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		Text(room.type, style = MaterialTheme.typography.titleMedium)
		Text("$${room.price}")
	}
}

@Composable
private fun PlaceRow(place: Place, onClick: () -> Unit) {
	Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		Text(place.name, style = MaterialTheme.typography.titleMedium)
		Text("View")
	}
}


