package me.outspending.biomesapi.exceptions;

import me.outspending.biomesapi.annotations.AsOf;

/**
 * This exception is thrown when an unknown biome is encountered.
 * Biomes are specific regions in Minecraft worlds that vary in climate, terrain, and vegetation.
 * This exception is used to indicate that the biome specified is not recognized by the BiomesAPI.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public class UnknownBiomeException extends RuntimeException {

    /**
     * Constructs a new UnknownBiomeException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UnknownBiomeException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnknownBiomeException with the specified detail message and cause.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public UnknownBiomeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new UnknownBiomeException with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public UnknownBiomeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnknownBiomeException with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message.
     * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public UnknownBiomeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}