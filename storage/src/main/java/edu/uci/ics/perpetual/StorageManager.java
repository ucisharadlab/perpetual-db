package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.types.DataObjectType;

public interface StorageManager {

    public void addRawObject(DataObject object);


    public DataObject getDataObject(int id, String type);



}
