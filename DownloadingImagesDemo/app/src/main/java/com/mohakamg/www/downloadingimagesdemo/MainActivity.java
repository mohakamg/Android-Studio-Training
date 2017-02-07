package com.mohakamg.www.downloadingimagesdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView image;

    public void downloadImg(View view){

        //https://upload.wikimedia.org/wikipedia/en/0/0d/Simpsons_FamilyPicture.png

        ImageDownloader task = new ImageDownloader();
        try {
            Bitmap myBitmap = task.execute("https://source.unsplash.com/weekly?hiking").get();
            image.setImageBitmap(myBitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return myBitmap;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView)findViewById(R.id.imageView);

    }
}
