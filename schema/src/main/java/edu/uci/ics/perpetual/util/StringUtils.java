package edu.uci.ics.perpetual.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class StringUtils {

    public static String removeQuote(String s) {
        if (s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\'') {
            return s.trim().substring(1, s.length() - 1);
        } else if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
            return s.trim().substring(1, s.length() - 1);
        }
        return s;
    }

    public static String fromList(List<String> list) {
        if (list == null) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(":");

        list.forEach(joiner::add);

        return joiner.toString();
    }

    public static String fromMap(HashMap<String, String> map) {
        if (map == null) {
            return "";
        }
        String mapFormat = "%s~%s";

        StringJoiner joiner = new StringJoiner(":");
        map.forEach((key, value) -> joiner.add(String.format(mapFormat, key, value)));

        return joiner.toString();
    }

    public static List<String> toList(String s) {
        if ("".equalsIgnoreCase(s)) {
            return null;
        }

        return Arrays.asList(s.split(":"));
    }

    public static HashMap<String, String> toMap(String s) {
        if ("".equalsIgnoreCase(s)) {
            return null;
        }

        HashMap<String, String> map = new HashMap<>();

        String[] pairs = s.split(":");
        for (String str: pairs) {
            String[] pair = str.split("~");
            map.put(pair[0], pair[1]);
        }
        return map;
    }
}
