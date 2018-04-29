/**
 * Provides methods for determining OS upon which runs JVM
 *
 * References:
 * https://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 */

package com.tuxnet.utils;

public class OperatingSystem {
    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
