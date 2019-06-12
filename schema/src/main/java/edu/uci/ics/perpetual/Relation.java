package edu.uci.ics.perpetual;

import java.util.*;

public class Relation {

    // One-to-Many mapping, parent-to-children
    private HashMap<String, Set<String>> relationships;

    public Relation() {
        this.relationships = new HashMap<>();
    }

    public void connect(String parentName, String childName) {
        String key = parentName.toUpperCase();
        if (!relationships.containsKey(key)) {
            relationships.put(key, new HashSet<>());
        }
        relationships.get(key).add(childName);
    }

    public boolean existRelation(String parentName, String childName) {
        String parent = parentName.toUpperCase();
        String child = childName.toLowerCase();
        return relationships.containsKey(parent) && relationships.get(parent).contains(child);
    }

    public Set<String> childOf(String parent) {
        return relationships.get(parent.toUpperCase());
    }
}
