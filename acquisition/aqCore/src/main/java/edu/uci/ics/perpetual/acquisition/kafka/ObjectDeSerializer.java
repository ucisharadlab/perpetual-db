package edu.uci.ics.perpetual.acquisition.kafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class ObjectDeSerializer implements org.apache.kafka.common.serialization.Deserializer{

    public void configure(Map map, boolean b) {

    }

    public void close() {

    }

    @Override
    public Object deserialize(String arg0, byte[] arg1) {
        try {
            ByteArrayInputStream baos = new ByteArrayInputStream(arg1);
            ObjectInputStream oos = new ObjectInputStream(baos);
            Object b = oos.readObject();
            oos.close();
            return b;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}