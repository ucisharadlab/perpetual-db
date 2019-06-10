package edu.uci.ics.perpetual.enrichement;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.functions.EnrichmentFunction;
import edu.uci.ics.perpetual.types.DataObjectType;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class EnrichmentFunctionTest {

    @Test
    public void test001() {
        String pathToJar = "src/test/edu/uci/ics/perpetual/enrichment/resources/EnrichmentFunctionTest/Enrichment.jar";
        File filbefore = new File(pathToJar);
        System.out.println(filbefore.getAbsolutePath());
        pathToJar = "/home/mikhail/workspace/intellij-workspace/perpetual-db/enrichment/src/test/edu/uci/ics/perpetual/enrichement/resources/EnrichmentFunctionTest/Enrichment.jar";
        System.out.println(pathToJar);
        File file = new File(pathToJar);
        System.out.println(file.getAbsolutePath());
        EnrichmentFunction function = EnrichmentFunction.getEnrichmentFunction("file://" + file.getAbsolutePath());
        DataObject dO = new DataObject("{value1 : 1, value2 : 2}", new DataObjectType());
        DataObject d1 = function.execute(dO);
        assertEquals("{\"value1\":1,\"value2\":2,\"Test\":\"Success\"}", d1.getObject().toString());

    }
}