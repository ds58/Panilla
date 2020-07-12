package com.ruinscraft.panilla.api.exception;

public class RateLimitException extends Exception {

    private String packetClassName;
    private long pps;

    public RateLimitException(String packetClassName, long pps) {
        this.packetClassName = packetClassName;
        this.pps = pps;
    }

    public String getPacketClassName() {
        return packetClassName;
    }

    public long getPPS() {
        return pps;
    }

}
