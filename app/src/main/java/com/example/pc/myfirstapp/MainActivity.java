package com.example.pc.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Hello";

    public String removeHtmlTags(String inputLine)
    {
        return Html.fromHtml(inputLine).toString().trim();
    }

    public String stocksToString(ArrayList<String[]> inputList)
    {
        String output = "";
        String tableFormat = "|%1$-14s|%2$-14s|%3$-14s|\r\n";

        for (String[] x : inputList)
        {
            int max=0;
            int StringLength=12;

            output = output + String.format(tableFormat,
                        x[0].substring(0, max = (x[0].length() < StringLength) ? x[0].length()-1 : StringLength),
                        x[1].substring(0,  max = (x[1].length() < StringLength) ? x[1].length()-1 : StringLength),
                        x[2].substring(0,  max = (x[2].length() < StringLength) ? x[2].length()-1 : StringLength));
        }
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView webTextView = (TextView) findViewById(R.id.WebTextView);
        webTextView.setMovementMethod(new ScrollingMovementMethod());

        Button showResultButton = (Button) findViewById(R.id.showResultButton);
        showResultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ShowResultActivity.class);

            EditText editText = (EditText) findViewById(R.id.FirstNumEditText);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            }
        });

        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), testActivity.class);
                startActivity(intent2);
            }
        });


        Button startButton = (Button) findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            String fullPage="";
                            ArrayList<String[]> stockList = new ArrayList<String[]>();
                            String[] stockData = new String[5];

                            try
                            {
                                URL financeUrl = new URL("https://www.finanzen.net/rohstoffe");
                                BufferedReader in = new BufferedReader(new InputStreamReader(financeUrl.openStream()));

                                String inputLine;
                                boolean tableStartTrigger = false;
                                int fieldCounter=0;

                                while ((inputLine = in.readLine()) != null)
                                {
                                    fullPage = fullPage + inputLine;

                                    if (inputLine.contains("<table id=\"commodity_prices\" class=\"table\">"))
                                    {
                                        tableStartTrigger=true;
                                    }

                                    if (tableStartTrigger)
                                    {
                                        if  (inputLine.contains("<td class"))
                                        {
                                            if (!removeHtmlTags(inputLine).equals("")) {
                                                stockData[fieldCounter] = removeHtmlTags(inputLine);
                                                fieldCounter++;
                                            }
                                        }
                                        if (inputLine.contains("</tr>") && fieldCounter>0)
                                        {
                                            stockList.add(stockData);
                                            stockData = new String[5];
                                            fieldCounter=0;
                                        }
                                        if(inputLine.contains("</table>"))
                                        {
                                            tableStartTrigger=false;
                                            break;
                                        }
                                    }
                                }
                                in.close();
                            }
                            catch (IOException e)
                            {

                                System.out.println(e.toString());
                                webTextView.setText(e.toString());
                            }
                            webTextView.setText(stocksToString(stockList));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
                Toast.makeText(getApplicationContext(), "HMMMMMMM", Toast.LENGTH_LONG).show();

                EditText firstNumEditText = (EditText) findViewById(R.id.FirstNumEditText);
                TextView resultTextView = (TextView) findViewById(R.id.ResultTextView);

                String currentText = firstNumEditText.getText().toString();
                resultTextView.setText(currentText);
            }
        });
    }
}
