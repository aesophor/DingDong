package com.example.aesophor.dingdong.util;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.example.aesophor.dingdong.R;

public class ImageUtils {

    private ImageUtils() {

    }

    public static void b64LoadImage(ImageView imageView, String b64string) {
        if (b64string == null || b64string.isEmpty()) {
            imageView.setImageResource(R.mipmap.default_avatar);
        } else {
            byte[] imageAsBytes = Base64.decode(b64string.getBytes(), Base64.DEFAULT);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
    }

}
