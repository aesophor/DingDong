package com.example.aesophor.dingdong.ui.settings;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.annotation.*;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.widget.*;
import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.user.User;
import com.example.aesophor.dingdong.util.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsFragment extends Fragment {

    public static final int PICK_IMAGE = 1;

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
        Button avatarButton = activity.findViewById(R.id.avatarButton);
        final ImageView avatarSet = activity.findViewById(R.id.avatarSet);


        final User currentUser = activity.getUser();
        usernameField.setText(currentUser.getUsername());
        fullnameField.setText(currentUser.getFullname());
        ImageUtils.b64LoadImage(avatarSet, currentUser.getB64Avatar());


        avatarButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        updateButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Encode avatar bitmap to base64
                MessengerActivity activity = (MessengerActivity) getContext();
                final ImageView avatarSet = activity.findViewById(R.id.avatarSet);
                BitmapDrawable drawable = (BitmapDrawable) avatarSet.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Response update = currentUser.update(fullnameField.getText().toString(), passwordField.getText().toString(), encoded);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MessengerActivity activity = (MessengerActivity) getContext();
                final ImageView avatarSet = activity.findViewById(R.id.avatarSet);
                avatarSet.setImageBitmap(bitmapImage);
                break;
            default:
                break;
        }
    }

}