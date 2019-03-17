package com.example.pc.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class show_multitable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_multitable);

        MainController MC = MainController.getInstance();
        try {
            ArrayList<ArrayList<ArrayList<String>>> allTables = MC.readAllTables();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Tables Read");
    }
}
