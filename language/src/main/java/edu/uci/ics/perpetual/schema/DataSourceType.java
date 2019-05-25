
package edu.uci.ics.perpetual.schema;

import edu.uci.ics.perpetual.expression.MySQLIndexHint;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A table. It can have an alias and the schema name it belongs to.
 */
public class DataSourceType extends ASTNodeAccessImpl {

//    private Database database;
//    private String schemaName;
//    private String name;
    private static final int NAME_IDX = 0;
    private static final int SCHEMA_IDX = 1;
    private static final int DATABASE_IDX = 2;
    private static final int SERVER_IDX = 3;

    private List<String> partItems = new ArrayList<>();

    private MySQLIndexHint hint;
    private String params;

    public DataSourceType() {
    }

    public DataSourceType(String name) {
        setIndex(NAME_IDX, name);
    }

    public DataSourceType(String schemaName, String name) {
        setIndex(NAME_IDX, name);
        setIndex(SCHEMA_IDX, schemaName);
    }

    public DataSourceType(Database database, String schemaName, String name) {
        setIndex(NAME_IDX, name);
        setIndex(SCHEMA_IDX, schemaName);
        setIndex(DATABASE_IDX, database.getDatabaseName());
        setIndex(SERVER_IDX, database.getServer().getFullyQualifiedName());
    }

    public DataSourceType(List<String> partItems) {
        this.partItems = new ArrayList<>(partItems);
        Collections.reverse(this.partItems);
    }

    public Database getDatabase() {
        return new Database(getIndex(DATABASE_IDX));
    }

    public void setDatabase(Database database) {
        setIndex(DATABASE_IDX, database.getDatabaseName());
    }

    public String getSchemaName() {
        return getIndex(SCHEMA_IDX);
    }

    public void setSchemaName(String string) {
        setIndex(SCHEMA_IDX, string);
    }

    public String getName() {
        return getIndex(NAME_IDX);
    }

    public void setName(String string) {
        setIndex(NAME_IDX, string);
    }

    private void setIndex(int idx, String value) {
        for (int i = 0; i < idx - partItems.size() + 1; i++) {
            partItems.add(null);
        }
        partItems.set(idx, value);
    }

    private String getIndex(int idx) {
        if (idx < partItems.size()) {
            return partItems.get(idx);
        } else {
            return null;
        }
    }


    public MySQLIndexHint getIndexHint() {
        return hint;
    }

    public void setHint(MySQLIndexHint hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
