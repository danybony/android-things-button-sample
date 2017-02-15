Button and LED (and Firebase) codelab for Android Things
========================================

This is a codelab based on the [Android Things Button Sample](https://github.com/androidthings/sample-button).
It demonstrates how to use a button input UserDriver to listen to GPIO pin changes, generate and listen for key events, change the state of an LED accordingly and synchronise with Firebase.


Pre-requisites
--------------

- Android Things compatible board
- Android Studio 2.2+
- [Rainbow Hat for Android Things](https://shop.pimoroni.com/products/rainbow-hat-for-android-things) or the following individual components:
    - 2 LED
    - 2 push button
    - 4 resistors
    - jumper wires
    - 1 breadboard

Schematics
----------

If you have the Raspberry Pi [Rainbow Hat for Android Things](https://shop.pimoroni.com/products/rainbow-hat-for-android-things), just plug it onto your Raspberry Pi 3.
Each step will have a different schematic, so have a look into each step's module directory for the relative schematic.

Steps
=================

1. The first step is exactly as defined in the [Android Things Button Sample](https://github.com/androidthings/sample-button). It shows how to use a button input UserDriver to listen to GPIO pin changes, generate and listen for key events and change the state of an LED accordingly
2. The second step duplicates what is done in the first one, for the hardware side, but it adds a software counter which is incremented or decremented when the user presses one of the physical buttons. The two LEDs will show when the counter values is positive (green on), negative (red on) or equal to zero (both off)
3. In the third step we add Firebase integration to our Thing, synchronising the counter value with the Firebase Database
4. The last step adds a mobile app, mirroring the function of the Thing board and synchronised with the same Firebase database. When the counter is modified in the mobile app, the value is automatically updated on the board and vice versa

Build and install
=================

First of all add the Firebase project json configuration file to modules 3 and 4 directories, as described in https://firebase.google.com/docs/android/setup#manually_add_firebase

On Android Studio, click on the "Run" button.

If you prefer to run on the command line, type

```bash
./gradlew installDebug
adb shell am start com.example.androidthings.button/.ButtonActivity
```

If you have everything set up correctly, the LED will light up when you press
the button and light off when you release it.

License
-------

Copyright 2016 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
