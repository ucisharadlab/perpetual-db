import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;

public class Test001 extends EnrichmentFunction {

    public DataObject enrich(DataObject dataObject) {
        dataObject.getObject().addProperty("Test", "Success");
        return dataObject;
    }
}
