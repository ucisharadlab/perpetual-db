package edu.uci.ics.perpetual;

import com.google.gson.JsonObject;
import edu.uci.ics.perpetual.parser.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestJsonParser {

    /**
     * normal test
     */
    @Test
    public void test() {
        String json = "{ip:'1.1.1.1', port:2000}";

        JsonObject object = JsonParser.toJsonObject(json);

        assertTrue(object.has("ip"));
        assertTrue(object.has("port"));
    }

    /**
     * Test: correctly handle ',' in value
     */
    @Test
    public void test1() {
        String json = "{ip:'1.1.1.1', port:2000, name:'Dong, Ryan'}";

        JsonObject object = JsonParser.toJsonObject(json);

        for (String key : object.keySet()) {
            System.out.println(key);
        }

//        (\w[\w\d]*:^\w\b$)|(\w[\w\d]*:\d*.\d*)
        assertTrue(object.has("ip"));
        assertTrue(object.has("port"));
        assertTrue(object.has("name"));
    }

    /**
     * Test: correctly handle float value
     */
    @Test
    public void test2() {
        String json = "{score:95.25}";

        JsonObject object = JsonParser.toJsonObject(json);

        for (String key : object.keySet()) {
            System.out.println(key);
        }

        assertTrue(object.has("score"));
    }
}
