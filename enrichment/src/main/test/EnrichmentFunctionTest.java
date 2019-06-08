import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;
import edu.uci.ics.perpetual.types.DataObjectType;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnrichmentFunctionTest {
//
   String pathToJar =  "/Users/omidkv/IdeaProjects/spring19-cs221-project/perpetual-db/enrichment/test3.jar";


    @Test

    public void test1() {
        EnrichmentFunction function = EnrichmentFunction.getEnrichmentFunction("file://" + pathToJar);

        DataObject dO = new DataObject("{value1 : 1, value2 : 2}", new DataObjectType());

        DataObject d1 = function.execute(dO);

        assertEquals("{\"value1\":1,\"value2\":2,\"Test\":\"Success\"}", d1.getObject().toString());

    }


}