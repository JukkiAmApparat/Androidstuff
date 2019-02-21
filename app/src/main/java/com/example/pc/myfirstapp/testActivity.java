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
            stockData = mc.readSoccerData("https://www.finanzen.net/rohstoffe");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TableLayout table = (TableLayout)testActivity.this.findViewById(R.id.testTable);
        //table.setLayoutMode(TabLayout.MODE_SCROLLABLE);
        table.setScrollY(table.getWidth());
        //table.getTabAt(table).select();

        TableRow row = (TableRow) LayoutInflater.from(testActivity.this).inflate(R.layout.soccer_row, null);
        ((TextView)row.findViewById(R.id.rank_cell)).setText("Rank");
        ((TextView)row.findViewById(R.id.club_cell)).setText("Club");
        ((TextView)row.findViewById(R.id.games_cell)).setText("Games");
        ((TextView)row.findViewById(R.id.win_cell)).setText("W");
        ((TextView)row.findViewById(R.id.draw_cell)).setText("D");
        ((TextView)row.findViewById(R.id.lost_cell)).setText("L");
        ((TextView)row.findViewById(R.id.goals_cell)).setText("Goals");
        ((TextView)row.findViewById(R.id.diff_cell)).setText("Diff.");
        ((TextView)row.findViewById(R.id.points_cell)).setText("Pts.");

        table.addView(row);
        int counter = 0;

        for(String[] stockrow : stockData)
        {
            // Inflate your row "template" and fill out the fields.
            counter++;
            row = (TableRow)LayoutInflater.from(testActivity.this).inflate(R.layout.soccer_row, null);
            ((TextView)row.findViewById(R.id.rank_cell)).setText(counter+"");
            ((TextView)row.findViewById(R.id.club_cell)).setText(mc.formatStockCell(stockrow[0]));
            ((TextView)row.findViewById(R.id.games_cell)).setText(mc.formatStockCell(stockrow[1]));
            ((TextView)row.findViewById(R.id.win_cell)).setText(mc.formatStockCell(stockrow[2]));
            ((TextView)row.findViewById(R.id.draw_cell)).setText(mc.formatStockCell(stockrow[3]));
            ((TextView)row.findViewById(R.id.lost_cell)).setText(mc.formatStockCell(stockrow[4]));
            ((TextView)row.findViewById(R.id.goals_cell)).setText(mc.formatStockCell(stockrow[5]));
            ((TextView)row.findViewById(R.id.diff_cell)).setText(mc.formatStockCell(stockrow[6]));
            ((TextView)row.findViewById(R.id.points_cell)).setText(mc.formatStockCell(stockrow[7]));
            table.addView(row);
        }
        /*
        row = (TableRow)LayoutInflater.from(testActivity.this).inflate(R.layout.stock_row, null);
        ((TextView)row.findViewById(R.id.stock_name_cell)).setText("Stock");
        ((TextView)row.findViewById(R.id.stock_value_cell)).setText("Value");
        ((TextView)row.findViewById(R.id.stock_percent_cell)).setText("Percent");
        ((TextView)row.findViewById(R.id.stock_unit_cell)).setText("Unit");
        table.addView(row);*/

        table.requestLayout();

    }
}
