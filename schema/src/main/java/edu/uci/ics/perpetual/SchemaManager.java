package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.function.TaggingFunction;
import edu.uci.ics.perpetual.parser.JsonParser;
import edu.uci.ics.perpetual.schema.IType;
import edu.uci.ics.perpetual.schema.Tag;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitorAdapter;
import edu.uci.ics.perpetual.statement.add.AddAcquisitionFunction;
import edu.uci.ics.perpetual.statement.add.AddDataSource;
import edu.uci.ics.perpetual.statement.add.AddTag;
import edu.uci.ics.perpetual.statement.create.type.*;
import edu.uci.ics.perpetual.table.Attribute;
import edu.uci.ics.perpetual.table.AttributeKind;
import edu.uci.ics.perpetual.types.DataSourceType;
import edu.uci.ics.perpetual.types.EnrichmentTag;
import edu.uci.ics.perpetual.types.MetadataType;
import edu.uci.ics.perpetual.types.RawType;

import java.util.ArrayList;
import java.util.List;

public class SchemaManager {

    private Schema schema;

    private Relation relation;

    private SchemaManager() {}

    private static SchemaManager instance;

    public static SchemaManager getInstance() {
        if (instance == null) {
            instance = new SchemaManager();
            instance.setSchema(new Schema());
            instance.setRelation(new Relation());
        }
        return instance;
    }
    
    // region SQL handler
    public void accept(Statement statement) {
        statement.accept(new StatementVisitorAdapter() {
            @Override
            public void visit(CreateMetadataType createMetadataType) {
                String typeName = createMetadataType.getType().getName();

                if (schema.existMetadataType(typeName)) {
                    throw new IllegalArgumentException();
                }
                List<Attribute> attributes = new ArrayList<>();
                for (ColumnDefinition cd : createMetadataType.getColumnDefinitions()) {
                    attributes.add(Attribute.parameterAttribute(
                            cd.getColumnName(), cd.getColDataType().getDataType()));
                }
                schema.addMetadataType(new MetadataType(typeName, attributes));
            }

            @Override
            public void visit(CreateRawType createRawType) {
                String typeName = createRawType.getType().getName();

                if (schema.existRawType(typeName)) {
                    throw new IllegalArgumentException(
                            String.format("Raw Type: %s has already existed", typeName));
                }
                List<Attribute> attributes = new ArrayList<>();
                for (ColumnDefinition cd : createRawType.getColumnDefinitions()) {
                    attributes.add(Attribute.parameterAttribute(
                            cd.getColumnName(), cd.getColDataType().getDataType()));
                }
                schema.addRawType(new RawType(typeName, attributes));
            }

            @Override
            public void visit(CreateDataSourceType createDataSourceType) {
                String sourceName = createDataSourceType.getType().getName();

                // check existence, and add
                if (schema.existDataSource(sourceName)) {
                    throw new IllegalArgumentException(
                            String.format("DataSource Type: %s has already existed", sourceName));
                }
                String params = createDataSourceType.getParams();

                schema.addSource(new DataSourceType(sourceName, JsonParser.toKeyList(params)));
            }

            @Override
            public void visit(AddAcquisitionFunction addAcquisitionFunction) {
                String typeName = addAcquisitionFunction.getDataSourceType().getName();

                if (!schema.existDataSource(typeName)) {
                    throw new IllegalArgumentException(
                            String.format("DataSource Type: %s does not exist", typeName));
                }

                DataSourceType dataSourceType = schema.getSource(typeName);
                String sourceName = addAcquisitionFunction.getName();
                String path = addAcquisitionFunction.getPath();
                dataSourceType.addAcquisitionFunction(sourceName, path);
            }

            @Override
            public void visit(AddDataSource addDataSource) {
                String typeName = addDataSource.getDataSourceType().getName();

                // check existence of dataSource type
                if (!schema.existDataSource(typeName)) {
                    throw new IllegalArgumentException(
                            String.format("DataSource Type: %s does not exist", typeName));
                }

                DataSourceType dataSourceType = schema.getSource(typeName);

                // check existence of acquisition function
                String functionName = addDataSource.getFunctionName();
                if (dataSourceType.hasSourceFunctions(functionName)) {
                    throw new IllegalArgumentException(
                            String.format("DataSource Type: %s does not have a function %s",
                                    typeName, functionName));
                }

                // check existence of source by id
                int sourceId = addDataSource.getId();
                if (dataSourceType.hasSource(sourceId)) {
                    throw new IllegalArgumentException(
                            String.format("DataSource Type: %s already has a source id %d, named %s",
                                    typeName, sourceId, dataSourceType.getSourceName(sourceId)));
                }

                // check params are conform to dataSource type's param scheme
                List<Attribute> sourceParams = JsonParser.toAttribute(addDataSource.getParams(), AttributeKind.VALUE);
                if (!dataSourceType.checkParams(sourceParams)) {
                    throw new IllegalArgumentException(
                            String.format("The scheme of input params does not conform with DataSourceType %s",
                                    typeName));
                }

                // finally, add new source
                dataSourceType.addSource(sourceId, addDataSource.getName(), sourceParams);
            }

            @Override
            public void visit(CreateFunction createFunction) {
//                "CREATE FUNCTION images_to_objects(Images, person) RETURNS Objects COST 50";

                String funcName = createFunction.getFunction().getName();

                if (schema.existFunction(funcName)) {
                    throw new IllegalArgumentException(
                            String.format("Function: %s already exists", funcName));
                }

                // check existence of source Type
                String sourceType = createFunction.getType().getName();
                if (!schema.existRawType(sourceType)) {
                    throw new IllegalArgumentException(
                            String.format("Function %s: cannot bind Raw Type: %s, type not found",
                                    funcName, sourceType));
                }

                // rest arguments
                List<Tag> paramTags = createFunction.getTags();

                String returnTagName = createFunction.getReturnTag().getName();
                TaggingFunction function = new TaggingFunction(createFunction.getFunction().getName(),
                        createFunction.getTags(), createFunction.getType().getName(), createFunction.getCost());
                schema.addFunction(function);

//                TaggingFunction function = new TaggingFunction(createFunction.getFunction().getName());
//                function.addParameter(createFunction.getType().getName());
//
//                createFunction.getTags().forEach(tag -> function.addParameter(tag.getName()));
//
//                String typeName = createFunction.getType().getName();
//                if (schema.exist(Tag.RAW, typeName) || schema.exist(Tag.ENRICHMENT, typeName)) {
//                    // this means that one of function's parameter is a Raw Type or Enrichment Type
//                    if (!relations.containsKey(typeName)) {
//                        HashSet<String> newSet = new HashSet<>();
//                        newSet.add(createFunction.getReturnTag().getName());
//                        relations.put(typeName, newSet);
//                    } else {
//                        relations.get(typeName).add(createFunction.getReturnTag().getName());
//                    }
//                }
            }

            @Override
            public void visit(AddTag addTag) {
                String tagName = addTag.getName();

                if (schema.existTag(tagName)) {
                    throw new IllegalArgumentException(
                            String.format("Tag: %s has already existed", tagName));
                }

                String parentName = addTag.getType().getName();

                if (!schema.existRawType(parentName)) {
                    throw new IllegalArgumentException(
                            String.format("Cannot associate with Type %s, type not found", parentName));
                }

                String dataType = addTag.getColumnDefinition().getColDataType().getDataType();

                schema.addTag(new EnrichmentTag(tagName, dataType, addTag.getType().getName()));
                relation.connect(parentName, tagName);
            }
        });
    }
    // endregion

    // region setter, only used during initialization
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

