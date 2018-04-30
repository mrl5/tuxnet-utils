package com.tuxnet.utils;

import java.net.*;
import java.io.*;

/**
 * References:
 * <br>1) <a href="https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html">
 * https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html</a>
 */

public class URLReader {
    public static void main(String[] args) throws Exception {

        URL oracle = new URL("http://www.oracle.com/");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }
}
