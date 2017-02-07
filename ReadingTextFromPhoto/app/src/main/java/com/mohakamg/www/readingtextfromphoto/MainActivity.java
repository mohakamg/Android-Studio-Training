package com.mohakamg.www.readingtextfromphoto;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Bitmap image;
    private TessBaseAPI mTess;
    String dataPath = "";
    public final static int RESULT_LOAD_IMG = 1;
    ImageView imageView;
    TextView OCRtextView;
    String OCRresult;
    String[] words;


    public class searchQueryOCRprocessing extends AsyncTask<String[], Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String[]... params) {
            return possibleCombinations(params[0]);
        }

    }

    public class processOCR extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            OCRresult = null;
            mTess.setImage(image);
            OCRresult = mTess.getUTF8Text();
            words = splitIntoWords(OCRresult);
            return words;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection URLConnection = null;

            try {
                url = new URL(urls[0]);
                URLConnection = (HttpURLConnection) url.openConnection();
                URLConnection.connect();
                InputStream in = URLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char ch = (char) data;
                    result += ch;
                    data = reader.read();
                }
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }


    public void processImage(View view){
        processOCR ocr = new processOCR();

        try {
            String[] words = ocr.execute().get();
            searchQueryOCRprocessing ocrComb = new searchQueryOCRprocessing();
            ArrayList<String> possibleComb = null;
            possibleComb = ocrComb.execute(words).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        OCRtextView.setText(OCRresult);
    }

    public void pickImage(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final Uri imageUri = data.getData();
            final InputStream imageStream;

            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                image = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        OCRtextView = (TextView)findViewById(R.id.OCRTextView);
        OCRtextView.setMovementMethod(new ScrollingMovementMethod());

        image = BitmapFactory.decodeResource(getResources(),R.drawable.test_image2);

        imageView.setImageBitmap(image);

        dataPath = getFilesDir() + "/tesseract/";

        checkFile(new File(dataPath + "tessdata/"));

        String lang = "eng";
        mTess = new TessBaseAPI();
        mTess.init(dataPath,lang);
/*
        String test = "Hello, How are you doing?";
        String[] testWords = splitIntoWords(test);
        ArrayList<String> testComb = possibleCombinations(testWords);
        Log.i("Test", testComb.toString() );*/

    }

    private void copyFiles() {

        try {
            String filePath = dataPath + "/tessdata/eng.traineddata";

            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filePath);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = instream.read(buffer))!=-1){
                outstream.write(buffer);
            }

            outstream.flush();
            outstream.close();
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkFile(File dir){
        if(!dir.exists()&&dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()){
            String filePath = dataPath + "/tessdata/eng.traineddata";
            File dataFile = new File(filePath);
            if(!dataFile.exists()){
                copyFiles();
            }
        }

    }

    private String[] splitIntoWords(String s){
        String[] words = s.split(" ");
        return words;
    }

    public ArrayList<String> possibleCombinations(String[] words){
        String result= "";
        int k = 0;
        ArrayList<String> results = new ArrayList<>();
        for(int i = 0;i<words.length-1;i++){
            result = "";
            for(int j=i;j<=words.length-1;j++){
                result += words[j] + " ";
                results.add(result);
            }
        }
        return results;
    }

    private String getFirstLink(String searchQuery){
        DownloadTask task = new DownloadTask();
        String Url = "";
        try {
            //<h3 class="r"
            Url = task.execute("https://www.google.com/#q="+searchQuery).get();
            String splitResult = Url.split("<h3 class=\"r\"")[1];
            Pattern p = Pattern.compile("<a href=\"(.*?)\"");
            Matcher m = p.matcher(splitResult);
            m.find();
            String first = m.group(1);
            return first;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
