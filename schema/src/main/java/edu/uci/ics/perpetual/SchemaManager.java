package edu.uci.ics.perpetual;


import edu.uci.ics.perpetual.request.*;
import edu.uci.ics.perpetual.statement.insert.Insert;
import edu.uci.ics.perpetual.storage.MysqlStorage;
import edu.uci.ics.perpetual.storage.Storage;
import edu.uci.ics.perpetual.types.TaggingFunction;

import edu.uci.ics.perpetual.parser.JsonParser;
import edu.uci.ics.perpetual.schema.Tag;
import edu.uci.ics.perpetual.statement.StatementVisitorAdapter;
import edu.uci.ics.perpetual.statement.add.AddAcquisitionFunction;
import edu.uci.ics.perpetual.statement.add.AddDataSource;
import edu.uci.ics.perpetual.statement.add.AddTag;
import edu.uci.ics.perpetual.statement.create.type.*;
import edu.uci.ics.perpetual.types.*;
import edu.uci.ics.perpetual.util.StringUtils;
import edu.uci.ics.perpetual.util.deparser.InsertDeParser;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaManager {

    private Schema schema;

    private Relation relation;

    private SchemaManager() {}

    private static Storage storage;

    private static SchemaManager instance;

    public static SchemaManager getInstance() {
        if (instance == null) {
            instance = new SchemaManager();
            storage = MysqlStorage.getInstance();

            LoadRequest schemaRequest = new LoadRequest(LoadRequest.LoadOption.SCHEMA);
            storage.load(schemaRequest);
            instance.setSchema((Schema) schemaRequest.getResult());


            LoadRequest relationRequest = new LoadRequest(LoadRequest.LoadOption.RELATION);
            storage.load(relationRequest);
            instance.setRelation((Relation) relationRequest.getResult());
        }
        return instance;
    }

    public DataObjectType getDataObjectTypeByDataSourceId(int dataSourceId) {
        return schema.getDataSource(dataSourceId).getSourceType().getReturnType();
    }

    // region Cache Handler
    public void accept(CacheRequest request) {
        if (request.isFindAll()) {

            List<String> result = schema.getRawMap().values().stream()
                    .map(RawType::getName).collect(Collectors.toList());

            request.setAllRawTypes(result);
            request.setStatus(RequestStatus.success());
        } else {
            String typeName = request.getRawTypeName();
            if (!schema.existRawType(typeName)) {
                RequestStatus status = new RequestStatus();
                status.setErrMsg(String.format("Raw Type '%s' does not exist", typeName));
                request.setStatus(status);
                return;
            }

            // Tag Name, and List<String> list of functions names that generate the tag
            HashMap<String, ArrayList<Pair<String, Integer>>> tagFunctionMapping = new HashMap<>();

            // retrieve all tags that parent is typeName
            Set<String> tags = relation.childOf(typeName);

            // loop over all TaggingFunctions
            for (TaggingFunction function : schema.getEnrichmentFunctions().values()) {
                String returnTag = function.getReturnTag();
                if (function.getSourceType().equalsIgnoreCase(typeName) && tags.contains(returnTag)) {
                    if (tagFunctionMapping.get(returnTag) == null) {
                        // initialized new ArrayList
                        tagFunctionMapping.put(returnTag, new ArrayList<>());
                    }
                    Pair<String, Integer> resultPair = new Pair<>(function.getFunctionName(), function.getCost());
                    tagFunctionMapping.get(returnTag).add(resultPair);
                }
            }
            request.setTagFunctionMapping(tagFunctionMapping);
            request.setStatus(RequestStatus.success());
        }
    }
    // endregion

    // region Acquisition Handler
    public void accept(AcquisitionRequest request) {
        RequestStatus status = new RequestStatus();

        int sourceId = request.getDataSourceId();

        if (!schema.existDataSource(sourceId)) {
            status.setErrMsg(String.format("DataSource with id '%s' does not exist", sourceId));
//            request.setStatus(status);
            return;
        }

        DataSource dataSource = schema.getDataSource(sourceId);

        request.setRawTypeScheme(dataSource.getSourceType().getReturnType().getAttributes());
        request.setAcquisitionFunctionPath(dataSource.getFunctionPath());
        request.setAcquisitionFunctionParameters(dataSource.getFunctionParams());

        String pathToJar = dataSource.getFunctionPath();
        String functionName = pathToJar.substring(pathToJar.lastIndexOf('/') + 1, pathToJar.lastIndexOf(".jar"));
        request.setAcquisitionName(functionName);
//        request.setStatus(RequestStatus.success());
    }
    // endregion

    // region SQL handler
    public void accept(final SqlRequest request) {
        RequestStatus status = new RequestStatus();

        request.getStatement().accept(new StatementVisitorAdapter() {

            @Override
            public void visit(Insert insert) {

                String typeName = insert.getType().getName();

                if (!schema.existTable(typeName)) {
                    status.setErrMsg(String.format("Table '%s' does not exist existed", typeName));
                    return;
                }

                storage.addData(insert, schema.getTableMap().get(typeName.toUpperCase()));

            }

            @Override
            public void visit(CreateMetadataType createMetadataType) {
                String typeName = createMetadataType.getType().getName();

                if (schema.existMetadataType(typeName)) {
                    status.setErrMsg(String.format("Metadata Type '%s' has already existed", typeName));
                    return;
                }
                HashMap<String, String> attributes = new HashMap<>();
                for (ColumnDefinition cd : createMetadataType.getColumnDefinitions()) {
                    attributes.put(cd.getColumnName(), cd.getColDataType().getDataType());
                }
                MetadataType metadataType = new MetadataType(typeName, attributes);

                schema.addMetadataType(metadataType);

                storage.persist(new StorageRequest(metadataType));
            }

            @Override
            public void visit(CreateTable createTable) {
                String typeName = createTable.getType().getName();

                if (schema.existTable(typeName)) {
                    status.setErrMsg(String.format("Table '%s' has already existed", typeName));
                    return;
                }
                HashMap<String, String> attributes = new HashMap<>();
                List<String> observables = new ArrayList<>();

                for (ColumnDefinition cd : createTable.getColumnDefinitions()) {
                    attributes.put(cd.getColumnName(), cd.getColDataType().toString());
                    if (cd.getColumnSpecStrings() != null && cd.getColumnSpecStrings().contains("OBSERVABLE")) {
                        observables.add(cd.getColumnName());
                    }
                }
                Table table = new Table(typeName, attributes, observables);

                schema.addTable(table);

                storage.persist(new StorageRequest(table));
            }

            @Override
            public void visit(CreateRawType createRawType) {
                String typeName = createRawType.getType().getName();

                if (schema.existRawType(typeName)) {
                    status.setErrMsg(String.format("Raw Type '%s' has already existed", typeName));
                    return;
                }
                HashMap<String, String> attributes = new HashMap<>();
                for (ColumnDefinition cd : createRawType.getColumnDefinitions()) {
                    attributes.put(cd.getColumnName(), cd.getColDataType().getDataType());
                }
                RawType rawType = new RawType(typeName, attributes);

                schema.addRawType(rawType);

                storage.persist(new StorageRequest(rawType));
            }

            @Override
            public void visit(CreateDataSourceType createDataSourceType) {
                String typeName = createDataSourceType.getType().getName();

                // check existence, and add
                if (schema.existDataSourceType(typeName)) {
                    status.setErrMsg(String.format("DataSource Type '%s' has already existed", typeName));
                    return;
                }

                String returnTypeName = createDataSourceType.getReturnType().getName();
                if (!schema.existRawType(returnTypeName)) {
                    status.setErrMsg(String.format("DataSource '%s' cannot bind return Raw Type '%s', type found",
                            typeName, returnTypeName));
                    return;
                }
                String params = createDataSourceType.getParams();

                DataSourceType dataSourceType = new DataSourceType(typeName, JsonParser.toKeyList(params), schema.getRawType(returnTypeName));

                schema.addDataSourceType(dataSourceType);

                storage.persist(new StorageRequest(dataSourceType));
            }

            @Override
            public void visit(AddAcquisitionFunction addAcquisitionFunction) {
                String typeName = addAcquisitionFunction.getDataSourceType().getName();

                if (!schema.existDataSourceType(typeName)) {
                    status.setErrMsg(String.format("DataSource Type '%s' does not exist", typeName));
                    return;
                }

                DataSourceType dataSourceType = schema.getDataSourceType(typeName);
                String sourceName = StringUtils.removeQuote(addAcquisitionFunction.getName());
                String path = StringUtils.removeQuote(addAcquisitionFunction.getPath());
                dataSourceType.addAcquisitionFunction(sourceName, path);

                storage.update(new StorageRequest(dataSourceType));
            }

            @Override
            public void visit(AddDataSource addDataSource) {
                // check existence of source by id
                int sourceId = addDataSource.getId();
                if (schema.existDataSource(sourceId)) {
                    status.setErrMsg(String.format("DataSource with '%s' has already existed", sourceId));
                    return;
                }

                String typeName = addDataSource.getDataSourceType().getName();

                // check existence of dataSource type
                if (!schema.existDataSourceType(typeName)) {
                    status.setErrMsg(String.format("DataSource Type '%s' does not exist", typeName));
                    return;
                }

                DataSourceType dataSourceType = schema.getDataSourceType(typeName);

                // check existence of acquisition function
                String functionName = StringUtils.removeQuote(addDataSource.getFunctionName());
                if (!dataSourceType.hasAcquisitionFunction(functionName)) {
                    status.setErrMsg(String.format("DataSource Type '%s' does not have a function %s",
                            typeName, functionName));
                    return;
                }

                // check params are conform to dataSource type's param scheme
                HashMap<String, String> sourceParams = JsonParser.toMap(addDataSource.getParams());
                if (!dataSourceType.checkParams(sourceParams.keySet())) {
                    status.setErrMsg(String.format("The scheme of input params does not conform with DataSourceType '%s'",
                            typeName));
                    return;
                }

                // finally, add new source
                DataSource dataSource = new DataSource(
                        sourceId, StringUtils.removeQuote(addDataSource.getName()), dataSourceType,
                        dataSourceType.getAcquisitionFunctionPath(functionName), sourceParams);

                schema.addDataSource(dataSource);

                storage.persist(new StorageRequest(dataSource));
            }

            @Override
            public void visit(CreateFunction createFunction) {
                String funcName = createFunction.getFunction().getName();

                if (schema.existFunction(funcName)) {
                    status.setErrMsg(
                            String.format("Function '%s' already exists", funcName));
                }

                // check existence of source Type
                String rawTypeName = createFunction.getType().getName();
                if (!schema.existRawType(rawTypeName)) {
                    status.setErrMsg(
                            String.format("Function '%s' cannot bind Raw Type '%s', type not found",
                                    funcName, rawTypeName));
                }

                // check existence of parameter Tags
                List<String> params = null;
                List<Tag> paramTags = createFunction.getTags();

                if (paramTags != null && paramTags.size() > 0) {
                    params = new ArrayList<>();
                    for (Tag tag : paramTags) {
                        String name = tag.getName();
                        if (!schema.existTag(name)) {
                            throw new IllegalArgumentException(
                                    String.format("Function '%s' cannot bind Tag '%s', tag found",
                                            funcName, rawTypeName));
                        }
                        params.add(name);
                    }
                }

                // check existence of return Tag
                String returnTagName = createFunction.getReturnTag().getName();

                if (!schema.existTag(returnTagName)) {
                    status.setErrMsg(String.format("Function '%s' cannot bind return Tag '%s', tag not found",
                                    funcName, rawTypeName));
                }

                TaggingFunction function = new TaggingFunction(funcName, rawTypeName, params, returnTagName, createFunction.getCost());
                function.setPath(createFunction.getPath());
                function.setQuality(createFunction.getQuality());
                schema.addFunction(function);

                storage.persist(new StorageRequest(function));
                // add new relation between Raw Type and return Tag
                if (!relation.existRelation(rawTypeName, returnTagName)) {
                    relation.connect(rawTypeName, returnTagName);

                    storage.persist(new StorageRequest(rawTypeName, returnTagName));
                }
            }

            @Override
            public void visit(AddTag addTag) {
                String tagName = addTag.getName();

                if (schema.existTag(tagName)) {
                    status.setErrMsg(
                            String.format("Tag: %s has already existed", tagName));
                }

                String parentName = addTag.getType().getName();

                if (!schema.existRawType(parentName)) {
                    status.setErrMsg(
                            String.format("Cannot associate with Type %s, type not found", parentName));
                }

                String dataType = addTag.getColumnDefinition().getColDataType().getDataType();

                EnrichmentTag tag = new EnrichmentTag(tagName, dataType, addTag.getType().getName());

                schema.addTag(tag);

                storage.persist(new StorageRequest(tag));
                if (!relation.existRelation(parentName, tagName)) {
                    relation.connect(parentName, tagName);
                    storage.persist(new StorageRequest(parentName, tagName));
                }

            }
        });

        if (status.isSuccess()) {
            request.setStatus(RequestStatus.success());
        } else {
            request.setStatus(status);
        }
    }
    // endregion

    // region setter, only used during initialization of SchemaManager
    private void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return this.schema;
    }

    private void setRelation(Relation relation) {
        this.relation = relation;
    }
    // endregion
}

