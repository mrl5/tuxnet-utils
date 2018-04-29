/**
 * Runs an external system command from JVM. "quiet" or "verbose" mode available
 *
 * References:
 * 1) https://alvinalexander.com/java/edu/pj/pj010016
 * 2) https://stackoverflow.com/questions/5928225/how-to-make-pipes-work-with-runtime-exec
 * 3) https://en.wikipedia.org/wiki/Pipeline_(Unix)
 *
 * @source https://alvinalexander.com/java/edu/pj/pj010016
 * @modification mrl5
 */

package com.tuxnet.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExternalCommand {

    /* only standard output */
    public List<String> quiet(String[] cmd) {
        return runCmd(cmd, 0);
    }

    /* standard and error output */
    public List<String> verbose(String[] cmd) {
        return runCmd(cmd, 1);
    }

    /* by giving String[] argument pipelines will work (more info in ref. #2 and #3) */
    private List<String> runCmd(String[] cmd, int option) {
        List<String> stdOut = new ArrayList<>();
        String s;

        try {
            /* using Runtime exec method: */
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            /* Add stdInput lines to ArrayList */
            while ((s = stdInput.readLine()) != null) stdOut.add(s);

            /* output errors */
            if (option == 1) {
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
            return stdOut;
        } catch (IOException e) {
            System.out.println("Exception: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
