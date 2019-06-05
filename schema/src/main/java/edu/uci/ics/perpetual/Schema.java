package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.function.TaggingFunction;
import edu.uci.ics.perpetual.types.*;

import java.util.HashMap;

public class Schema {

    private HashMap<String, MetadataType> metadataMap;
    private HashMap<String, RawType> rawMap;
    private HashMap<String, DataSourceType> dataSourceMap;
    private HashMap<String, EnrichmentTag> tagMap;
    private HashMap<String, TaggingFunction> enrichmentFunctions;

//    private HashMap<String, DataObjectType> tagMap;

    Schema() {
        this.metadataMap = new HashMap<>();
        this.dataSourceMap = new HashMap<>();
        this.rawMap = new HashMap<>();
        this.tagMap = new HashMap<>();
        this.enrichmentFunctions = new HashMap<>();
    }

    // region Raw Type
    boolean existRawType(String typeName) {
        return rawMap.containsKey(typeName);
    }

    void addRawType(RawType rawType) {
        rawMap.put(rawType.getName(), rawType);
    }

    RawType getRawType(String typeName) {
        return rawMap.get(typeName);
    }
    // endregion

    // region Metadata Type
    boolean existMetadataType(String typeName) {
        return metadataMap.containsKey(typeName);
    }

    void addMetadataType(MetadataType metadataType) {
        metadataMap.put(metadataType.getName(), metadataType);
    }
    // endregion

    // region DataSource Type
    boolean existDataSource(String sourceName) {
        return dataSourceMap.containsKey(sourceName);
    }

    void addSource(DataSourceType dataSourceType) {
        dataSourceMap.put(dataSourceType.getName(), dataSourceType);
    }

    DataSourceType getSource(String sourceName) {
        return dataSourceMap.get(sourceName);
    }
    // endregion

    // region Enrichment Tag
    boolean existTag(String tagName) {
        return tagMap.containsKey(tagName);
    }

    void addTag(EnrichmentTag tag) {
        tagMap.put(tag.getName(), tag);
    }

    EnrichmentTag getTag(String tagName) {
        return tagMap.get(tagName);
    }
    // endregion

    // region TaggingFunction
    boolean existFunction(String funcName) {
        return enrichmentFunctions.containsKey(funcName);
    }

    void addFcuntion(TaggingFunction function) {
        enrichmentFunctions.put(function.getFunctionName(), function);
    }
    // endregion

}
