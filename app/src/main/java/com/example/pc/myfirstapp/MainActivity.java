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
        System.out.format(tableFormat, "A", "AA", "AAA");


        for (String[] x : inputList)
        {
            /*
            output = output + x[0] +" "+x[1]+" "+x[2]+" "+x[3]+" "+x[4] + "\r\n";
            String.format("%16s%16s%16s%16s%16s", x[0], x[1], x[2],x[3],x[4]);
            */
            //int max = (x[0] > b) ? a : b; .substring(0,10)
            int max=0;
            int StringLength=12;
            /*
            output = output +String.format("%-14s%12s%-12s%-12s%-12s",
                    x[0].substring(0, max = (x[0].length() < StringLength) ? x[0].length()-1 : StringLength),
                    x[1].substring(0,  max = (x[1].length() < StringLength) ? x[1].length()-1 : StringLength),
                    x[2].substring(0,  max = (x[2].length() < StringLength) ? x[2].length()-1 : StringLength),
                    x[3].substring(0,  max = (x[3].length() < StringLength) ? x[3].length()-1 : StringLength),
                    x[4].substring(0,  max = (x[4].length() < StringLength) ? x[4].length()-1 : StringLength)
            ).trim()+ "\r\n";
            */

            /*
            output = output +
                    (x[0].substring(0, max = (x[0].length() < StringLength) ? x[0].length()-1 : StringLength) + "\t\t\t\t\t\t" +
                    x[1].substring(0,  max = (x[1].length() < StringLength) ? x[1].length()-1 : StringLength)+ "\t\t" +
                    x[2].substring(0,  max = (x[2].length() < StringLength) ? x[2].length()-1 : StringLength)+ "\t\t" +
                    x[3].substring(0,  max = (x[3].length() < StringLength) ? x[3].length()-1 : StringLength))+ "\r\n";
                    //x[4].substring(0,  StringLength)
            */
            output = output + String.format(tableFormat,
                        x[0].substring(0, max = (x[0].length() < StringLength) ? x[0].length()-1 : StringLength),
                        x[1].substring(0,  max = (x[1].length() < StringLength) ? x[1].length()-1 : StringLength),
                        x[2].substring(0,  max = (x[2].length() < StringLength) ? x[2].length()-1 : StringLength));
                            //x[3].substring(0,  max = (x[3].length() < StringLength) ? x[3].length()-1 : StringLength));
            //x[4].substring(0,  StringLength)



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

        Button startButton = (Button) findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                LayoutInflater myInflater = LayoutInflater.from(MainActivity.this);
                View view = myInflater.inflate(R.layout.activity_main, null);
                Toast mytoast = new Toast(MainActivity.this);
                mytoast.setView(view);
                mytoast.setDuration(Toast.LENGTH_LONG);
                mytoast.show();
                */
                /*
                Intent intent = new Intent(v.getContext(), ShowResultActivity.class);

                EditText editText = (EditText) findViewById(R.id.FirstNumEditText);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                */

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            String fullPage="";
                            ArrayList<String[]> stockList = new ArrayList<String[]>();
                            String[] stockData = new String[5];
                            Integer stockCounter = 0;

                            try
                            {
                                URL yahoo = new URL("https://www.finanzen.net/rohstoffe");
                                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                                yahoo.openStream()));

                                String inputLine;
                                boolean tableStartTrigger = false;
                                boolean dataStartTrigger = false;
                                int fieldCounter=0;
                                //String[] stockData = new String[3];

                                while ((inputLine = in.readLine()) != null)
                                {
                                    fullPage = fullPage + inputLine;
                                    //if (inputLine.contains("<div class=\"row quotebox\">"))

                                    if (inputLine.contains("<table id=\"commodity_prices\" class=\"table\">"))
                                    {
                                        tableStartTrigger=true;
                                    }

                                    if (tableStartTrigger)
                                    {
                                        /**
                                         *
                                         <tr >
                                         <td class="tdFixed" ><a href="/rohstoffe/goldpreis" title="Goldpreis">Goldpreis</a></td>
                                         <td class="text-right tdFixed" >1.314,93</td>
                                         <td class="text-right tdFixed" ><div class="arrow-right-top"></div></td>
                                         <td class="text-right tdFixed" ><span class="colorGreen">1,14</span></td>
                                         <td class="text-left tdFixed" >USD je Feinunze</td>
                                         <td class="text-right tdFixed" >08.02 22:59</td>
                                         </tr>
                                         */

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


                                        // Einzelkurs
                                        /*
                                        if (inputLine.contains("<span>CHF</span>") && inputLine.indexOf("+")==-1 && inputLine.indexOf("-")==-1)
                                        {
                                            stockData[0] = inputLine.substring(0, inputLine.indexOf("<"));
                                        }
                                        if ((inputLine.indexOf("+")>=0 || inputLine.indexOf("-")>=0) && inputLine.contains("<span>CHF</span>"))
                                        {
                                            stockData[1] = inputLine.substring(0, inputLine.indexOf("<"));
                                        }
                                        if (inputLine.contains("<span>%</span>"))
                                        {
                                            stockData[2] = inputLine.substring(0, inputLine.indexOf("<"));
                                            dataTrigger=false;
                                            break;
                                        }
                                        */
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
                        } catch (Exception e) {
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

                /*
                String messageStr="Hello Android!";
                int server_port = 12345;
                try
                {


                    DatagramSocket s = new DatagramSocket();
                    InetAddress local = InetAddress.getByName("192.168.1.102");
                    int msg_length=messageStr.length();
                    byte[] message = messageStr.getBytes();
                    DatagramPacket p = new DatagramPacket(message, msg_length,local,server_port);
                    s.send(p);
                }
                catch (IOException e)
                {
                    System.out.println(e.toString());
                }
                */
            }
        });
    }
}
