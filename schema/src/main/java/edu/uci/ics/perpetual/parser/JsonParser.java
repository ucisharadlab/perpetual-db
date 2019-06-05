package edu.uci.ics.perpetual.parser;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.table.Attribute;
import edu.uci.ics.perpetual.table.AttributeKind;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    private static Pattern pattern;

    public static JsonObject toJsonObject(String jsonString) {
        if (pattern == null) {
            pattern = Pattern.compile("\\w[\\w\\d]*:[^:]+(?=,|$)");
        }
        // remove '{' and '}' toJsonObject string
        String json = jsonString.substring(1, jsonString.length() - 1);

        JsonObject jsonObject = new JsonObject();

        Matcher m = pattern.matcher(json);

        while (m.find()) {
            String[] pair = json.substring(m.start(), m.end()).split(":");
            jsonObject.addProperty(pair[0].trim(), pair[1].trim());
        }

        return jsonObject;
    }

    public static List<Attribute> toAttribute(String jsonString, AttributeKind kind) {
        Set<String> seen = new HashSet<>();
        List<Attribute> attributeList = new ArrayList<>();

        if (pattern == null) {
            pattern = Pattern.compile("\\w[\\w\\d]*:[^:]+(?=,|$)");
        }
        // remove '{' and '}' toJsonObject string
        String json = jsonString.substring(1, jsonString.length() - 1);

        Matcher m = pattern.matcher(json);

        while (m.find()) {
            String[] pair = json.substring(m.start(), m.end()).split(":");
            if (pair.length != 2) {
                throw new IllegalArgumentException("Incorrect Json format");
            }
            if (seen.contains(pair[0])) {
                throw new IllegalArgumentException(String.format("key '%s' has seen before", pair[0]));
            }
            seen.add(pair[0]);
            attributeList.add(Attribute.of(kind,pair[0], pair[1]));
        }

        return attributeList;
    }

    public static List<String> toKeyList(String jsonString) {
        List<String> keys = new ArrayList<>();

        if (pattern == null) {
            pattern = Pattern.compile("\\w[\\w\\d]*:[^:]+(?=,|$)");
        }
        // remove '{' and '}' toJsonObject string
        String json = jsonString.substring(1, jsonString.length() - 1);

        Matcher m = pattern.matcher(json);

        while (m.find()) {
            String[] pair = json.substring(m.start(), m.end()).split(":");
            if (pair.length != 2) {
                throw new IllegalArgumentException("Incorrect Json format");
            }
            if (keys.contains(pair[0])) {
                throw new IllegalArgumentException(String.format("key '%s' has seen before", pair[0]));
            }
            keys.add(pair[0]);
        }

        return keys;
    }
}
