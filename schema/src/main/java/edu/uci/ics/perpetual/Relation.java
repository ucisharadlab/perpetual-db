package edu.uci.ics.perpetual;

import java.util.*;

public class Relation {

    // One-to-Many mapping, parent-to-children
    private HashMap<String, Set<String>> relationships;

    public Relation() {
        this.relationships = new HashMap<>();
    }

    public void connect(String parentName, String childName) {
        if (!relationships.containsKey(parentName)) {
            relationships.put(parentName, new HashSet<>());
        }
        relationships.get(parentName).add(childName);
    }

    public boolean existRelation(String parentName, String childName) {
        return relationships.containsKey(parentName) && relationships.get(parentName).contains(childName);
    }

    public Set<String> childOf(String parent) {
        return relationships.get(parent);
    }
}
