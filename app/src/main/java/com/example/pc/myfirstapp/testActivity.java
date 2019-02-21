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

public class testActivity extends AppCompatActivity {

    public final String[] leagueSources = {"Bundesliga", "Premier League", "La Liga", "Serie A", "Ligue 1"};
    public final String[] leagueUrls = {
            "https://www.transfermarkt.ch/1-bundesliga/tabelle/wettbewerb/L1/saison_id/2018",
            "https://www.transfermarkt.ch/premier-league/tabelle/wettbewerb/GB1/saison_id/2018",
            "https://www.transfermarkt.ch/primera-division/tabelle/wettbewerb/ES1/saison_id/2018",
            "https://www.transfermarkt.ch/serie-a/tabelle/wettbewerb/IT1/saison_id/2018",
            "https://www.transfermarkt.ch/ligue-1/tabelle/wettbewerb/FR1/saison_id/2018"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Spinner leagueSelectionSpinner = (Spinner) findViewById(R.id.leagueSelectionSpinner);
        ArrayAdapter<String> sourceSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leagueSources);
        sourceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagueSelectionSpinner.setAdapter(sourceSpinnerAdapter);

        leagueSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainController mc = MainController.getInstance();
                ArrayList<String[]> stockData = null;
                try {
                    stockData = mc.readSoccerData(leagueUrls[position]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                TableLayout table = (TableLayout)testActivity.this.findViewById(R.id.testTable);
                table.removeAllViews();
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
                table.requestLayout();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
