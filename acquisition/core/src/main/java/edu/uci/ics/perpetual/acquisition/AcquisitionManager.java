package edu.uci.ics.perpetual.acquisition;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class AcquisitionManager {

    AcquisitionManager instance;
    KafkaConsumer<Object, Object> consumer;
    private Properties kafkaConfigs;
    private AcquisitionManager(){
         kafkaConfigs = new Properties();
        // Setting environment variables.
        kafkaConfigs.put("bootstrap.servers", kafkaConfigs.get("bootstrap.servers"));     // kafka server host and port
        kafkaConfigs.put("key.deserializer", kafkaConfigs.get("key.deserializer"));    // key deserializer
        kafkaConfigs.put("value.deserializer", kafkaConfigs.get("value.deerializer"));  // value deserializer
    }
    public AcquisitionManager getInstance(){
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

}
