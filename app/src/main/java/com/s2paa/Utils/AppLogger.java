package com.s2paa.Utils;


import java.util.logging.Level;
import java.util.logging.Logger;

public  class AppLogger {

    public static void info(String str) {
        Logger.getLogger("SMS").info(str);
    }

    public static void err(String str,Exception ex) {
        Logger.getLogger("SMS").log(Level.INFO,str,ex);
    }

    public static void err(Exception ex) {
        Logger.getLogger("SMS").log(Level.INFO,"",ex);
    }
}
