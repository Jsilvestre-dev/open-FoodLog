# No Calorie Left Behind

No Calorie Left Behind is a macronutrient and calorie tracking Android application designed to make it easy to monitor daily nutrition goals at a glance.

Users can configure custom calorie and macronutrient targets and log food entries throughout the day while tracking their progress through a clean and responsive interface. The app is built using modern Android development practices including Jetpack Compose for UI, Room for local persistence, and reactive state management using Kotlin Coroutines and Flow.

## Motivation

Many nutrition tracking apps are complex and cluttered.
This project was built to explore a simpler approach to macro tracking with a clean UI and fast logging workflow.

## Key Concepts Demonstrated

- Modern Android UI with Jetpack Compose
- MVVM architecture and reactive state management
- Local persistence with Room
- Offline-first mobile application design
- Modular architecture supporting future Kotlin Multiplatform integration

## Screenshots

<img width="180" height="390" alt="Image" src="https://github.com/user-attachments/assets/6d459a35-ad56-435a-834c-75a24250865c" />

<img width="180" height="390" alt="Image" src="https://github.com/user-attachments/assets/f9c465fc-87c8-459d-82ca-53fe452adce7" />

<img width="180" height="390" alt="Image" src="https://github.com/user-attachments/assets/a732556f-3dfa-42d2-90cd-e6f52b77c2ee" />

<img width="180" height="390" alt="Image" src="https://github.com/user-attachments/assets/e9b5cc92-8625-4ccc-ba8d-82f3f923d630" />

## Features

- Customizable daily goals for calories, protein, carbohydrates, and fats
- Quick food logging with calorie and macronutrient tracking
- At-a-glance dashboard showing progress toward daily nutrition targets
- Local data persistence for reliable offline usage
- Dynamic theming based on the device's system theme

## Tech Stack

- **Kotlin**: primary language used for the application
- **Jetpack Compose**: declarative UI toolkit for building the interface
- **Room Database**: local persistence layer for nutrition data
- **DataStore**: storage for user preferences and configuration
- **Kotlin Coroutines & Flow**: asynchronous programming and reactive state management

## Architecture

The application follows a modern Android architecture centered around MVVM principles.

- **Room** is used as the primary data source for storing nutrition entries and user preferences.
- **DataStore** manages configuration settings such as nutrition targets.
- **Jetpack Compose** provides a reactive UI that updates automatically when the underlying data changes.
- **Coroutines and Flow** handle asynchronous operations and allow the UI to react to changes in the database.

## Running the Project

1. Install Android Studio.
2. Clone the repository:
3. Open the project in Android Studio.
4. Allow Gradle to sync.
5. Run the app on an Android emulator or device.

### Requirements

- Android Studio
- Android SDK 33+

## Future Improvements

- Kotlin Multiplatform integration to enable an iOS version of the app
- Improvements to ViewModel and data layer architecture
- Support for saving custom recipes
- Serving size calculations for logged foods
- Expanded nutrition analytics and historical tracking

