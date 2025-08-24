This app is build using Kotlin and Jetpack Compose. The backend will be handled by Firebase, specifically for user authentication and the database.

Here's a breakdown of the project :

Project Setup

Project Name: BookNest 

Technology: Jetpack Compose 

SDk used : Android studio

Add Dependencies: Include all necessary dependencies in your build.gradle file. 

Firebase Integration:

Enable : Firebase Authentication (specifically phone number authentication). 

Set up : Firebase Realtime Database to store and retrieve hotel and place information. 


App Screens and UI breakkdown :

1.Splash Screen 


2.Send OTP Screen 


3.Verify OTP Screen 


4.Find Room Screen 


5.Select Hotel Screen 


6.Select Room Screen 


7.Checkout Screen 


8.Where2Go Screen 


9.Place Details Screen 

_____________________________________________________________________________________________________________________________________________________________________________________________________________________________

# Core Functionality

User Authentication: Implemented complete phone authentication flow where a user can enter their phone number, receive an OTP from Firebase, and verify it to log in. 

Data Management: Fetch hotel and place data from the Firebase Realtime Database to display in the app. 

App Navigation and Workflow :

The user flow should follow this specific navigation path:

->The app opens with a Splash Screen.

->It proceeds to the Send OTP screen where the user inputs their phone number.

->The user is then taken to the Verify OTP screen.

->A Loading Animation should be displayed while waiting for the OTP.

->Firebase Authentication handles the OTP sending and verification process.

->If verification is successful, the user lands on the Find Room screen.

->From here, the user can also navigate to the Where2Go screen, which leads to the Place Details screen.

->The user has an option to Logout, which would take them back to the authentication flow.

->From the Find Room screen, after selecting all options, the user proceeds to the Select Hotel screen.

->Next is the Select Room screen.

->Finally, the user reaches the Checkout screen.

The data for the hotel and place details is fetched from the Firebase Realtime Database. 
