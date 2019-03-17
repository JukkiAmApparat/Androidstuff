package com.example.pc.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sourceSelectionSpinner = (Spinner) findViewById(R.id.SourceSelectionSpinner);
        String[] sources = {"Finanzen", "Bundesliga", "Transfermarkt"};

        ArrayAdapter<String> sourceSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sources);
        sourceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSelectionSpinner.setAdapter(sourceSpinnerAdapter);

        Button showResultButton = (Button) findViewById(R.id.showFinanceButton);
        showResultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ShowResultActivity.class);
            startActivity(intent);
            }
        });

        Button testButton = (Button) findViewById(R.id.showFootballButton);
        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), testActivity.class);
                startActivity(intent2);
            }
        });

        Button wikiButton = (Button) findViewById(R.id.showWikiButton);
        wikiButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(v.getContext(), show_wikitable.class);
                startActivity(intent3);
            }
        });

        Button showDataButton = (Button) findViewById(R.id.showDataButton);
        showDataButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(v.getContext(), show_multitable.class);
                startActivity(intent4);
            }
        });
    }
}
