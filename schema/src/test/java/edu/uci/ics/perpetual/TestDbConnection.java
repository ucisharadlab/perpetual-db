package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.storage.MysqlStorage;
import org.junit.Test;

public class TestDbConnection {

    @Test
    public void testDB() {
        MysqlStorage storage = MysqlStorage.getInstance();
    }
}
