package com.Conmiro.bots.api.Logging.Logger;

/**
 * Created by Connor on 7/9/2016.
 * APACHE Standard Logging Levels: FATAL, ERROR, WARNING (WARN), INFO, DEBUG, TRACE
 */



public class Logger {

    private static final int defaultLogLevel = 3;
    private static int logLevel = defaultLogLevel;

    public static void info(String str) {
        if (logLevel > 2){
            System.out.println("*Logger*<Info>: " + str);
        }
    }

    public static void debug(String str) {
        if (logLevel > 1)
            System.out.println("*Logger*<Debug>: "+str);
    }


    public static void error(String str) {
        if (logLevel > 0)
            System.out.println("*Logger*<Error>: "+str);
    }

    public static void disable() {
        logLevel = 0;
    }

    public static void enable() {
        logLevel = defaultLogLevel;
    }

    public static void setLogLevel(int x){
        logLevel = x;
    }


}
