package com.mohakamg.www.languagepreferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean firstTime;
    TextView textView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setLanguage(String language){

        sharedPreferences.edit().clear().apply();
        sharedPreferences.edit().putString("language", language).apply();
        sharedPreferences.edit().putBoolean("firstTime", firstTime).apply();

        textView.setText(language);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.english:
                setLanguage("English");
                break;

            case R.id.spanish:
                setLanguage("Spanish");
                break;

            default:
                firstTime = true;
                sharedPreferences.edit().putBoolean("firstTime", firstTime);
                break;



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        sharedPreferences = this.getSharedPreferences("com.mohakamg.www.languagepreferences",MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();
        firstTime = sharedPreferences.getBoolean("firstTime",true);


        String languagePrefrence = "";


        if(firstTime){
            firstTime = false;
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Language").setMessage("Which Language would you like?")
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setLanguage("English");
                        }
                    }).setNegativeButton("Spanish", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        setLanguage("Spanish");
                }
            }).show();



        }

        else{
            languagePrefrence = sharedPreferences.getString("language","English");
            textView.setText(languagePrefrence);


        }


    }
}
