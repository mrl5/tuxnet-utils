package com.tuxnet.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs an external system command from JVM. "quiet" or "verbose" mode available.
 * <p>
 * References:
 * <br>1) <a href="https://alvinalexander.com/java/edu/pj/pj010016">
 * https://alvinalexander.com/java/edu/pj/pj010016</a>
 * <br>2) <a href="https://stackoverflow.com/questions/5928225/how-to-make-pipes-work-with-runtime-exec">
 * https://stackoverflow.com/questions/5928225/how-to-make-pipes-work-with-runtime-exec</a>
 * <br>3) <a href="https://en.wikipedia.org/wiki/Pipeline_(Unix)">
 * https://en.wikipedia.org/wiki/Pipeline_(Unix)</a>
 */

public class ExternalCommand {
    /**
     * Only standard output
     *
     * @param cmd Console command
     * @return result as List
     */
    public List<String> quiet(String[] cmd) {
        return runCmd(cmd, 0);
    }

    /**
     * Standard and error output
     *
     * @param cmd Console command
     * @return result as List
     */
    public List<String> verbose(String[] cmd) {
        return runCmd(cmd, 1);
    }

    /**
     * Runs an external system command from JVM. By giving String[] argument pipelines will work (more info in ref. #2 and #3)
     *
     * @param cmd    Console command
     * @param option 0: quiet; 1: verbose
     * @return result as List
     */
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
