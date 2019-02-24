package com.example.pc.myfirstapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class show_wikitable extends AppCompatActivity {
    public final String[] wikiSources = {"Population"};
    public final String[] wikiUrls = {
            "https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population"};
    private Context context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wikitable);

        context = getApplicationContext();

        Spinner wikiSourceSpinner = (Spinner) findViewById(R.id.wikiScourceSpinner);
        ArrayAdapter<String> sourceSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wikiSources);
        sourceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wikiSourceSpinner.setAdapter(sourceSpinnerAdapter);

        wikiSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainController mc = MainController.getInstance();
                ArrayList<String[]> wikiData = null;
                try {
                    wikiData = mc.readWikiData(wikiUrls[position]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                TableLayout table = (TableLayout)show_wikitable.this.findViewById(R.id.wikiTable);
                table.removeAllViews();
                int counter = 0;

                TableRow row2;
                TextView textView2;


                for(String[] stockrow : wikiData)
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
                    table.addView(row2);
                }
                table.requestLayout();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
