package edu.uci.ics.perpetual.workload;

import edu.uci.ics.perpetual.caching.WorkloadType;
import edu.uci.ics.perpetual.workload.extractor.IExtractInfo;
import edu.uci.ics.perpetual.workload.extractor.IExtractor;
import edu.uci.ics.perpetual.workload.extractor.QueryBotExtractor;
import edu.uci.ics.perpetual.workload.parser.IWorkloadParser;
import edu.uci.ics.perpetual.workload.parser.QueryBotWorkloadParser;
import org.apache.commons.lang3.NotImplementedException;

public class WorkloadManager implements Runnable {

    private String dataDir;
    private IWorkloadParser workloadParser;
    private IExtractor extractor;

    private IExtractInfo extractInfo;
    private int sleepInterval;

    public WorkloadManager(String dataDir, WorkloadType wType, int sleepInterval) {

        this.dataDir = dataDir;
        this.sleepInterval = sleepInterval;

        switch (wType) {
            case QueryBot:
                workloadParser = new QueryBotWorkloadParser(dataDir);
                extractor = new QueryBotExtractor(workloadParser);
                break;
            case Twitter:
                throw new NotImplementedException("");
            default:
                break;
        }

    }

    @Override
    public void run() {

        while (true) {
            extractInfo = extractor.extractAll();
            System.out.print(extractInfo);
            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public IExtractInfo getExtractInfo() {
        return extractInfo;
    }

}
