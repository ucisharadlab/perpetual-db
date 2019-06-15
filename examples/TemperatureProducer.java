import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uci.ics.perpetual.acquisition.datatypes.AcquisitionRequest;
import edu.uci.ics.perpetual.acquisition.datatypes.Producer;

public class TemperatureProducer extends Producer{

	private static final String COMMA_DELIMITER = ",";


	public TemperatureProducer(AcquisitionRequest request) {
		super(request);
	}

	@Override
	public void fetch() throws IOException{
		System.out.println("fetching temperatures");
		BufferedReader br = null;
		try {
			Map<String, String> params = request.getAcquisitionFunctionParameters();
			String tempFilePath = params.get("filePath");
			br = new BufferedReader(new FileReader(tempFilePath));
			String line;
			int idx = 0;
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITER);
				String jsonString = "{\"temperature\":"+values[1]+",\"R1\":"+values[2]+"}";
				System.out.println("PRODUCER UDF : Sending..." + jsonString);
				sendMessage(idx,jsonString);
			}
		} catch (Exception e) {
			System.out.println("PRODUCER UDF : Error" + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if(null != br) {
				br.close();
			}
		}
	}


}