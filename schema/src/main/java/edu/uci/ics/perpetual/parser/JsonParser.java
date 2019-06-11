package edu.uci.ics.perpetual.parser;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    private static Pattern pattern;

    public static JsonObject toJsonObject(String jsonString) {
        if (pattern == null) {
            pattern = Pattern.compile("\\s*\\w[\\w\\d\\s]*:[^:]+(?=,|$)");
        }
        // remove ''{' and ''}' from jsonString
        String json = jsonString.trim().substring(2, jsonString.length() - 2);

        JsonObject jsonObject = new JsonObject();

        Matcher m = pattern.matcher(json);

        while (m.find()) {
            String[] pair = json.substring(m.start(), m.end()).split(":");
            jsonObject.addProperty(pair[0].trim(), StringUtils.removeQuote(pair[1].trim()));
        }

        return jsonObject;
    }

    public static HashMap<String, String> toMap(String jsonString) {
        HashMap<String, String> map = new HashMap<>();

        if (pattern == null) {
            pattern = Pattern.compile("\\s*\\w[\\w\\d\\s]*:[^:]+(?=,|$)");
        }
        // remove ''{' and ''}' from jsonString
        String json = jsonString.trim().substring(2, jsonString.length() - 2);

        Matcher m = pattern.matcher(json);

        while (m.find()) {
            String[] pair = json.substring(m.start(), m.end()).split(":");
            if (pair.length != 2) {
                throw new IllegalArgumentException("Incorrect Json format");
            }
            if (map.containsKey(pair[0])) {
                throw new IllegalArgumentException(String.format("key '%s' has seen before", pair[0].trim()));
            }
            map.put(pair[0].trim(), StringUtils.removeQuote(pair[1].trim()));
        }

        return map;
    }

    public static List<String> toKeyList(String jsonString) {
        List<String> keys = new ArrayList<>();

        if (pattern == null) {
            pattern = Pattern.compile("\\s*\\w[\\w\\d\\s]*:[^:]+(?=,|$)");
        }
        // remove ''{' and ''}' from jsonString
        String json = jsonString.trim().substring(2, jsonString.length() - 2);

        Matcher m = pattern.matcher(json);

        while (m.find()) {
            String[] pair = json.substring(m.start(), m.end()).split(":");
            if (pair.length != 2) {
                throw new IllegalArgumentException("Incorrect Json format");
            }
            if (keys.contains(pair[0])) {
                throw new IllegalArgumentException(String.format("key '%s' has seen before", pair[0].trim()));
            }
            keys.add(pair[0].trim());
        }

        return keys;
    }
}
