package me.outspending.biomesapi;

import me.outspending.biomesapi.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        NMSHandler.init(); // Initialize NMS

        CustomBiome biome = CustomBiome.builder()
                .resourceKey(BiomeResourceKey.of("test", "custombiome"))
                .particleRenderer(ParticleRenderer.defaultSettings())
                .settings(BiomeSettings.defaultSettings())
                .fogColor("#db4929")
                .foliageColor("#22c1c8")
                .skyColor("#c8227d")
                .waterColor("#c82222")
                .waterFogColor("#b9de2e")
                .grassColor("#40df8b")
                .build();

        // Register the newly created biome
        BiomeRegistry.newRegistry().register(biome);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        CustomBiome biome = BiomeHandler.getBiome(new BiomeResourceKey("test", "custombiome"));
        if (biome == null) return;

        Chunk chunk = e.getBlock().getChunk();

        BiomeSetter.setChunkBiome(chunk, biome, true);
    }

}
