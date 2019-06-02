package com.example.aesophor.dingdong.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.user.User;

public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MessengerActivity activity = (MessengerActivity) getContext();
        EditText usernameField = activity.findViewById(R.id.usernameField);
        final EditText fullnameField = activity.findViewById(R.id.fullnameField);
        final EditText passwordField = activity.findViewById(R.id.passwordField);
        Button updateButton = activity.findViewById(R.id.updateButton);

        final User currentUser = activity.getUser();
        usernameField.setText(currentUser.getUsername());
        fullnameField.setText(currentUser.getFullname());

        updateButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response update = currentUser.update(fullnameField.getText().toString(), passwordField.getText().toString());
                if (update.success()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Success")
                            .setMessage("Your profile has been successfully updated")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Failed")
                            .setMessage("Error: " + update.getStatusCode().toString())
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });
    }

}