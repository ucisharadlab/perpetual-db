
package edu.uci.ics.perpetual.statement.create.type;

import java.util.List;

import edu.uci.ics.perpetual.schema.Type;
import edu.uci.ics.perpetual.statement.select.PlainSelect;

/**
 * An index (unique, primary etc.) in a CREATE TABLE statement
 */
public class Index {

    private String indexType;
    private Type type;
    private List<String> columnsNames;
    private String name;
    private List<String> idxSpec;

    /**
     * A list of strings of all the columns regarding this index
     */
    public List<String> getColumnsNames() {
        return columnsNames;
    }

    public String getName() {
        return name;
    }

    /**
     * The type of this index: "PRIMARY KEY", "UNIQUE", "INDEX"
     */
    public Type getType() {
        return type;
    }

    public void setColumnsNames(List<String> list) {
        columnsNames = list;
    }

    public void setName(String string) {
        name = string;
    }

    public void setType(Type string) {
        type = string;
    }

    public List<String> getIndexSpec() {
        return idxSpec;
    }

    public void setIndexSpec(List<String> idxSpec) {
        this.idxSpec = idxSpec;
    }

    @Override
    public String toString() {
        String idxSpecText = PlainSelect.getStringList(idxSpec, false, false);
        return type + (name != null ? " " + name : "") + " " + PlainSelect.
                getStringList(columnsNames, true, true) + (!"".equals(idxSpecText) ? " " + idxSpecText : "");
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }
}
