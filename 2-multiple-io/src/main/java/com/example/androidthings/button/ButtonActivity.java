/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.button;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Example of using Button driver for increasing/decreasing a value.
 * The change of the value is reflected by the toggled LEDs.
 * <p>
 * This activity initialize an InputDriver for every button to emit key events when the buttons GPIO pin states change
 * and flip the state of the LED GPIO pin.
 * <p>
 * You need to connect two LED and two push button switch to pins specified in {@link BoardDefaults}
 * according to the schematic provided in the sample README.
 */
public class ButtonActivity extends Activity {
    private static final String TAG = ButtonActivity.class.getSimpleName();

    private Gpio redLedGpio;
    private Gpio greenLedGpio;
    private ButtonInputDriver minusButtonInputDriver;
    private ButtonInputDriver plusButtonInputDriver;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting ButtonActivity");

        PeripheralManagerService pioService = new PeripheralManagerService();
        try {
            Log.i(TAG, "Configuring GPIO pins");
            redLedGpio = pioService.openGpio(BoardDefaults.getGPIOForRedLED());
            redLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            greenLedGpio = pioService.openGpio(BoardDefaults.getGPIOForGreenLED());
            greenLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            Log.i(TAG, "Registering button driver");
            // Initialize and register the InputDriver that will emit SPACE key events
            // on GPIO state changes.
            minusButtonInputDriver = new ButtonInputDriver(
                    BoardDefaults.getGPIOForMinusButton(),
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_NUMPAD_SUBTRACT
            );
            minusButtonInputDriver.register();

            plusButtonInputDriver = new ButtonInputDriver(
                    BoardDefaults.getGPIOForPlusButton(),
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_NUMPAD_ADD
            );
            plusButtonInputDriver.register();
        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pins", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                counter--;
                displayStatus();
                return true;
            case KeyEvent.KEYCODE_NUMPAD_ADD:
                counter++;
                displayStatus();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Update the value of the LEDs output:
     * - Red LED is on if the counter value is less than 0
     * - Green LED is on if the counter value is greater than 0
     * - Both LEDs are off if the counter is at 0
     */
    private void displayStatus() {
        Log.d(TAG, "Current counter value: " + counter);
        try {
            if (counter == 0) {
                redLedGpio.setValue(false);
                greenLedGpio.setValue(false);
            } else if (counter > 0) {
                redLedGpio.setValue(false);
                greenLedGpio.setValue(true);
            } else {
                redLedGpio.setValue(true);
                greenLedGpio.setValue(false);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (minusButtonInputDriver != null) {
            minusButtonInputDriver.unregister();
            try {
                minusButtonInputDriver.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally {
                minusButtonInputDriver = null;
            }
        }
        if (plusButtonInputDriver != null) {
            plusButtonInputDriver.unregister();
            try {
                plusButtonInputDriver.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally {
                plusButtonInputDriver = null;
            }
        }

        if (redLedGpio != null) {
            try {
                redLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally {
                redLedGpio = null;
            }
            redLedGpio = null;
        }
        if (greenLedGpio != null) {
            try {
                greenLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally {
                greenLedGpio = null;
            }
            greenLedGpio = null;
        }
    }
}
