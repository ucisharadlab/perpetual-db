package edu.uci.ics.perpetual.request;

import java.util.HashMap;

public class AcquisitionRequest {

    // provided
    private int requestId;
    private int dataSourceId;


    private String acquistionFunctionPath;

    private HashMap<String, String> acquistionFunctionParameters;

    private HashMap<String, String> rawTypeScheme;


}
