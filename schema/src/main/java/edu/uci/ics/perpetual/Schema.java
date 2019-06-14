package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.types.TaggingFunction;
import edu.uci.ics.perpetual.types.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Schema class is an abstraction the holds all necessary information
 * that will be used by other components of perpetual-db
 */
public class Schema {

    private HashMap<String, MetadataType> metadataMap;
    private HashMap<String, RawType> rawMap;
    private HashMap<String, DataSourceType> dataSourceTypeMap;
    private HashMap<Integer, DataSource> dataSourceMap;
    private HashMap<String, EnrichmentTag> tagMap;
    private HashMap<String, TaggingFunction> enrichmentFunctions;

    // One-to-Many mapping, parent-to-children
    private HashMap<String, Set<String>> relationships;

    public Schema() {
        this.metadataMap = new HashMap<>();
        this.rawMap = new HashMap<>();
        this.dataSourceTypeMap = new HashMap<>();
        this.dataSourceMap = new HashMap<>();
        this.tagMap = new HashMap<>();
        this.enrichmentFunctions = new HashMap<>();

        this.relationships = new HashMap<>();
    }

    boolean existRawType(String typeName) {
        return rawMap.containsKey(typeName.toUpperCase());
    }

    public void addRawType(RawType rawType) {
        rawMap.put(rawType.getName().toUpperCase(), rawType);
    }

    public RawType getRawType(String typeName) {
        return rawMap.get(typeName.toUpperCase());
    }

    boolean existMetadataType(String typeName) {
        return metadataMap.containsKey(typeName.toUpperCase());
    }

    public void addMetadataType(MetadataType metadataType) {
        metadataMap.put(metadataType.getName().toUpperCase(), metadataType);
    }

    boolean existDataSourceType(String typeName) {
        return dataSourceTypeMap.containsKey(typeName.toUpperCase());
    }

    public void addDataSourceType(DataSourceType dataSourceType) {
        dataSourceTypeMap.put(dataSourceType.getName().toUpperCase(), dataSourceType);
    }

    public DataSourceType getDataSourceType(String typeName) {
        return dataSourceTypeMap.get(typeName.toUpperCase());
    }

    boolean existDataSource(int sourceId) {
        return dataSourceMap.containsKey(sourceId);
    }

    public void addDataSource(DataSource dataSource) {
        dataSourceMap.put(dataSource.getId(), dataSource);
    }

    DataSource getDataSource(int sourceId) {
        return dataSourceMap.get(sourceId);
    }

    boolean existTag(String tagName) {
        return tagMap.containsKey(tagName.toUpperCase());
    }

    public void addTag(EnrichmentTag tag) {
        tagMap.put(tag.getName().toUpperCase(), tag);
    }

    EnrichmentTag getTag(String tagName) {
        return tagMap.get(tagName.toUpperCase());
    }

    boolean existFunction(String funcName) {
        return enrichmentFunctions.containsKey(funcName.toUpperCase());
    }

    public void addFunction(TaggingFunction function) {
        enrichmentFunctions.put(function.getFunctionName().toUpperCase(), function);
    }

    TaggingFunction getFunction(String funcName) {
        return enrichmentFunctions.get(funcName.toUpperCase());
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

    public HashMap<String, MetadataType> getMetadataMap() {
        return metadataMap;
    }

    public HashMap<String, RawType> getRawMap() {
        return rawMap;
    }

    public HashMap<String, DataSourceType> getDataSourceTypeMap() {
        return dataSourceTypeMap;
    }

    public HashMap<Integer, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public HashMap<String, EnrichmentTag> getTagMap() {
        return tagMap;
    }

    public HashMap<String, TaggingFunction> getEnrichmentFunctions() {
        return enrichmentFunctions;
    }
}
