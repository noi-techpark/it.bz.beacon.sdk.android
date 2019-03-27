# beacon-suedtirol-mobile-sdk-android
This repository contains the mobile Android SDK that can be used in order to read the beacon installed within the Beacon Suedtirol project.


##Usage
1. move the .aar release file to ./libs folder
2. Add this to your build.gradle file:


    implementation fileTree(dir: 'libs', include: ['*.aar'])
    
3. Create a custom Application class and then, in the onCreate() method, call:


    NearbybeaconManager.initialize();
    
4. If you've received the credentials to use the trusted API, initialize the SDK with the following command instead:


    NearbybeaconManager.initialize(String username, String password);
