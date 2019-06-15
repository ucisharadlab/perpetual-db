import edu.uci.ics.perpetual.acquisition.datatypes.Producer;
import edu.uci.ics.perpetual.request.AcquisitionRequest;

import java.util.Random;

public class TwitterProducer extends Producer {

    public TwitterProducer(AcquisitionRequest request) {
        super(request);
    }

    public void fetch() throws Exception {

        Random r = new Random();

        while(true) {
            String jsonString = "{\"temperature\":" + r.nextInt(100) + ",\"R1\":" + r.nextInt(10000) + "}";
            System.out.println("PRODUCER UDF : Sending..." + jsonString);
            sendMessage(0, jsonString);
            System.out.println("Sending data");
            Thread.sleep(1000);
        }

    }


}