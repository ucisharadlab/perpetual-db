package edu.uci.ics.perpetual.acquisition;

import edu.uci.ics.perpetual.acquisition.requestmanagement.AcquisitionRequestManager;
import edu.uci.ics.perpetual.acquisition.utils.AcquisitionConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.Java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import static edu.uci.ics.perpetual.acquisition.utils.AcquisitionConfig.config;
public class AcquisitionManager {

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
    public Object getData(String requestId){
        // TODO implement function - get complete data structure updated by various consumers.
        consumer = new KafkaConsumer<Object, Object>( kafkaConfigs);    // consumer
        consumer.subscribe(Arrays.asList(requestId));      // topic
        ConsumerRecords<Object, Object> records  = consumer.poll(100);
        ArrayList<Object> data = new ArrayList<Object>();
        for (ConsumerRecord<Object, Object> record : records)
        {
            data.add( record.value() );
        }
        consumer.close();
        return data;
    }

    public AcquisitionRequestManager getRequestManager(){
        return requestManager;
    }

}
