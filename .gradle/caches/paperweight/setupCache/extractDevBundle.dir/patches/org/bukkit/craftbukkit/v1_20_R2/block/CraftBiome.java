package org.bukkit.craftbukkit.v1_20_R2.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R2.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;

public class CraftBiome {

    public static Biome minecraftToBukkit(net.minecraft.world.level.biome.Biome minecraft) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> registry = CraftRegistry.getMinecraftRegistry(Registries.BIOME);
        Biome bukkit = Registry.BIOME.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        if (bukkit == null) {
            return Biome.CUSTOM;
        }

        return bukkit;
    }

    public static Biome minecraftHolderToBukkit(Holder<net.minecraft.world.level.biome.Biome> minecraft) {
        return CraftBiome.minecraftToBukkit(minecraft.value());
    }

    private static final java.util.Map<org.bukkit.block.Biome, ResourceKey<net.minecraft.world.level.biome.Biome>> BIOME_KEY_CACHE = java.util.Collections.synchronizedMap(new java.util.EnumMap<>(Biome.class)); // Paper
    public static net.minecraft.world.level.biome.Biome bukkitToMinecraft(Biome bukkit) {
        if (bukkit == null || bukkit == Biome.CUSTOM) {
            return null;
        }

        return CraftRegistry.getMinecraftRegistry(Registries.BIOME)
                .getOptional(BIOME_KEY_CACHE.computeIfAbsent(bukkit, b -> ResourceKey.create(Registries.BIOME, CraftNamespacedKey.toMinecraft(b.getKey())))).orElseThrow();
    }

    public static Holder<net.minecraft.world.level.biome.Biome> bukkitToMinecraftHolder(Biome bukkit) {
        if (bukkit == null || bukkit == Biome.CUSTOM) {
            return null;
        }

        net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> registry = CraftRegistry.getMinecraftRegistry(Registries.BIOME);

        if (registry.wrapAsHolder(CraftBiome.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<net.minecraft.world.level.biome.Biome> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own biome base with out properly registering it.");
    }
}
