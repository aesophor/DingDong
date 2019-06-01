package com.example.aesophor.dingdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.user.User;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void signIn() {
        TextView errorMessage = findViewById(R.id.errorMessage);
        String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        try {
            Response signIn = User.login(username, password);

            switch (signIn.getStatusCode()) {
                case SUCCESS: {
                    Intent intent = new Intent(SignInActivity.this, MessengerActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    break;
                }
                case VALIDATION_ERR: {
                    errorMessage.setText("Incorrect username/password");
                    break;
                }
                default:
                    break;
            }
        } catch (Exception ex) {
            errorMessage.setText("Unable to connect to server");
            ex.printStackTrace();
        }
    }

}
