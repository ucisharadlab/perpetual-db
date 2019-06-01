package edu.uci.ics.perpetual.acquisition.utils;

import java.util.TimerTask;

public class ScheduledStopTask extends TimerTask {


    TimerTask task;

    public ScheduledStopTask(TimerTask task){
        this.task = task;
    }

    @Override
    public void run() {
        task.cancel();
    }
}
