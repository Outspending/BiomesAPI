package me.outspending.biomesapi;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        CustomBiome biome = CustomBiome.builder()
                .resourceKey(new BiomeResourceKey("test", "custombiome"))
                .settings(BiomeSettings.defaultSettings())
                .fogColor("db4929")
                .foliageColor("22c1c8")
                .skyColor("c8227d")
                .waterColor("c82222")
                .waterFogColor("b9de2e")
                .grassColor("40df8b")
                .build();

        // This wont be needed when API is done
        DedicatedServer server = ((CraftServer) getServer()).getServer();

        // Register the newly created biome
        BiomeRegistry.newRegistry().register(server, biome);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Chunk chunk = e.getBlock().getChunk();
        CustomBiome biome = BiomeHandler.getBiome(new BiomeResourceKey("test", "custombiome"));
        if (biome == null) return;

        Bukkit.broadcastMessage("Biome: " + biome.getResourceKey().toString());
        BiomeSetter.setChunkBiome(chunk, biome, true);
    }


}
