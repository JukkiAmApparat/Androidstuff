package com.example.pc.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShowResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
       // TextView textView = findViewById(R.id.textView2);
        //textView.setText(message);
        Integer[] test = {12,3,9001};
        int counter=0;
        TableLayout table = (TableLayout)ShowResultActivity.this.findViewById(R.id.stockTable);
        for(Integer b :test)
        {
            // Inflate your row "template" and fill out the fields.
            TableRow row = (TableRow)LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.attrib_row, null);
            ((TextView)row.findViewById(R.id.attrib_name)).setText(""+test[counter]);
            ((TextView)row.findViewById(R.id.attrib_value)).setText(""+(test[counter]/2));
            table.addView(row);
            counter++;
        }
        table.requestLayout();
    }
}
