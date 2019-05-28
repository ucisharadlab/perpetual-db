
package edu.uci.ics.perpetual.statement.create.type;

import edu.uci.ics.perpetual.statement.select.PlainSelect;

import java.util.ArrayList;
import java.util.List;

public class ColDataType {

    private String dataType;
    private List<String> argumentsStringList;
    private String characterSet;
    private List<Integer> arrayData = new ArrayList<Integer>();

    public List<String> getArgumentsStringList() {
        return argumentsStringList;
    }

    public String getDataType() {
        return dataType;
    }

    public void setArgumentsStringList(List<String> list) {
        argumentsStringList = list;
    }

    public void setDataType(String string) {
        dataType = string;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public List<Integer> getArrayData() {
        return arrayData;
    }

    public void setArrayData(List<Integer> arrayData) {
        this.arrayData = arrayData;
    }

    @Override
    public String toString() {
        StringBuilder arraySpec = new StringBuilder();
        for (Integer item : arrayData) {
            arraySpec.append("[");
            if (item != null) {
                arraySpec.append(item);
            }
            arraySpec.append("]");
        }
        return dataType
                + (argumentsStringList != null ? " " + PlainSelect.
                                getStringList(argumentsStringList, true, true) : "")
                + arraySpec.toString()
                + (characterSet != null ? " CHARACTER SET " + characterSet : "");
    }
}
