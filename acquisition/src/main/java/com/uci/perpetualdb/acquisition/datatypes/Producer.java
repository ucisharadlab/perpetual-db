package com.uci.perpetualdb.acquisition.datatypes;

public abstract class Producer {

    Request request;

    public Producer(Request request){
        // TODO  shared kafka initialization
    }

    public void sendMessage(Object object){
        // TODO  pass object to kafka
    }

    public abstract void fetch();
}
