package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.RateLimitException;

import java.util.LinkedList;
import java.util.Queue;

public class PacketRateLimiter {

    private static final int MAX_LOG_SIZE = 10;

    private final String packetClassName;
    private final int ppsLimit;

    private Queue<Long> ppsLog;

    private long lastPacketTime;
    private long ppsAverage;

    public PacketRateLimiter(String packetClassName, int ppsLimit) {
        this.packetClassName = packetClassName;
        this.ppsLimit = ppsLimit;

        ppsLog = new LinkedList<>();
    }

    public String getPacketClassName() {
        return packetClassName;
    }

    public long getAveragePPS() {
        return ppsAverage;
    }

    public void checkRateLimit() throws RateLimitException {
        final long now = System.currentTimeMillis();
        final long lastPacketTime = this.lastPacketTime;

        if (lastPacketTime != 0 && ppsLog.size() > 2) {
            final long oldest;

            if (ppsLog.size() >= MAX_LOG_SIZE) {
                oldest = ppsLog.remove();
            } else {
                oldest = ppsLog.peek();
            }

            long pps = 1000 / ((now - oldest) / ppsLog.size());

            ppsAverage = pps;
        }

        this.lastPacketTime = now;

        ppsLog.add(now);

        if (ppsAverage > ppsLimit) {
            throw new RateLimitException(packetClassName, ppsAverage);
        }
    }

}
