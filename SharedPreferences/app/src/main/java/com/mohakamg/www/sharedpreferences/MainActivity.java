package com.mohakamg.www.sharedpreferences;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.mohakamg.www.sharedpreferences", MODE_PRIVATE);

        ArrayList<String> friends = new ArrayList<>();

        friends.add("Monica");
        friends.add("Chandler");
        friends.add("Joey");

/*
        try {
            sharedPreferences.edit().putString("friends", ObjectSerializer.serialize(friends)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        ArrayList<String> newFriends = new ArrayList<>();

        try {
            newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*sharedPreferences.edit().putString("username", "rob").apply();

        String username = sharedPreferences.getString("username", "");
*/
        Log.i("Friends", newFriends.toString());


    }
}
