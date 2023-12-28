package me.outspending.biomesapi.exceptions;

import me.outspending.biomesapi.annotations.AsOf;

/**
 * This exception is thrown when an unknown NMS version is encountered.
 * NMS (Net Minecraft Server) is the internal server code for Minecraft.
 * This exception is used to indicate that the server version is not supported by the BiomesAPI.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public class UnknownNMSVersionException extends RuntimeException {

    /**
     * Constructs a new UnknownNMSVersionException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UnknownNMSVersionException(String message) {
        super(message);
    }

}