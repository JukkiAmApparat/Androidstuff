package com.example.pc.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        MainController mc = MainController.getInstance();

        ArrayList<String[]> stockData = null;
        try {
            stockData = mc.readStockData("https://www.finanzen.net/rohstoffe");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TableLayout table = (TableLayout)testActivity.this.findViewById(R.id.testTable);
        //table.setLayoutMode(TabLayout.MODE_SCROLLABLE);
        table.setScrollY(table.getWidth());
        //table.getTabAt(table).select();

        TableRow row = (TableRow) LayoutInflater.from(testActivity.this).inflate(R.layout.stock_row, null);
        ((TextView)row.findViewById(R.id.stock_name_cell)).setText("Stock");
        ((TextView)row.findViewById(R.id.stock_value_cell)).setText("Value");
        ((TextView)row.findViewById(R.id.stock_percent_cell)).setText("Percent");
        ((TextView)row.findViewById(R.id.stock_unit_cell)).setText("Unit");
        table.addView(row);

        for(String[] stockrow : stockData)
        {
            // Inflate your row "template" and fill out the fields.
            row = (TableRow)LayoutInflater.from(testActivity.this).inflate(R.layout.stock_row, null);
            ((TextView)row.findViewById(R.id.stock_name_cell)).setText(mc.formatStockCell(stockrow[0]));
            ((TextView)row.findViewById(R.id.stock_value_cell)).setText(mc.formatStockCell(stockrow[1]));
            ((TextView)row.findViewById(R.id.stock_percent_cell)).setText(mc.formatStockCell(stockrow[2]));
            ((TextView)row.findViewById(R.id.stock_unit_cell)).setText(mc.formatStockCell(stockrow[3]));
            table.addView(row);
        }
        row = (TableRow)LayoutInflater.from(testActivity.this).inflate(R.layout.stock_row, null);
        ((TextView)row.findViewById(R.id.stock_name_cell)).setText("Stock");
        ((TextView)row.findViewById(R.id.stock_value_cell)).setText("Value");
        ((TextView)row.findViewById(R.id.stock_percent_cell)).setText("Percent");
        ((TextView)row.findViewById(R.id.stock_unit_cell)).setText("Unit");
        table.addView(row);

        table.requestLayout();

    }
}
