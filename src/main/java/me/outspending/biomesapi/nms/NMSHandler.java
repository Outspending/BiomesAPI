package me.outspending.biomesapi.nms;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import org.bukkit.Bukkit;

import java.util.Optional;

@UtilityClass
@AsOf("0.0.1")
public class NMSHandler {

    private static NMS NMS_VERSION;

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

    @AsOf("0.0.1")
    public static boolean isNMSLoaded() {
        return NMS_VERSION != null;
    }

    @AsOf("0.0.1")
    public static Optional<NMS> getNMS() {
        return Optional.ofNullable(NMS_VERSION);
    }

}
