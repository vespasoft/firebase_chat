Architecture pattern: Ive decided implement MVVM because of the size of the app, so the app is 
decouple in two main layers: model and view model.

I made sure to extract third parties dependencies from the view model layer, and encapsulate the origin 
of the data logic in the model layer.

I used Hilt to implement dependency injection pattern but later I realize that it would be better use 
koin to reuse the code in a KMM app, that's way I used ktor as a HTTP client instead use retrofit.

Possible improvements that I'd like to implement in the future: 
 * Encrypt messages data in the firebase database using AES-256
 * Implement Koin as dependency injection framework
 * Implement login with google button an get profile image of the user.
 * Send images in the chat and save then in firebase storage.
 * Create iOS version using swiftUI.
