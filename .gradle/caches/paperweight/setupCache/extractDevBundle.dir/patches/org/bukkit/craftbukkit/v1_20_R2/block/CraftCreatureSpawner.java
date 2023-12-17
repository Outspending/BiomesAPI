package org.bukkit.craftbukkit.v1_20_R2.block;

import com.google.common.base.Preconditions;
import java.util.Optional;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntityType;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<SpawnerBlockEntity> implements CreatureSpawner {

    public CraftCreatureSpawner(World world, SpawnerBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftCreatureSpawner(CraftCreatureSpawner state) {
        super(state);
    }

    @Override
    public EntityType getSpawnedType() {
        SpawnData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.by(spawnData.getEntityToSpawn());
        return type.map(CraftEntityType::minecraftToBukkit).orElse(null);
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null) {
            this.getSnapshot().getSpawner().spawnPotentials = SimpleWeightedRandomList.empty(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later
            this.getSnapshot().getSpawner().nextSpawnData = new SpawnData();
            return;
        }
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "Can't spawn EntityType %s from mob spawners!", entityType);

        RandomSource rand = (this.isPlaced()) ? this.getWorldHandle().getRandom() : RandomSource.create();
        this.getSnapshot().setEntityId(CraftEntityType.bukkitToMinecraft(entityType), rand);
    }

    @Override
    public String getCreatureTypeName() {
        SpawnData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.by(spawnData.getEntityToSpawn());
        return type.map(entityTypes -> net.minecraft.world.entity.EntityType.getKey(entityTypes).getPath()).orElse(null);
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            this.setSpawnedType(null);
            return;
        }
        this.setSpawnedType(type);
    }

    @Override
    public int getDelay() {
        return this.getSnapshot().getSpawner().spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        this.getSnapshot().getSpawner().spawnDelay = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return this.getSnapshot().getSpawner().minSpawnDelay;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= this.getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getSnapshot().getSpawner().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getSnapshot().getSpawner().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= this.getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        this.getSnapshot().getSpawner().maxSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.getSnapshot().getSpawner().maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        this.getSnapshot().getSpawner().maxNearbyEntities = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return this.getSnapshot().getSpawner().spawnCount;
    }

    @Override
    public void setSpawnCount(int count) {
        this.getSnapshot().getSpawner().spawnCount = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().getSpawner().requiredPlayerRange;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().getSpawner().requiredPlayerRange = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return this.getSnapshot().getSpawner().spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.getSnapshot().getSpawner().spawnRange = spawnRange;
    }

    @Override
    public CraftCreatureSpawner copy() {
        return new CraftCreatureSpawner(this);
    }

    // Paper start
    @Override
    public boolean isActivated() {
        this.requirePlaced();
        return this.getSnapshot().getSpawner().isNearPlayer(this.world.getHandle(), this.getPosition());
    }

    @Override
    public void resetTimer() {
        this.requirePlaced();
        this.getSnapshot().getSpawner().delay(this.world.getHandle(), this.getPosition());
    }

    @Override
    public void setSpawnedItem(org.bukkit.inventory.ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null && !itemStack.getType().isAir(), "spawners cannot spawn air");
        net.minecraft.world.item.ItemStack item = org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack.asNMSCopy(itemStack);
        net.minecraft.nbt.CompoundTag entity = new net.minecraft.nbt.CompoundTag();
        entity.putString("id", net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(net.minecraft.world.entity.EntityType.ITEM).toString());
        entity.put("Item", item.save(new net.minecraft.nbt.CompoundTag()));
        this.getSnapshot().getSpawner().setNextSpawnData(this.isPlaced() ? this.world.getHandle() : null, this.getPosition(), new net.minecraft.world.level.SpawnData(entity, java.util.Optional.empty()));
    }
    // Paper end
}
