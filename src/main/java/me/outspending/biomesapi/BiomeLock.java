package me.outspending.biomesapi;

import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.nms.NMSHandler;

import java.util.function.Supplier;

/**
 * This is a utility class that provides static methods to manipulate the biome registry in a Minecraft server.
 * It provides methods to change the lock state of the biome registry and to retrieve a MappedRegistry object from the server.
 * This class uses the Bukkit API to interact with the Minecraft server and the Java Reflection API to manipulate the registry lock state.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public interface BiomeLock {

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
        NMSHandler.executeNMS(nms -> nms.unlockRegistry(supplier));
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
        NMSHandler.executeNMS(nms -> nms.biomeRegistryLock(isLocked));
    }

}
