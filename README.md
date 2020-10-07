# Beacon Suedtirol - Alto Adige SDK Android
This repository contains the mobile Android SDK that can be used in order to read the beacons installed within the Beacon Suedtirol project.

## Usage

1. If you haven't already done it, add this line to your `project build.gradle` under `repositories`: 
	```
   maven { url "https://jitpack.io" }
   ```
   ...or...
   ```
   maven {
      url "s3://it.bz.opendatahub/release"
      authentication {
            awsIm(AwsImAuthentication)
      }
   }
   ```
2. Add this line to your app's `build.gradle` file (look for latest release in repository and replace "x.y.z" properly):
   ```
   implementation 'com.github.noi-techpark:beacon-suedtirol-mobile-sdk-android:x.y.z'
   ```
3. Create a custom Application class and in the `onCreate()` method, call:
   ```
   NearbyBeaconManager.initialize(this);
   ```
   
4. If you've received the credentials to use the trusted API, initialize the SDK with the following command instead:
   ```
    NearbyBeaconManager.initialize(username, password);
	```
5. In the activity where you want to discover nearby beacons, get an instance of the `NearbyBeaconManager`:
   ```
   manager = NearbyBeaconManager.getInstance();
   ```
6. Let your activity implement the `IBeaconListener` and/or `EddystoneListener` interface(s) and pass it to the manager:
   ```
   manager.setIBeaconListener(this);
   manager.setEddystoneListener(this);
   ```
7. Implements the methods you need:
   ```
    onEddystoneDiscovered(Eddystone eddystone)

    onEddystoneLost(Eddystone eddystone)

    onIBeaconDiscovered(IBeacon iBeacon)

    onIBeaconLost(IBeacon iBeacon)
	```
### Start the beacon's scan:
``` gfm
NearbyBeaconManager.getInstance().startScanning();
```

### Permissions for Bluetooth
It is important to grant android users the permission to access bluetooth
and the Internet connection; to do so, add the following lines to file
`AndroidManifest.xml`, located in your project's `scr/main`  directory.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.BLUETOOTH" />
```

An example of this file is:
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest
	xmlns:android="http://schemas.android.com/apk/res/android
		<http://schemas.android.com/apk/res/android>"
	xmlns:tools="http://schemas.android.com/tools
		<http://schemas.android.com/tools>"
    package="com.example.project">
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<application android:allowBackup="true" 
	android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
	android:launchMode="singleInstance"
	android:roundIcon="@mipmap/ic_launcher_round"
	android:supportsRtl="true"        
	android:theme="@style/AppTheme">
		<activity android:name=".MainActivity">
			<intent-filter>
				<action android:name="android.intent.action.MAIN" />
				<category android:name="android.intent.category.LAUNCHER" />
			</intent-filter>
		</activity>
	...
	</application>
</manifest>
```

## Inbound Licenses
- com.kontaktio:sdk:4.0.2 - Creative Commons Attribution-NoDerivs 3.0
  Unported
- io.swagger:swagger-annotations:1.5.15 – Apache 2.0
- com.squareup.okhttp:okhttp:2.7.5 – Apache 2.0
- com.squareup.okhttp:logging-interceptor:2.7.5 – Apache 2.0
- com.squareup:otto:1.3.8 – Apache 2.0
- com.google.code.gson:gson:2.8.2 – Apache 2.0
- org.threeten:threetenbp:1.3.5 – BSD 3-clause
- io.gsonfire:gson-fire:1.8.0 – MIT

## Outbound License
See [LICENSE](./LICENSE.md).
