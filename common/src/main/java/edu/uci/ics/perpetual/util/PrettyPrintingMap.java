package edu.uci.ics.perpetual.util;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PrettyPrintingMap<K, V> {
    private Map<K, V> map;
    private String indent = "";

    public PrettyPrintingMap(Map<K, V> map) {
        this.map = map;
    }

    public PrettyPrintingMap(Map<K, V> map, String indent) {
        this.map = map;
        this.indent = indent;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<K, V> entry = iter.next();
            sb.append(indent);
            sb.append(entry.getKey());
            sb.append('=');
            if (entry.getValue() instanceof Map) {
                sb.append("\n");
                sb.append(new PrettyPrintingMap((Map)entry.getValue(), "\t"));
                sb.append("\n");
            } else {
                sb.append(entry.getValue());
            }
            if (iter.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();

    }

    public JsonObject toJSON() {
        JsonObject sb = new JsonObject();
        Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<K, V> entry = iter.next();
            if (entry.getValue() instanceof Map) {
                sb.add(entry.getKey().toString(), new PrettyPrintingMap((Map)entry.getValue(), "\t").toJSON());

            } else {
                sb.addProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return sb;

    }
}