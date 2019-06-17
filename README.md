# Beacon Suedtirol - Alto Adige SDK Android
This repository contains the mobile Android SDK that can be used in order to read the beacons installed within the Beacon Suedtirol project.

## Integration in your Android App

**URL:** s3://it.bz.opendatahub/release
**Group ID:** it.bz.beacon
**Artifact ID:** sdk

For the configuration in the Gradle file you can use the following example:
```
repositories {
    maven {
        url "s3://it.bz.opendatahub/release"
    }
    mavenLocal()
    jcenter()
}
compile('it.bz.beacon:sdk:0.2.2')
```

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
