package me.outspending.biomesapi;

import me.outspending.biomesapi.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        CustomBiome biome = CustomBiome.builder()
                .resourceKey(BiomeResourceKey.of("test", "custombiome"))
                .settings(BiomeSettings.defaultSettings())

                .fogColor(Color.fromRGB(95, 95, 95))
                .foliageColor(Color.fromRGB(192, 233, 145))
                .skyColor(Color.fromRGB(177, 245, 238))
                .waterColor(Color.fromRGB(246, 133, 212))
                .waterFogColor(Color.fromRGB(245, 177, 224))
                .grassColor(Color.fromRGB(140, 246, 133))

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

        Location loc = e.getBlock().getLocation();
        Location loc1 = loc.clone().add(50, 50, 50);
        Location loc2 = loc.clone().add(-50, -50, -50);

        BiomeSetter.setRegionBiome(loc1, loc2, biome, true);
    }

}
