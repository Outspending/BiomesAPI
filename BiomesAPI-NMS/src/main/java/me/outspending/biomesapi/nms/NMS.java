package me.outspending.biomesapi.nms;

import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public interface NMS {

    <T> T getCraftServer(@NotNull Server server);
}
