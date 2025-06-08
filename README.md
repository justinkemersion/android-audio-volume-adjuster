# Android Audio Volume Adjuster

A simple Android application to adjust audio levels on your device beyond the stock limits. It offers two main features:

- **Bluetooth Volume Boost**: When connected via Bluetooth, tap the button to boost the audio output.
- **Quiet Mode**: Incrementally lower the volume below the device's minimum to achieve extra quiet playback.

## Requirements

- Android 5.0 (API level 21) or higher
- Android SDK and build tools (Android Studio recommended)

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd android-audio-volume-adjuster
   ```
2. (Optional) Scaffold the project structure:
   ```bash
   ./create_volume_boost_project.sh
   ```
3. Build and install the app on your device:
   ```bash
   cd VolumeBoostApp
   ./gradlew installDebug
   ```

## Usage

1. Launch **Volume Boost** on your device.
2. Grant the **MODIFY_AUDIO_SETTINGS** permission if prompted.
3. Use the **Toggle Volume Boost** button to turn Bluetooth audio boost on or off.
4. Use the system volume buttons to engage Quiet Mode for extra quiet playback.

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.