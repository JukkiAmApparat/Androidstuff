package com.example.pc.myfirstapp;

import android.text.Html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainController {

    public static Integer cellStringLength;
    public static String multiPageURL;
    public static int duplicateCounter;
    public static HashSet<String> entrySet;
    private static MainController MainController_instance = null;

    public static ArrayList<ArrayList<ArrayList<String>>> allTables;
    public static ArrayList<ArrayList<String>> currentTable;
    public static ArrayList<String> currentRow;
    public static String fullPage;
    public static Document doc;

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
        entrySet=new HashSet<String>();
        multiPageURL = "?p="; // dann page nummer dahinter
        duplicateCounter = 0;
    }

    public String removeHtmlTags(String inputLine)
    {
        return Html.fromHtml(inputLine).toString().replaceAll("￼","").trim();
    }


    public static String formatStockCell(String cellValue)
    {
        if (cellValue.contains("\n"))
        {
            return cellValue;
        }

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

    public ArrayList<String[]> readMultipageStock (final String webpageUrl) throws InterruptedException, ExecutionException
    {
        duplicateCounter=0;
        entrySet.clear();
        ArrayList<String[]> completeList = new ArrayList<String[]>();

        if (webpageUrl.toLowerCase().contains("rohstoffe"))
        {
            return readStockData(webpageUrl);
        }

        for(int i=0; duplicateCounter<5; i++)
        {
            if (i==0)
            {
                completeList = readStockData(webpageUrl);
            }
            else
            {
                completeList.addAll(readStockData(webpageUrl+multiPageURL+(i+1)));
            }
        }
        return completeList;
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

                                    if (!entrySet.add(Arrays.toString(stockData)))
                                    {
                                        duplicateCounter++;
                                    }

                                    else
                                    {
                                        stockList.add(stockData);
                                    }

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

    public ArrayList<ArrayList<String>> readWikiData(final String webpageUrl) throws InterruptedException, ExecutionException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<ArrayList<ArrayList<String>>> callable = new Callable<ArrayList<ArrayList<String>>>() {
            @Override
            public ArrayList<ArrayList<String>> call() {

                ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
                ArrayList<String> rowData = new ArrayList<String>();
                boolean cellStartTrigger = false;

                try
                {
                    URL dataUrl = new URL(webpageUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(dataUrl.openStream()));

                    String inputLine;
                    String lineBuffer="";
                    String lineAddon="";
                    boolean tableStartTrigger = false;
                    int fieldCounter=0;

                    while ((inputLine = in.readLine()) != null)
                    {

                        if (inputLine.contains("<table class=\"wikitable sortable\""))
                        {
                            tableStartTrigger=true;
                        }

                        if (tableStartTrigger)
                        {
                            if (lineAddon.length()>0)
                            {
                                inputLine=lineAddon+inputLine;
                                lineAddon="";
                            }

                            if (inputLine.contains("<td") || inputLine.contains("<th"))
                            {
                                cellStartTrigger=true;
                            }

                            if (cellStartTrigger)
                            {
                                lineBuffer=lineBuffer+inputLine;
                            }

                            if (inputLine.contains("</th>") || inputLine.contains("</td>"))
                            {

                                if (inputLine.contains("</th>"))
                                {
                                    lineAddon=lineBuffer.substring(lineBuffer.indexOf("</th>")+5);
                                    rowData.add(removeHtmlTags(lineBuffer.substring(lineBuffer.indexOf("<th"),lineBuffer.indexOf("</th>"))));
                                }
                                else
                                {
                                    lineAddon=lineBuffer.substring(lineBuffer.indexOf("</td>")+5);
                                    rowData.add(removeHtmlTags(lineBuffer.substring(lineBuffer.indexOf("<td"),lineBuffer.indexOf("</td>"))));
                                }

                                fieldCounter++;
                                cellStartTrigger=false;
                                lineBuffer="";
                            }

                            if (inputLine.contains("</tr>") && fieldCounter>0)
                            {
                                dataList.add(rowData);
                                rowData = new ArrayList<String>();
                                lineBuffer="";
                                lineAddon=inputLine.substring(inputLine.indexOf("</tr>")+5);
                                cellStartTrigger=false;
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
        Future<ArrayList<ArrayList<String>>> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

    public ArrayList<ArrayList<ArrayList<String>>> readAllTables(final String webpageUrl) throws InterruptedException, ExecutionException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<ArrayList<ArrayList<ArrayList<String>>>> callable = new Callable<ArrayList<ArrayList<ArrayList<String>>>>() {
            @Override
            public ArrayList<ArrayList<ArrayList<String>>> call() {
                allTables = new ArrayList<ArrayList<ArrayList<String>>>();
                currentTable = new ArrayList<ArrayList<String>>();
                currentRow = new ArrayList<String>();
                fullPage = new String("");

                try
                {
                    URL currentURL = new URL(webpageUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(currentURL.openStream(), "UTF8"));
                    String inputLine;
                    Element table;
                    Elements header;
                    Elements rows;
                    Element row;
                    Elements cols;

                    while ((inputLine = in.readLine()) != null)
                    {
                        fullPage = fullPage + inputLine;
                    }

                    in.close();
                    doc = Jsoup.parse(fullPage);

                    if (doc.select("table").size()>0)
                    {
                        for (int x=0; x<doc.select("table").size(); x++)
                        {
                            table = doc.select("table").get(x);
                            currentTable = new ArrayList<ArrayList<String>>();

                            header = table.select("th");
                            currentRow = new ArrayList<String>();
                            for (int i=0; i<header.size(); i++)
                            {
                                currentRow.add(header.get(i).text());
                            }
                            currentTable.add(currentRow);

                            rows = table.select("tr");

                            for (int i=0; i<rows.size(); i++)
                            {
                                row = rows.get(i);
                                currentRow = new ArrayList<String>();

                                cols = row.select("td");

                                if (cols.size()==0)
                                {
                                    continue;
                                }
                                else
                                {
                                    for (int j=0; j<cols.size(); j++)
                                    {
                                        currentRow.add(cols.get(j).text());
                                    }
                                    currentTable.add(currentRow);
                                }
                            }
                            allTables.add(currentTable);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return allTables;
            }
        };
        Future<ArrayList<ArrayList<ArrayList<String>>>> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

}
