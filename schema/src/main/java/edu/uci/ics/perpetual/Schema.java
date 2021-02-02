package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.types.TaggingFunction;
import edu.uci.ics.perpetual.types.*;
import edu.uci.ics.perpetual.util.PrettyPrintingMap;

import java.util.HashMap;

public class Schema {

    private HashMap<String, Table> tableMap;
    private HashMap<String, MetadataType> metadataMap;
    private HashMap<String, RawType> rawMap;
    private HashMap<String, DataSourceType> dataSourceTypeMap;
    private HashMap<Integer, DataSource> dataSourceMap;
    private HashMap<String, EnrichmentTag> tagMap;
    private HashMap<String, TaggingFunction> enrichmentFunctions;

    public Schema() {
        this.metadataMap = new HashMap<>();
        this.rawMap = new HashMap<>();
        this.dataSourceTypeMap = new HashMap<>();
        this.dataSourceMap = new HashMap<>();
        this.tagMap = new HashMap<>();
        this.enrichmentFunctions = new HashMap<>();
        this.tableMap = new HashMap<>();
    }

    // region Raw Type
    boolean existRawType(String typeName) {
        return rawMap.containsKey(typeName.toUpperCase());
    }

    public void addRawType(RawType rawType) {
        rawMap.put(rawType.getName().toUpperCase(), rawType);
    }

    public RawType getRawType(String typeName) {
        return rawMap.get(typeName.toUpperCase());
    }
    // endregion

    // region Metadata Type
    boolean existMetadataType(String typeName) {
        return metadataMap.containsKey(typeName.toUpperCase());
    }

    public void addMetadataType(MetadataType metadataType) {
        metadataMap.put(metadataType.getName().toUpperCase(), metadataType);
    }
    // endregion

    boolean existTable(String typeName) {
        return tableMap.containsKey(typeName.toUpperCase());
    }

    public void addTable(Table table) {
        tableMap.put(table.getName().toUpperCase(), table);
    }

    // region DataSource Type
    boolean existDataSourceType(String typeName) {
        return dataSourceTypeMap.containsKey(typeName.toUpperCase());
    }

    public void addDataSourceType(DataSourceType dataSourceType) {
        dataSourceTypeMap.put(dataSourceType.getName().toUpperCase(), dataSourceType);
    }

    public DataSourceType getDataSourceType(String typeName) {
        return dataSourceTypeMap.get(typeName.toUpperCase());
    }
    // endregion

    // region DataSource
    boolean existDataSource(int sourceId) {
        return dataSourceMap.containsKey(sourceId);
    }

    public void addDataSource(DataSource dataSource) {
        dataSourceMap.put(dataSource.getId(), dataSource);
    }

    DataSource getDataSource(int sourceId) {
        return dataSourceMap.get(sourceId);
    }
    // endregion

    // region Enrichment Tag
    boolean existTag(String tagName) {
        return tagMap.containsKey(tagName.toUpperCase());
    }

    public void addTag(EnrichmentTag tag) {
        tagMap.put(tag.getName().toUpperCase(), tag);
    }

    EnrichmentTag getTag(String tagName) {
        return tagMap.get(tagName.toUpperCase());
    }
    // endregion

    // region TaggingFunction
    boolean existFunction(String funcName) {
        return enrichmentFunctions.containsKey(funcName.toUpperCase());
    }

    public void addFunction(TaggingFunction function) {
        enrichmentFunctions.put(function.getFunctionName().toUpperCase(), function);
    }

    TaggingFunction getFunction(String funcName) {
        return enrichmentFunctions.get(funcName.toUpperCase());
    }
    // endregion

    // region Getter
    public HashMap<String, MetadataType> getMetadataMap() {
        return metadataMap;
    }

    public void setMetadataMap(HashMap<String, MetadataType> metadataMap) {
        this.metadataMap = metadataMap;
    }

    public HashMap<String, RawType> getRawMap() {
        return rawMap;
    }

    public void setRawMap(HashMap<String, RawType> rawMap) {
        this.rawMap = rawMap;
    }

    public void setDataSourceMap(HashMap<Integer, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
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

    public void setTagMap(HashMap<String, EnrichmentTag> tagMap) {
        this.tagMap = tagMap;
    }

    public HashMap<String, TaggingFunction> getEnrichmentFunctions() {
        return enrichmentFunctions;
    }

    public void setEnrichmentFunctions(HashMap<String, TaggingFunction> enrichmentFunctions) {
        this.enrichmentFunctions = enrichmentFunctions;
    }

    public HashMap<String, Table> getTableMap() {
        return tableMap;
    }

    public void setTableMap(HashMap<String, Table> tableMap) {
        this.tableMap = tableMap;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Metadata Types\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(metadataMap));
        sb.append("\n-------------------------------------------------\n\n");

        sb.append("Raw Types\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(rawMap));
        sb.append("\n-------------------------------------------------\n\n");

        sb.append("Data Source Types\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(dataSourceTypeMap));
        sb.append("\n-------------------------------------------------\n\n");

        sb.append("Data Sources\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(dataSourceMap));
        sb.append("\n-------------------------------------------------\n\n");

        sb.append("Tags\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(tagMap));
        sb.append("\n-------------------------------------------------\n\n");

        sb.append("Enrichment Functions\n-------------------------------------------\n");
        sb.append(new PrettyPrintingMap(enrichmentFunctions));
        sb.append("\n-------------------------------------------------\n\n");

        return sb.toString();

    }

    // endregion
}
