package edu.uci.ics.perpetual.common;

public class Interval {

    private long start;
    private long end;

    public Interval(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean isOverlap(Interval interval) {

        return interval.start <= this.end && interval.end >= this.start;

    }

    public Interval merge(Interval interval) {
        if (!isOverlap(interval))
            return null;

        return new Interval(Math.min(this.start, interval.start), Math.max(this.end, interval.end));
    }

    public Interval intersection(Interval interval) {
        if (!isOverlap(interval))
            return null;

        return new Interval(Math.max(this.start, interval.start), Math.min(this.end, interval.end));
    }


}
