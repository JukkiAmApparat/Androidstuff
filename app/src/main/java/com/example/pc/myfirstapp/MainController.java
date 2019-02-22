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
    }

    public String removeHtmlTags(String inputLine)
    {
        return Html.fromHtml(inputLine).toString().trim();
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

    public ArrayList<String[]> readStockData(final String webpageUrl) throws InterruptedException, ExecutionException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<ArrayList<String[]>> callable = new Callable<ArrayList<String[]>>() {
            @Override
            public ArrayList<String[]> call() {

                ArrayList<String[]> stockList = new ArrayList<String[]>();
                String[] stockData = null;

                String inputLine;
                boolean tableStarted = false;
                boolean cellStartTrigger=false;
                int fieldCounter=0;
                String lineBuffer = "";

                String tableStartTrigger="";
                String rowStartTrigger="";
                String rowEndTrigger="";
                String tableEndTrigger="";

                try
                {
                    URL financeUrl = new URL(webpageUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(financeUrl.openStream()));



                    if (!webpageUrl.toLowerCase().contains("rohstoffe"))
                    {
                        stockData = new String[20];
                        tableStartTrigger="<div class=\"table-quotes\">";
                        rowStartTrigger="<tr>";
                        rowEndTrigger="</tr>";
                        tableEndTrigger="</table>";
                    }

                    if (webpageUrl.toLowerCase().contains("rohstoffe"))
                    {
                        stockData = new String[5];
                        tableStartTrigger="<table id=\"commodity_prices\" class=\"table\">";
                        rowStartTrigger="<td class";
                        rowEndTrigger="</tr>";
                        tableEndTrigger="</table>";

                    }
                    String currentLine = "";
                    while ((inputLine = in.readLine()) != null)
                    {
                        currentLine= inputLine;
                        //Rohstofftable: "<table id=\"commodity_prices\" class=\"table\">")
                        //Aktien Tables: <div class="table-quotes">
                        //Devisentable: <h2 class="box-headline">Devisentabelle</h2>
                        if (webpageUrl.toLowerCase().contains("rohstoffe"))
                        {
                            if (inputLine.contains(tableStartTrigger))
                            {
                                tableStarted=true;
                            }

                            if (tableStarted)
                            {
                                if  (inputLine.contains(rowStartTrigger))
                                {
                                    if (!removeHtmlTags(inputLine).equals("")) {
                                        stockData[fieldCounter] = removeHtmlTags(inputLine);
                                        fieldCounter++;
                                    }
                                }
                                if (inputLine.contains(rowEndTrigger) && fieldCounter>0)
                                {
                                    stockList.add(stockData);
                                    stockData = new String[5];
                                    fieldCounter=0;
                                }
                                if(inputLine.contains(tableEndTrigger))
                                {
                                    break;
                                }
                            }
                        }

                        if (!webpageUrl.toLowerCase().contains("rohstoffe"))
                        {
                            if (inputLine.contains(tableStartTrigger))
                            {
                                tableStarted=true;
                            }

                            if (tableStarted)
                            {

                                if (inputLine.contains("<td >"))
                                {
                                    cellStartTrigger=true;
                                }

                                if (cellStartTrigger)
                                {
                                    lineBuffer=lineBuffer+inputLine;
                                }

                                if (inputLine.contains("</td>"))
                                {
                                    stockData[fieldCounter] = removeHtmlTags(lineBuffer);
                                    fieldCounter++;
                                    lineBuffer="";
                                    cellStartTrigger=false;
                                }

                                if (inputLine.contains(rowEndTrigger) && fieldCounter>0)
                                {
                                    stockList.add(stockData);
                                    stockData = new String[20];
                                    fieldCounter=0;
                                    lineBuffer="";
                                    cellStartTrigger=false;
                                }
                                if(inputLine.contains(tableEndTrigger))
                                {
                                    break;
                                }
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
        executor.shutdown();

        return future.get();
    }
    public ArrayList<String[]> readSoccerData(final String webpageUrl) throws InterruptedException, ExecutionException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<ArrayList<String[]>> callable = new Callable<ArrayList<String[]>>() {
            @Override
            public ArrayList<String[]> call() {

                ArrayList<String[]> dataList = new ArrayList<String[]>();
                String[] rowData = new String[8];

                try
                {
                    URL dataUrl = new URL(webpageUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(dataUrl.openStream()));

                    String inputLine;
                    boolean tableStartTrigger = false;
                    int fieldCounter=0;

                    while ((inputLine = in.readLine()) != null)
                    {

                        if (inputLine.contains("<div class=\"responsive-table\">"))
                        {
                            tableStartTrigger=true;
                        }

                        if (tableStartTrigger)
                        {
                            if  (inputLine.contains("<a class") && !inputLine.contains("tiny_wappen"))
                            {
                                if (!removeHtmlTags(inputLine).equals("")) {
                                    rowData[fieldCounter] = removeHtmlTags(inputLine);
                                    fieldCounter++;
                                }
                            }
                            if (inputLine.contains("<td class=\"zentriert\">") && fieldCounter>0)
                            {
                                rowData[fieldCounter] = removeHtmlTags(inputLine);
                                fieldCounter++;
                            }
                            if (inputLine.contains("</tr>") && fieldCounter>0)
                            {
                                dataList.add(rowData);
                                rowData = new String[8];
                                fieldCounter=0;
                            }
                            if(inputLine.contains("</table>"))
                            {
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
                return dataList;
            }
        };
        Future<ArrayList<String[]>> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

}
