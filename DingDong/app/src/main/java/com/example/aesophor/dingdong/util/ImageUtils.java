package com.example.aesophor.dingdong.util;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

public class ImageUtils {

    private ImageUtils() {

    }

    public static void b64LoadImage(ImageView imageView, String b64string) {
        byte[] imageAsBytes = Base64.decode(b64string.getBytes(), Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
    }

}
