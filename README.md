# Beacon Suedtirol - Alto Adige SDK Android
This repository contains the mobile Android SDK that can be used in order to read the beacons installed within the Beacon Suedtirol project.

## Usage
#### 1. If you haven't already, add this to your `project build.gradle` under `repositories`:
```
    maven { url "https://jitpack.io" }
```
#### 2. Add this to your app `build.gradle` file:
```
    implementation 'com.github.noi-techpark:beacon-suedtirol-mobile-sdk-android:0.2.2'
```
#### 3. Create a custom Application class and in the `onCreate()` method, call:
```
    NearbyBeaconManager.initialize();
```
#### 4. If you've received the credentials to use the trusted API, initialize the SDK with the following command instead:
```
    NearbyBeaconManager.initialize(username, password);
```
   
#### 5. In the activity where you want to discover nearby beacons, get an instance of the `NearbyBeaconManager`:
```
    manager = NearbyBeaconManager.getInstance();
```
#### 6. Let your activity implement the `IBeaconListener` and/or `EddystoneListener` interface(s) and pass it to the manager:
```
    manager.setIBeaconListener(this);
    manager.setEddystoneListener(this);
```
#### 7. Implements the methods you need:
```
    onEddystoneDiscovered(Eddystone eddystone)

    onEddystoneLost(Eddystone eddystone)

    onIBeaconDiscovered(IBeacon iBeacon)

    onIBeaconLost(IBeacon iBeacon)
```

# Inbound Licenses
- com.kontaktio:sdk:4.0.2 - Creative Commons Attribution-NoDerivs 3.0 Unported
- io.swagger:swagger-annotations:1.5.15 – Apache 2.0
- com.squareup.okhttp:okhttp:2.7.5 – Apache 2.0
- com.squareup.okhttp:logging-interceptor:2.7.5 – Apache 2.0
- com.squareup:otto:1.3.8 – Apache 2.0
- com.google.code.gson:gson:2.8.2 – Apache 2.0
- org.threeten:threetenbp:1.3.5 – BSD 3-clause
- io.gsonfire:gson-fire:1.8.0 – MIT

