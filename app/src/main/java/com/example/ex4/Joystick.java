package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Joystick extends AppCompatActivity implements JoystickView.JoystickListener{

    /**
     * OnCreate of the joystick, sets the layout to it's correspondent XML.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }

    /**
     * Upon every movement of the joystick, sends the new values to the server.
     * @param xPlumbus horizontal value -> the aileron of the plane.
     * @param yPlumbus vertical value -> the elevator of the plane.
     */
    @Override
    public void onJoystickMoved(float xPlumbus, float yPlumbus) {
        Client client = Client.getInstance();
        client.send("set /controls/flight/aileron " + Float.toString(xPlumbus * -1) + "\r\n");
        client.send("set /controls/flight/elevator " + Float.toString(yPlumbus * -1) + "\r\n");
        Log.d("Main Method", "X percent: " + xPlumbus + " Y percent: " + yPlumbus);
    }


}
