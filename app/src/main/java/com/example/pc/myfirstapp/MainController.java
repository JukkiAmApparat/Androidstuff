package com.example.pc.myfirstapp;

import android.text.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainController {

    public static ArrayList<String[]> stockDataList;
    public static Integer cellStringLength;
    private static MainController MainController_instance = null;

    public static MainController getInstance()
    {
        if (MainController_instance == null) {
            MainController_instance = new MainController();
        }
        return MainController_instance;
    }

    public MainController()
    {
        cellStringLength = 12;
        stockDataList = new ArrayList<String[]>();
    }

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

    public static String formatStockCell(String cellValue)
    {
        cellValue=cellValue.trim();
        if (cellValue.length()>cellStringLength)
        {
            return cellValue.substring(0,cellStringLength);
        }
        else
        {
            return cellValue;
        }
    }

    public ArrayList<String[]> readStockData(String webpageUrl) throws InterruptedException, ExecutionException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<ArrayList<String[]>> callable = new Callable<ArrayList<String[]>>() {
            @Override
            public ArrayList<String[]> call() {

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
                    return null;
                }
                return stockList;
            }
        };

        Future<ArrayList<String[]>> future = executor.submit(callable);
        // future.get() returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();

        return future.get();
    }

}
