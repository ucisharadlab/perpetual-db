package com.uci.perpetualdb.acquisition.datatypes;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
public abstract class Producer {

    Request request;
    KafkaProducer<Object, Object> producer;

    public Producer(Request request){
        // TODO  shared kafka initialization
        Properties configs = new Properties();
        configs.put("bootstrap.servers", configs.get("bootstrap.servers")); // set kafka host and server
        configs.put("acks", configs.get("acks"));                         // Do not wait confirmation of sent message from kafka
        configs.put("block.on.buffer.full", configs.get("block.on.buffer.full"));
        configs.put("key.serializer", configs.get("key.serializer"));   // set serializer
        configs.put("value.serializer", configs.get("value.serializer")); // set serializer

        // Generate Kafka Producer
        producer = new KafkaProducer<Object, Object>(configs);

        //Massage here

        producer.flush();
        producer.close();
    }

    static {

    }
    public void sendMessage(long idx , Object object){
        producer.send( new ProducerRecord <Object,Object>( request.getReqId() + "",idx, object ));
    }

    public abstract void fetch();
}
