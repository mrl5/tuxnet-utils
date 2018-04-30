package com.tuxnet.utils;

import java.util.List;

/**
 * Executes given command in GNU Bash. "quiet", "verbose" and "verboseCmd" modes available.
 * Make sure to set correct path to GNU Bash if not running on Linux.
 * <p>
 * References:
 * <br>1) <a href="https://en.wikipedia.org/wiki/Pipeline_(Unix)">
 * https://en.wikipedia.org/wiki/Pipeline_(Unix)</a>
 *
 * @author mrl5
 */

public class Bash {
    /* Array made for pipelines (see references) */
    private String[] terminalCmd = {
            null,
            "-c",
            ""
    };
    /* object for running a command in terminal */
    private ExternalCommand runCmd = new ExternalCommand();

    /**
     * @param bashPath path to GNU Bash
     */
    public Bash(String bashPath) {
        terminalCmd[0] = bashPath;
    }

    public Bash() {
        OperatingSystem os = new OperatingSystem();
        if (os.isWindows()) terminalCmd[0] = "\"C:\\Program Files\\Git\\bin\\sh.exe\"";
        else terminalCmd[0] = "/bin/sh";
    }

    /**
     * Quiet mode method
     *
     * @param cmd bash command
     * @return result as List
     */
    public List<String> quiet(String cmd) {
        terminalCmd[2] = cmd;
        return runCmd.quiet(terminalCmd);
    }

    /**
     * verbose mode method
     *
     * @param cmd bash command
     * @return result as List
     */
    public List<String> verbose(String cmd) {
        terminalCmd[2] = cmd;
        return runCmd.verbose(terminalCmd);
    }

    /**
     * verbose mode method with visible command - like in Linux terminal
     *
     * @param cmd bash command
     * @return result as List
     */
    public List<String> verboseCmd(String cmd) {
        StringBuilder terminal = new StringBuilder("$ ");
        terminal.append(cmd);
        System.out.println(terminal.toString());
        return verbose(cmd);
    }
}
