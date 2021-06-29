package com.example.blueteeth;

public class TimeToText {

    public static String convert(long time) {
        String result = "";
        result += time / 3600 >= 10 ? time / 3600 : "0" + time / 3600;
        result += ":";
        result += (time % 3600) / 60 >= 10 ? (time % 3600) / 60 : "0" + (time % 3600) / 60;
        result += ":";
        result += time % 60 >= 10 ? time % 60 : "0" + time % 60;
        return result;
    }
}
