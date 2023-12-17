package me.outspending.biomesapi;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * This is a utility class that provides static methods to manipulate the biome registry in a Minecraft server.
 * It provides methods to change the lock state of the biome registry and to retrieve a MappedRegistry object from the server.
 * This class uses the Bukkit API to interact with the Minecraft server and the Java Reflection API to manipulate the registry lock state.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
@UtilityClass
public final class BiomeLock {

    /**
     * Unlocks the biome registry, performs an operation, and then locks the biome registry again.
     * This method is useful when you need to perform an operation that requires the biome registry to be unlocked.
     * The operation to be performed is provided as a Supplier.
     *
     * @param supplier The operation to be performed on the unlocked biome registry. This operation is provided as a Supplier.
     * The Supplier's get method is called to perform the operation.
     *
     * The method works as follows:
     * 1. It retrieves the MappedRegistry object for biomes using the getRegistry method.
     * 2. It unlocks the biome registry by calling the changeRegistryLock method with false as the argument.
     * 3. It performs the operation provided as the Supplier.
     * 4. It locks the biome registry again by calling the freeze method on the MappedRegistry object.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    static void unlock(Supplier<?> supplier) {
        MappedRegistry<Biome> biomes = getRegistry(Registries.BIOME);
        changeRegistryLock(false);
        supplier.get();
        biomes.freeze();
    }

    /**
     * This method changes the lock state of the biome registry in the Minecraft server.
     * It first retrieves the MappedRegistry object for biomes using the getRegistry method.
     * Then, it uses reflection to access the boolean field in the RegistryMaterials class, which represents the lock state.
     * The lock state is then set to the value provided as the method argument.
     *
     * @param isLocked The new lock state for the biome registry.
     * @throws ClassNotFoundException if the RegistryMaterials class cannot be found.
     * @throws IllegalAccessException if the boolean field in the RegistryMaterials class cannot be accessed.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    static void changeRegistryLock(boolean isLocked) {
        MappedRegistry<Biome> biomes = getRegistry(Registries.BIOME);
        try {
            Class<?> registryBiomeClass = Class.forName("net.minecraft.core.RegistryMaterials");
            for (Field field : registryBiomeClass.getDeclaredFields()) {
                if (field.getType() == boolean.class) {
                    field.setAccessible(true);
                    field.setBoolean(biomes, isLocked);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves a MappedRegistry object from the Minecraft server.
     * It uses the Bukkit API to get the server instance and then accesses the server's registry.
     * The registry is then cast to a MappedRegistry object and returned.
     *
     * @param key The ResourceKey for the registry that needs to be retrieved.
     * @return MappedRegistry object corresponding to the provided ResourceKey.
     * @throws RuntimeException if the registry corresponding to the provided ResourceKey does not exist.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    static <T> MappedRegistry<T> getRegistry(ResourceKey<Registry<T>> key) {
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        return (MappedRegistry<T>) server.registryAccess().registryOrThrow(key);
    }

}
