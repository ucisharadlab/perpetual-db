package edu.uci.ics.perpetual.storage;

import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.request.LoadRequest;
import edu.uci.ics.perpetual.request.StorageRequest;
import edu.uci.ics.perpetual.statement.insert.Insert;
import edu.uci.ics.perpetual.types.Table;

import java.util.HashMap;

public interface Storage {

    void load(LoadRequest request);

    void persist(StorageRequest request);

    void update(StorageRequest request);

    void addData(Insert insert, Table table);
}
