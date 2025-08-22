package com.example.booknest.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

sealed interface AuthUiState {
	data object Idle : AuthUiState
	data object Sending : AuthUiState
	data object CodeSent : AuthUiState
	data object Verifying : AuthUiState
	data object SignedIn : AuthUiState
	data class Error(val message: String) : AuthUiState
}

class AuthViewModel : ViewModel() {
	private val auth: FirebaseAuth = FirebaseAuth.getInstance()
	private var verificationId: String? = null

	private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
	val uiState: StateFlow<AuthUiState> = _uiState

	fun isLoggedIn(): Boolean = auth.currentUser != null

	fun sendOtp(activity: Activity, phoneNumber: String) {
		_uiState.value = AuthUiState.Sending
		val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			override fun onVerificationCompleted(credential: PhoneAuthCredential) {
				verifyWithCredential(credential)
			}

			override fun onVerificationFailed(e: FirebaseException) {
				_uiState.value = AuthUiState.Error(e.localizedMessage ?: "Verification failed")
			}

			override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
				this@AuthViewModel.verificationId = verificationId
				_uiState.value = AuthUiState.CodeSent
			}
		}

		val options = PhoneAuthOptions.newBuilder(auth)
			.setPhoneNumber(phoneNumber)
			.setTimeout(60L, TimeUnit.SECONDS)
			.setActivity(activity)
			.setCallbacks(callbacks)
			.build()
		PhoneAuthProvider.verifyPhoneNumber(options)
	}

	fun submitOtp(code: String) {
		val id = verificationId ?: run {
			_uiState.value = AuthUiState.Error("No verification id")
			return
		}
		_uiState.value = AuthUiState.Verifying
		val credential = PhoneAuthProvider.getCredential(id, code)
		verifyWithCredential(credential)
	}

	private fun verifyWithCredential(credential: PhoneAuthCredential) {
		viewModelScope.launch {
			auth.signInWithCredential(credential)
				.addOnSuccessListener { _uiState.value = AuthUiState.SignedIn }
				.addOnFailureListener { e -> _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Sign in failed") }
		}
	}

	fun logout() {
		auth.signOut()
		_uiState.value = AuthUiState.Idle
	}
}


