package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.exception.RateLimitException;

import java.util.LinkedList;
import java.util.Queue;

public class PacketRateLimiter {

    private final IPanilla panilla;
    private final String packetClassName;
    private final int ppsLimit;

    private Queue<Long> pLog;

    public PacketRateLimiter(IPanilla panilla, String packetClassName, int ppsLimit) {
        this.panilla = panilla;
        this.packetClassName = packetClassName;
        this.ppsLimit = ppsLimit;

        pLog = new LinkedList<>();
    }

    public void checkRateLimit(Object packetHandle) throws RateLimitException {
        if (!packetHandle.getClass().getName().equals(packetClassName)) {
            return;
        }

        int currentPPS = calculatePPS();

        if (currentPPS > ppsLimit) {
            throw new RateLimitException();
        }
    }

    private int calculatePPS() {


        return 0;
    }

}
