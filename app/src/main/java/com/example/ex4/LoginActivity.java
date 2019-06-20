package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * OnCreate of the login activity, sets a listener on the CONNECT button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.connect).setOnClickListener(this);
    }

    /**
     * onClick of the CONNECT button, parses the ip and port from the text boxes, connects to
     * the server and starts the joystick activity.
     * @param view
     */
    @Override
    public void onClick(View view) {
        Client.getInstance().connect(((EditText) findViewById(R.id.ip)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString()));
        startActivity(new Intent(this, Joystick.class));
    }
}
