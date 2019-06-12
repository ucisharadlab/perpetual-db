package edu.uci.ics.perpetual.acquisition.datatypes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import static edu.uci.ics.perpetual.acquisition.utils.AcquisitionConfig.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
public abstract class Producer {

    protected AcquisitionRequest request;
    private KafkaProducer<Object, Object> producer;

    public Producer(AcquisitionRequest request){
        // shared kafka initialization
        Properties configs = new Properties();
        configs.put("bootstrap.servers", config.get("bootstrap.servers")); // set kafka host and server
        configs.put("acks", config.get("acks"));                         // Do not wait confirmation of sent message from kafka
        configs.put("block.on.buffer.full", config.get("block.on.buffer.full"));
        configs.put("key.serializer", config.get("key.serializer"));   // set serializer
        configs.put("value.serializer", config.get("value.serializer")); // set serializer
        configs.put("value.serializer.encoding","UTF8");


        this.request = request;
        // Generate Kafka Producer
        producer = new KafkaProducer<Object, Object>(configs);
    }

    public void sendMessage(long idx , Object object){
        System.out.println("Sending: "+ object);
        producer.send( new ProducerRecord <Object,Object>( request.getRequestId() + "",idx, object ));
    }

    public abstract void fetch() throws Exception;

    public void close(){
        System.out.println("Stopping the Kafka Producer topicL " + request.getRequestId());
        producer.flush();
        producer.close();
    }

    protected Map<String,Object> getMapFromJSON(String json) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }

}
