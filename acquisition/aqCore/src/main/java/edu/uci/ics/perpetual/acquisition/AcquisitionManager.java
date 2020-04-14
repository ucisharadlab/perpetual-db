package edu.uci.ics.perpetual.acquisition;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.perpetual.acquisition.requestmanagement.AcquisitionRequestManager;
import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.request.AcquisitionRequestStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static edu.uci.ics.perpetual.acquisition.utils.AcquisitionConfig.config;

public class AcquisitionManager {

    Logger LOGGER = Logger.getLogger(AcquisitionManager.class);

    private static AcquisitionManager instance;
    KafkaConsumer<Object, Object> consumer;
    final AcquisitionRequestManager requestManager = AcquisitionRequestManager.getInstance();

    private Properties kafkaConfigs;
    private AcquisitionManager(){
         kafkaConfigs = new Properties();
        // Setting environment variables.
        kafkaConfigs.put("bootstrap.servers", config.get("bootstrap.servers"));     // kafka server host and port
        kafkaConfigs.put("key.deserializer", config.get("key.deserializer"));    // key deserializer
        kafkaConfigs.put("value.deserializer", config.get("value.deserializer"));  // value deserializer
        kafkaConfigs.put("group.id","acquisition");
    }
    public static AcquisitionManager getInstance(){
        if(null != instance){
            return instance;
        }
        instance = new AcquisitionManager();
        return instance;
    }

    /**
     * Invoked by Ingestion Engine
     */
    public ArrayList<DataObject> getData(int requestId) throws JsonParseException, IOException {
        KafkaConsumer<Object, Object> consumer;
        consumer = new KafkaConsumer<Object, Object>( kafkaConfigs);    // consumer
        consumer.subscribe(Arrays.asList(requestId+""));      // topic
        ConsumerRecords<Object, Object> records  = consumer.poll(100);

        ArrayList<DataObject> data = new ArrayList<DataObject>();
        if(records.isEmpty() && requestManager.getRequestStatus( requestId ) == AcquisitionRequestStatus.DONE){
            return null;
        }
        for (ConsumerRecord<Object, Object> record : records)
        {
            LOGGER.info( "ACQUISITION ENGINE: Found: " + record.toString() );
            ObjectMapper om = new ObjectMapper();
            JsonFactory factory = om.getFactory();
            JsonParser parser = factory.createParser(record.value().toString());
            JsonNode rec = om.readTree( parser );
            data.add( new DataObject(rec.toString() ,
                    requestManager.getRequestDataSourceType(requestId))
            );
        }
        consumer.close();
        LOGGER.info( "ACQUISITION ENGINE: returning data of size: " + data.size()  + " to the ingestion engine for processing");
        return data;
    }

    public AcquisitionRequestManager getRequestManager(){
        return requestManager;
    }

}
