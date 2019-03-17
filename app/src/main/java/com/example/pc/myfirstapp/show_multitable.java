package com.example.pc.myfirstapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class show_multitable extends AppCompatActivity {
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_multitable);
        context = getApplicationContext();

        MainController MC = MainController.getInstance();
        ArrayList<ArrayList<ArrayList<String>>> allTables = null;
        try {
            allTables = MC.readAllTables();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Tables Read");
        TableLayout contentTableLayout = (TableLayout) findViewById(R.id.contentTableLayout);

        contentTableLayout.removeAllViews();
        int counter = 0;

        TableRow row2;
        TextView textView2;


        for(ArrayList<String> stockrow : allTables.get(0))
        {
            counter++;
            row2=new TableRow(context);
            for (String cellData : stockrow)
            {
                textView2 = new TextView(context);
                textView2.setText(cellData);
                if (counter==1)
                {
                    textView2.setTypeface(null, Typeface.BOLD);
                }
                row2.addView(textView2);
            }
            contentTableLayout.addView(row2);
        }
        contentTableLayout.requestLayout();

        //TableLayout dynamicTableLayout = new TableLayout(this);
    }
}
