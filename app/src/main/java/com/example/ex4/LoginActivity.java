package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.connect).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Client.getInstance().connect(((EditText) findViewById(R.id.ip)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString()));
        // TODO joystick
    }
}
