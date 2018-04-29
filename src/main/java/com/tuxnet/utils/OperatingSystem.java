package com.tuxnet.utils;

/**
 * This class provides methods for determining OS upon which runs JVM
 * <p>
 * References:
 * https://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 */

public class OperatingSystem {
    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
