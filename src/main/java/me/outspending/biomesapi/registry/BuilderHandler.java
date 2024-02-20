package me.outspending.biomesapi.registry;

import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.biome.CustomBiome;
import org.jetbrains.annotations.NotNull;

@AsOf("0.1.0")
public interface BuilderHandler<K, V> {

    @AsOf("0.1.0")
    void handle(V value, @NotNull K key);

    @AsOf("0.1.0")
    V build(@NotNull CustomBiome biome);
}
