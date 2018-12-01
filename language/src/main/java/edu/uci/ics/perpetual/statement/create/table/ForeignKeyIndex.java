
package edu.uci.ics.perpetual.statement.create.table;

import java.util.List;
import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.select.PlainSelect;

/**
 * Foreign Key Index
 *
 * @author toben
 */
public class ForeignKeyIndex extends NamedConstraint {

    private Table table;
    private List<String> referencedColumnNames;
    private String onDeleteReferenceOption;
    private String onUpdateReferenceOption;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<String> getReferencedColumnNames() {
        return referencedColumnNames;
    }

    public void setReferencedColumnNames(List<String> referencedColumnNames) {
        this.referencedColumnNames = referencedColumnNames;
    }

    public String getOnDeleteReferenceOption() {
        return onDeleteReferenceOption;
    }

    public void setOnDeleteReferenceOption(String onDeleteReferenceOption) {
        this.onDeleteReferenceOption = onDeleteReferenceOption;
    }

    public String getOnUpdateReferenceOption() {
        return onUpdateReferenceOption;
    }

    public void setOnUpdateReferenceOption(String onUpdateReferenceOption) {
        this.onUpdateReferenceOption = onUpdateReferenceOption;
    }

    @Override
    public String toString() {
        String referenceOptions = "";
        if (onDeleteReferenceOption != null) {
            referenceOptions += " ON DELETE " + onDeleteReferenceOption;
        }
        if (onUpdateReferenceOption != null) {
            referenceOptions += " ON UPDATE " + onUpdateReferenceOption;
        }
        return super.toString()
                + " REFERENCES " + table + PlainSelect.
                        getStringList(getReferencedColumnNames(), true, true)
                + referenceOptions;
    }
}
