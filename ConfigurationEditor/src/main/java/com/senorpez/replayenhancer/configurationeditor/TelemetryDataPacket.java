package com.senorpez.replayenhancer.configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;

import java.nio.ByteBuffer;

public class TelemetryDataPacket extends Packet {
    private final SimpleIntegerProperty gameSessionState;
    private final SimpleIntegerProperty numParticipants;
    private final SimpleIntegerProperty raceStateFlags;

    public TelemetryDataPacket(ByteBuffer data) {
        super(data);

        this.gameSessionState = new SimpleIntegerProperty(ReadChar(data));
        Integer viewedParticipantIndex = ReadChar(data);

        this.numParticipants = new SimpleIntegerProperty(ReadChar(data));

        Integer unfilteredThrottle = ReadChar(data);
        Integer unfilteredBrake = ReadChar(data);
        Integer unfilteredSteering = ReadChar(data);
        Integer unfilteredClutch = ReadChar(data);

        this.raceStateFlags = new SimpleIntegerProperty(ReadChar(data));
    }

    public int getNumParticipants() {
        return numParticipants.get();
    }

    public int getRaceState() {
        int bitmask = 0b00000111;
        return raceStateFlags.get() & bitmask;
    }

    public int getSessionState() {
        int bitmask = 0b11110000;
        return (gameSessionState.get() & bitmask) >> 4;
    }
}
