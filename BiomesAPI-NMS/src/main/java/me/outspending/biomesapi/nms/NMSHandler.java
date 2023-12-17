package me.outspending.biomesapi.nms;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class NMSHandler {

    @Getter
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Getter
    protected static Class<?> CraftServer;

    public static void init() {
        CraftServer = getClass("org.bukkit.craftbukkit." + version + ".CraftServer");
    }

    public static @Nullable Class<?> getClass(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> @Nullable T castClass(Class<?> clazz, T object) {
        try {
            return (T) clazz.cast(object);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return null;
    }

}
