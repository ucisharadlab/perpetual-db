package edu.uci.ics.perpetual.acquisition.utils;

import java.util.TimerTask;

public class ScheduledStopTask extends TimerTask {

    String taskName;

    TimerTask task;

    public ScheduledStopTask(TimerTask task, String name){
        this.task = task; this.taskName = name;
    }

    @Override
    public void run() {
        System.out.println("Stopping the task!!!" );
        task.cancel();
    }
}
