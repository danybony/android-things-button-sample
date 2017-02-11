package com.example.androidthings.button;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FirebaseCounter firebaseCounter;

    private TextView counterValue;
    private View negativeLight;
    private View positiveLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseCounter = FirebaseCounter.newInstance();
        counterValue = (TextView) findViewById(R.id.counter_value);
        negativeLight = findViewById(R.id.light_negative);
        positiveLight = findViewById(R.id.light_positive);

        TextView decreaseButton = (TextView) findViewById(R.id.button_decrease);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseCounter.decreaseCounter();
            }
        });

        TextView increaseButton = (TextView) findViewById(R.id.button_increase);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseCounter.increaseCounter();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseCounter.setListener(counterListener);
    }

    private FirebaseCounter.Listener counterListener = new FirebaseCounter.Listener() {
        @Override
        public void onCounterUpdated(long value) {
            displayStatus(value);
        }
    };

    private void displayStatus(long counter) {
        counterValue.setText(String.valueOf(counter));
        if (counter == 0) {
            negativeLight.setSelected(false);
            positiveLight.setSelected(false);
        } else if (counter < 0) {
            negativeLight.setSelected(true);
            positiveLight.setSelected(false);
        } else {
            negativeLight.setSelected(false);
            positiveLight.setSelected(true);
        }
    }

    @Override
    protected void onPause() {
        firebaseCounter.removeListener();
        super.onPause();
    }
}
