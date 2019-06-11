package edu.uci.ics.perpetual;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.parser.JsonParser;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.junit.Assert.assertTrue;

public class TestJsonParser {

    /**
     * normal test
     */
    @Test
    public void test() {
        String json = "'{ip:'1.1.1.1', port:2000}'";

        JsonObject object = JsonParser.toJsonObject(json);

        assertTrue(object.has("ip"));
        assertEquals("1.1.1.1", object.get("ip").getAsString());
        assertTrue(object.has("port"));
        assertEquals("2000", object.get("port").getAsString());

    }

    /**
     * Test: correctly handle ',' in value
     */
    @Test
    public void test1() {
        String json = "'{ip:'1.1.1.1', port:2000, name:'Dong, Ryan'}'";

        JsonObject object = JsonParser.toJsonObject(json);

        assertTrue(object.has("ip"));
        assertEquals("1.1.1.1", object.get("ip").getAsString());
        assertTrue(object.has("port"));
        assertEquals("2000", object.get("port").getAsString());
        assertTrue(object.has("name"));
        assertEquals("Dong, Ryan", object.get("name").getAsString());
    }

    /**
     * Test: correctly handle float value
     */
    @Test
    public void test2() {
        String json = "'{ score : 95.25}'";

        JsonObject object = JsonParser.toJsonObject(json);

        assertTrue(object.has("score"));
        assertEquals("95.25", object.get("score").getAsString());

    }
}
