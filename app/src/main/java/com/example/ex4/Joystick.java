package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Joystick extends AppCompatActivity implements JoystickView.JoystickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }

    @Override
    public void onJoystickMoved(float xPlumbus, float yPlumbus, int source) {
        Log.d("Main Method", "X percent: " + xPlumbus + " Y percent: " + yPlumbus);
    }


}
