package com.uci.perpetualdb.acquisition.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class ObjectSerializer implements org.apache.kafka.common.serialization.Serializer {

    public void configure(Map map, boolean b) {

    }

    public byte[] serialize(String s, Object o) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);

            byte[] b = baos.toByteArray();
            oos.close();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public void close() {

    }

}