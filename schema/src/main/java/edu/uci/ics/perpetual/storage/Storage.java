package edu.uci.ics.perpetual.storage;

import edu.uci.ics.perpetual.request.LoadRequest;
import edu.uci.ics.perpetual.request.StorageRequest;

/**
 * Storage defines the interface that needs to be implement by persistent storage
 */
public interface Storage {

    void load(LoadRequest request);

    void persist(StorageRequest request);

    void update(StorageRequest request);
}
