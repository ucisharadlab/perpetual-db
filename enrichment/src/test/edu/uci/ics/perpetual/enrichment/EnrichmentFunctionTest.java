package edu.uci.ics.perpetual.enrichment;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.types.DataObjectType;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EnrichmentFunctionTest {

    @Test
    public void test001() {
        String pathToJar = "src/test/edu/uci/ics/perpetual/enrichment/resources/EnrichmentFunctionTest/Enrichment.jar";
        File file = new File(pathToJar);
        EnrichmentFunction function = EnrichmentFunction.getEnrichmentFunction("Enrichment", "file://" + file.getAbsolutePath());
        DataObject dO = new DataObject("{value1 : 1, value2 : 2}", new DataObjectType());
        function.execute(dO);
        assertEquals("{\"value1\":1,\"value2\":2,\"Enrichment\":\"Success\"}", dO.getObject().toString());

    }
}