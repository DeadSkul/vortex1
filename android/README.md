# GeoPulse Android

GeoPulse is a weather probability app that shows historical odds of exceeding comfort thresholds using NASA POWER climate data.

## Features

- 🌍 Interactive map for location selection
- 📅 Date picker for any day of the year
- 🎯 Comfort presets or custom thresholds
- 📊 Historical weather odds visualization
- 🛰️ NASA POWER API integration

## Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 26 or higher
- Google Maps API Key

### Configuration

1. Get a Google Maps API Key:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Enable Maps SDK for Android
   - Create an API key

2. Add your API key to `secrets.properties`:
   ```
   MAPS_API_KEY=your_actual_api_key_here
   ```

### Building

1. Open the project in Android Studio
2. Sync Gradle files
3. Run on an emulator or physical device

## Architecture

- **Jetpack Compose** - Modern Android UI toolkit
- **Material 3** - Latest Material Design components
- **Navigation Compose** - Type-safe navigation
- **Google Maps Compose** - Maps integration
- **Retrofit** - REST API client
- **ViewModel** - State management

## API

This app uses the NASA POWER API to fetch historical climate data:
- Temperature (max/min)
- Precipitation
- Wind speed

Data range: 1981-2020

## License

Educational project for NASA Space Apps Challenge
