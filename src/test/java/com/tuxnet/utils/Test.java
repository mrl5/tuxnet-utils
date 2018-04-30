package com.tuxnet.utils;

public class Test {
    public static void main(String[] args) {
        Bash bash = new Bash();
        for (String line : bash.verboseCmd("ls -la")) {
            System.out.println(line);
        }
    }
}
