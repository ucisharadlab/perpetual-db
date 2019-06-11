package edu.uci.ics.perpetual.storage;

import edu.uci.ics.perpetual.Schema;
import edu.uci.ics.perpetual.request.LoadRequest;
import edu.uci.ics.perpetual.request.StorageRequest;

import java.util.HashMap;

public interface Storage {

    void load(LoadRequest request);

    void persist(StorageRequest request);

    void update(StorageRequest request);
}
