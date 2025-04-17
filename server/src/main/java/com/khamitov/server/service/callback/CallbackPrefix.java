package com.khamitov.server.service.callback;

public class CallbackPrefix {

    public static String delimiter = ":";

    public static String setPrefix(String prefix, String msg) {
        return prefix + delimiter + msg;
    }

    public static String getPrefix(String msg) {
        var splits = msg.split(delimiter);
        if (splits.length < 2) {
            return null;
        }
        return splits[0];
    }

    public static String getTile(String msg) {
        var splits = msg.split(delimiter);
        if (splits.length < 2) {
            return null;
        }
        return splits[1];
    }
}
