package me.outspending.biomesapi.nms;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import org.bukkit.Bukkit;

import java.util.Optional;

/**
 * Utility class for handling NMS (Net Minecraft Server) related operations.
 * This class is annotated with @UtilityClass from the Lombok library, which indicates that this is a utility class and hence, cannot be instantiated.
 * It also generates a private no-args constructor, which throws an exception when invoked.
 *
 * @version 0.0.1
 */
@UtilityClass
@AsOf("0.0.1")
public class NMSHandler {

    /**
     * Holds the NMS version instance.
     */
    private static NMS NMS_VERSION;

    /**
     * Initializes the NMS version based on the server's version.
     * The server's version is retrieved from the package name of the server class.
     * The version is then used in a switch statement to instantiate the appropriate NMS version.
     * If the server's version is not supported, a RuntimeException is thrown.
     *
     * @throws RuntimeException if the server's version is not supported
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void init() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        switch (version) {
            case "v1_19_R1" -> NMS_VERSION = new NMS_v1_19_R1();
            case "v1_19_R2" -> NMS_VERSION = new NMS_v1_19_R2();
            case "v1_19_R3" -> NMS_VERSION = new NMS_v1_19_R3();
            case "v1_20_R1" -> NMS_VERSION = new NMS_v1_20_R1();
            case "v1_20_R2" -> NMS_VERSION = new NMS_v1_20_R2();
            case "v1_20_R3" -> NMS_VERSION = new NMS_v1_20_R3();
            default -> throw new RuntimeException("This version of Minecraft is not supported!");
        }
    }

    /**
     * Checks if the NMS version has been loaded.
     *
     * @return true if the NMS version has been loaded, false otherwise
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static boolean isNMSLoaded() {
        return NMS_VERSION != null;
    }

    /**
     * Retrieves the NMS version instance.
     *
     * @return an Optional containing the NMS version instance if it exists, an empty Optional otherwise
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static Optional<NMS> getNMS() {
        return Optional.ofNullable(NMS_VERSION);
    }

}
