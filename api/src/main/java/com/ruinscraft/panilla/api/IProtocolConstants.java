package com.ruinscraft.panilla.api;

/*
 * Minecraft Constants
 *
 * Made up constants should be prefixed with NOT_PROTOCOL_
 * and be a reasonable value which would prevent game-breaking things.
 */
public interface IProtocolConstants {

    default int maxUsernameLength() {
        return 16;
    }

    default int maxBookTitleLength() {
        return 16;
    }

    default int maxAnvilRenameChars() {
        return 35;
    }

    default int maxFireworksFlight() {
        return 3;
    }

    default int minFireworksFlight() {
        return 1;
    }

    default int maxFireworksExplosions() {
        return 8;
    }

    default int maxSlimeSize() {
        return 3; // large slime
    }

    default int maxPacketSize() {
        return 2097152;
    }

    /* Not protocol -- Reasonable values/assumptions */

    default int NOT_PROTOCOL_maxItemNameLength() {
        return 128;
    }

    default int NOT_PROTOCOL_maxLoreLines() {
        return 48;
    }

    default int NOT_PROTOCOL_maxLoreLineLength() {
        return NOT_PROTOCOL_maxItemNameLength();    // TODO: use max item name length for lore lines as well?
    }

    default int NOT_PROTOCOL_maxBlockEntityTagLengthBytes() {
        return 707958;
    }

}
