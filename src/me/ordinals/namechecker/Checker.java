package me.ordinals.namechecker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class Checker
{
    private int minNameLength, maxNameLength, threads;
    private String name = "";
    private boolean taken;
    private File file = new File("names.txt");
    private PrintWriter printWriter = printWriter = new PrintWriter(file);

    public Checker() throws Exception
    {
        menu();
    }

    private void menu()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter minimum name length: ");
        minNameLength = scanner.nextInt();
        System.out.print("enter maximum name length: ");
        maxNameLength = scanner.nextInt();
        System.out.print("enter amount of threads: ");
        threads = scanner.nextInt();
        checkNames();
    }

    private void checkNames()
    {
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        while (threads > 0)
        {
            generateName();
            getStatus();
            if (taken)
            {
                System.out.println("[-] " + name + "is taken.");
            }
            else
            {
                System.out.println("[+] " + name + "is available.");
                printWriter.println(name);
            }
            threads--;
        }
        printWriter.close();
    }

    private void generateName()
    {
        try
        {
            if (minNameLength > 26)
            {
                System.out.println("you cannot use a minimum number above 26.");
                Thread.sleep(5000L);
                System.exit(-1);
            }
            else if (maxNameLength > 26)
            {
                System.out.println("you cannot use a maximum number above 26.");
                Thread.sleep(5000L);
                System.exit(-1);
            }
            else if (minNameLength == maxNameLength)
            {
                System.out.println("you cannot use the same minimum and maximum.");
                Thread.sleep(5000L);
                System.exit(-1);
            }

            name = "";
            String letters[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
            Random random = new Random();
            Thread.sleep(1L);
            for (int i = 0; i < random.nextInt(maxNameLength - minNameLength) + minNameLength; i++)
            {
                name += letters[random.nextInt(26)];
            }
            Thread.sleep(3000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void getStatus()
    {
        try
        {
            URL url = new URL("https://namemc.com/search?q=" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "text/html; charset=utf-8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null)
            {
                stringBuffer.append(line);
            }
            reader.close();
            if (stringBuffer.toString().contains("Available"))
            {
                taken = false;
            }
            else
            {
                taken = true;
            }
            stringBuffer.delete(0, stringBuffer.length());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
