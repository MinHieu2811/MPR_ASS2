package com.example.mpr_ass2_2001040076;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
//    @SuppressLint("StaticFieldLeak")
    private final ImageView imageView;

    public ImageDownloader(ImageView imageView) {
        this.imageView = imageView;
    }

    // Interacts with UI
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap == null) {
            Toast.makeText(imageView.getContext(), "Failed to connect!", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        for (String link : strings) {
            return onDownloadImage(link);
        }
        return null;
    }



    private Bitmap onDownloadImage(String link) {
        URL imageUrl;
        HttpURLConnection httpConnection = null;
        InputStream inputStream = null;
        try {
            imageUrl = new URL(link);
            httpConnection = (HttpURLConnection) imageUrl.openConnection();
            inputStream = httpConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
