package com.uci.perpetualdb.acquisition;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class AcquisitionManager {

    AcquisitionManager instance;
    KafkaConsumer<Object, Object> consumer;
    Properties kafkaconfigs;
    private AcquisitionManager(){
         kafkaconfigs = new Properties();
        // 환경 변수 설정
        kafkaconfigs.put("bootstrap.servers", kafkaconfigs.get("bootstrap.servers"));     // kafka server host 및 port
        kafkaconfigs.put("key.deserializer", kafkaconfigs.get("key.deserializer"));    // key deserializer
        kafkaconfigs.put("value.deserializer", kafkaconfigs.get("value.deerializer"));  // value deserializer
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
    public Object getData(){
        // TODO implement function - get complete data structure updated by various consumers.

        consumer = new KafkaConsumer<Object, Object>(kafkaconfigs);    // consumer 생성
        consumer.subscribe(Arrays.asList("TOPIC NAME"));      // topic 설정

        return null;
    }

}
