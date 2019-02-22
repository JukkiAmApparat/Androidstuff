package com.example.pc.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ShowResultActivity extends AppCompatActivity {

    public final String[] finSources = {"Rohstoffe", "Devisen", "DAX", "Dow Jones", "Nikkei", "S&P 500", "Nasdaq 100", "TECDAX", "MDAX"};
    public final String[] finUrls = {
            "https://www.finanzen.net/rohstoffe",
            "https://www.finanzen.net/devisen",
            "https://www.finanzen.net/index/dax/30-werte",
            "https://www.finanzen.net/index/dow_jones/werte",
            "https://www.finanzen.net/index/nikkei_225/werte",
            "https://www.finanzen.net/index/s&p_500/werte",
            "https://www.finanzen.net/index/nasdaq_100/werte",
            "https://www.finanzen.net/index/tecdax/werte",
            "https://www.finanzen.net/index/mdax/werte"
    };
    public final String multiPageURL = "?p="; // dann page nummer dahinter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        MainController mc = MainController.getInstance();
        ArrayList<String[]> stockData = null;
        try {
            stockData = mc.readStockData("https://www.finanzen.net/rohstoffe");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Spinner finSourceSpinner = (Spinner) findViewById(R.id.finScourceSpinner);
        ArrayAdapter<String> sourceSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, finSources);
        sourceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        finSourceSpinner.setAdapter(sourceSpinnerAdapter);

        finSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                MainController mc = MainController.getInstance();
                ArrayList<String[]> stockData = null;
                try {
                    stockData = mc.readStockData(finUrls[position]);
                    //stockData = mc.readStockData("https://www.finanzen.net/rohstoffe");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (!finUrls[position].toLowerCase().contains("rohstoffe"))
                {
                    TableLayout table = (TableLayout)ShowResultActivity.this.findViewById(R.id.stockTable);
                    table.removeAllViews();
                    TableRow row = (TableRow) LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.stock_row7, null);
                    ((TextView)row.findViewById(R.id.stock7_name_cell)).setText("Name \n ISIN)");
                    ((TextView)row.findViewById(R.id.stock7_last_cell)).setText("Last \n Prev");
                    ((TextView)row.findViewById(R.id.stock7_low_cell)).setText("Low \n High");
                    ((TextView)row.findViewById(R.id.stock7_plus_cell)).setText("+\n -");
                    ((TextView)row.findViewById(R.id.stock7_time_cell)).setText("Time \n Date");
                    ((TextView)row.findViewById(R.id.stock7_plus3m_cell)).setText("+/-3M \n %3M");
                    ((TextView)row.findViewById(R.id.stock7_plus6m_cell)).setText("+/-6M \n %6M");
                    ((TextView)row.findViewById(R.id.stock7_plus1y_cell)).setText("+/-1Y \n %1Y");

                    table.addView(row);
                    int counter = 0;

                    for(String[] stockrow : stockData)
                    {
                        counter++;
                        row = (TableRow)LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.stock_row7, null);
                        ((TextView)row.findViewById(R.id.stock7_name_cell)).setText(mc.formatStockCell(stockrow[0]));
                        ((TextView)row.findViewById(R.id.stock7_last_cell)).setText(mc.formatStockCell(stockrow[1]));
                        ((TextView)row.findViewById(R.id.stock7_low_cell)).setText(mc.formatStockCell(stockrow[2]));
                        ((TextView)row.findViewById(R.id.stock7_plus_cell)).setText(mc.formatStockCell(stockrow[4]));
                        ((TextView)row.findViewById(R.id.stock7_time_cell)).setText(mc.formatStockCell(stockrow[6]));
                        ((TextView)row.findViewById(R.id.stock7_plus3m_cell)).setText(mc.formatStockCell(stockrow[7]));
                        ((TextView)row.findViewById(R.id.stock7_plus6m_cell)).setText(mc.formatStockCell(stockrow[8]));
                        ((TextView)row.findViewById(R.id.stock7_plus1y_cell)).setText(mc.formatStockCell(stockrow[9]));
                        table.addView(row);
                    }
                    table.requestLayout();
                }

                if (finUrls[position].toLowerCase().contains("rohstoffe"))
                {
                    TableLayout table = (TableLayout)ShowResultActivity.this.findViewById(R.id.stockTable);
                    table.removeAllViews();
                    TableRow row = (TableRow)LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.stock_row, null);
                    ((TextView)row.findViewById(R.id.stock_name_cell)).setText("Stock");
                    ((TextView)row.findViewById(R.id.stock_value_cell)).setText("Value");
                    ((TextView)row.findViewById(R.id.stock_percent_cell)).setText("Percent");
                    ((TextView)row.findViewById(R.id.stock_unit_cell)).setText("Unit");
                    table.addView(row);

                    for(String[] stockrow : stockData)
                    {
                        row = (TableRow)LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.stock_row, null);
                        ((TextView)row.findViewById(R.id.stock_name_cell)).setText(mc.formatStockCell(stockrow[0]));
                        ((TextView)row.findViewById(R.id.stock_value_cell)).setText(mc.formatStockCell(stockrow[1]));
                        ((TextView)row.findViewById(R.id.stock_percent_cell)).setText(mc.formatStockCell(stockrow[2]));
                        ((TextView)row.findViewById(R.id.stock_unit_cell)).setText(mc.formatStockCell(stockrow[3]));
                        table.addView(row);
                    }
                    table.requestLayout();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        TableLayout table = (TableLayout)ShowResultActivity.this.findViewById(R.id.stockTable);
        TableRow row = (TableRow)LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.stock_row, null);
        ((TextView)row.findViewById(R.id.stock_name_cell)).setText("Stock");
        ((TextView)row.findViewById(R.id.stock_value_cell)).setText("Value");
        ((TextView)row.findViewById(R.id.stock_percent_cell)).setText("Percent");
        ((TextView)row.findViewById(R.id.stock_unit_cell)).setText("Unit");
        table.addView(row);

        for(String[] stockrow : stockData)
        {
            row = (TableRow)LayoutInflater.from(ShowResultActivity.this).inflate(R.layout.stock_row, null);
            ((TextView)row.findViewById(R.id.stock_name_cell)).setText(mc.formatStockCell(stockrow[0]));
            ((TextView)row.findViewById(R.id.stock_value_cell)).setText(mc.formatStockCell(stockrow[1]));
            ((TextView)row.findViewById(R.id.stock_percent_cell)).setText(mc.formatStockCell(stockrow[2]));
            ((TextView)row.findViewById(R.id.stock_unit_cell)).setText(mc.formatStockCell(stockrow[3]));
            table.addView(row);
        }
        table.requestLayout();
    }
}
