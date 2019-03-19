package com.example.pc.myfirstapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        Button refreshDataButton = (Button) findViewById(R.id.refreshDataButton);
        refreshDataButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                EditText addressEditText = (EditText) findViewById(R.id.adressEditText);
                String currentAddress = addressEditText.getText().toString();

                MainController MC = MainController.getInstance();
                ArrayList<ArrayList<ArrayList<String>>> allTables = null;
                try {
                    allTables = MC.readAllTables(currentAddress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("Tables Read");
                TableLayout contentTableLayout = (TableLayout) findViewById(R.id.contentTableLayout);
                contentTableLayout.removeAllViews();

                TableLayout currentTableLayout;

                int counter = 0;

                TableRow row2;
                TextView textView2;
                int tableCounter = 0;

                for (ArrayList<ArrayList<String>> currentTable : allTables)
                {
                    tableCounter++;
                    currentTableLayout = new TableLayout(context);
                    for(ArrayList<String> stockrow : currentTable)
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
                        currentTableLayout.addView(row2);
                        currentTableLayout.requestLayout();

                    }
                    row2 = new TableRow(context);
                    textView2 = new TextView(context);
                    textView2.setText("Table "+ tableCounter);
                    textView2.setTypeface(null, Typeface.BOLD);
                    contentTableLayout.addView(textView2);
                    contentTableLayout.addView(currentTableLayout);
                    contentTableLayout.requestLayout();
                    counter = 0;
                }
            }
        });
    }
}
