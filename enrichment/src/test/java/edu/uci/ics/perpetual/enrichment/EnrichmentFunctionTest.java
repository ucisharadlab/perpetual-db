package edu.uci.ics.perpetual.enrichment;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.types.DataObjectType;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EnrichmentFunctionTest {

    @Test
    public void test001() {
        String pathToJar = "src/test/java/edu/uci/ics/perpetual/enrichment/resources/EnrichmentFunctionTest/Test001.jar";
        File file = new File(pathToJar);
        EnrichmentFunction function = EnrichmentFunction.getEnrichmentFunction("file://" + file.getAbsolutePath());
        DataObject dO = new DataObject("{value1 : 1, value2 : 2}", new DataObjectType());
        DataObject d1 = function.execute(dO);
        assertEquals("{\"value1\":1,\"value2\":2,\"Test\":\"Success\"}", d1.getObject().toString());

    }
}