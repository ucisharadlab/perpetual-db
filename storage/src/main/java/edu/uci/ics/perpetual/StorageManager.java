package edu.uci.ics.perpetual;

import java.util.List;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.predicate.ExpressionPredicate;
import edu.uci.ics.perpetual.types.DataObjectType;

public interface StorageManager {

    public void addRawObject(DataObject object);


    public DataObject getDataObject(DataObjectType type, int id);
    
    public List<DataObject> getDataObjects(DataObjectType type, ExpressionPredicate predicate);



}
