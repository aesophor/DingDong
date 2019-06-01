package com.example.aesophor.dingdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.user.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        try {
            Response login = User.login(username, password);
            if (login.success()) {
                Intent intent = new Intent(this, MessengerActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {

            }
        } catch (Exception ex) {
            ((TextView) findViewById(R.id.errorMessage)).setText("Unable to connect to server");
            ex.printStackTrace();
        }
    }

}
