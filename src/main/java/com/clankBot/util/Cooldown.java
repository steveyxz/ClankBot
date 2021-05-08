package com.clankBot.util;

import java.util.Date;

import static com.clankBot.util.GlobalMethods.parseStringToMilli;

public class Cooldown {

    private long milliseconds;
    private long timestamp;

    /**
     * Creates a Cooldown instance USING THE FORMAT SHOWN BELOW.
     *
     * @param time The time specified in mute format, like 3m 4m 5s 3d,
     *             which would convert to 3 days, 7 minutes and 5 seconds..
     */
    public Cooldown(String time) {
        this(parseStringToMilli(time));
    }

    public Cooldown(long milliseconds) {
        this.milliseconds = milliseconds;
        this.timestamp = new Date().getTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public boolean checkCompletion() {
        return new Date().getTime() >= timestamp + milliseconds;
    }

    public long timeTillDone() {
        return milliseconds - (new Date().getTime() - timestamp);
    }
}
