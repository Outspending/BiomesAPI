package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ArrowItem;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaCrossbow extends CraftMetaItem implements CrossbowMeta {

    static final ItemMetaKey CHARGED = new ItemMetaKey("Charged", "charged");
    static final ItemMetaKey CHARGED_PROJECTILES = new ItemMetaKey("ChargedProjectiles", "charged-projectiles");
    //
    private boolean charged;
    private List<ItemStack> chargedProjectiles;

    CraftMetaCrossbow(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaCrossbow)) {
            return;
        }

        CraftMetaCrossbow crossbow = (CraftMetaCrossbow) meta;
        this.charged = crossbow.charged;

        if (crossbow.hasChargedProjectiles()) {
            this.chargedProjectiles = new ArrayList<>(crossbow.chargedProjectiles);
        }
    }

    CraftMetaCrossbow(CompoundTag tag) {
        super(tag);

        this.charged = tag.getBoolean(CraftMetaCrossbow.CHARGED.NBT);

        if (tag.contains(CraftMetaCrossbow.CHARGED_PROJECTILES.NBT, CraftMagicNumbers.NBT.TAG_LIST)) {
            ListTag list = tag.getList(CraftMetaCrossbow.CHARGED_PROJECTILES.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);

            if (list != null && !list.isEmpty()) {
                this.chargedProjectiles = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    CompoundTag nbttagcompound1 = list.getCompound(i);

                    this.chargedProjectiles.add(CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(nbttagcompound1)));
                }
            }
        }
    }

    CraftMetaCrossbow(Map<String, Object> map) {
        super(map);

        Boolean charged = SerializableMeta.getObject(Boolean.class, map, CraftMetaCrossbow.CHARGED.BUKKIT, true);
        if (charged != null) {
            this.charged = charged;
        }

        Iterable<?> projectiles = SerializableMeta.getObject(Iterable.class, map, CraftMetaCrossbow.CHARGED_PROJECTILES.BUKKIT, true);
        if (projectiles != null) {
            for (Object stack : projectiles) {
                if (stack instanceof ItemStack) {
                    this.addChargedProjectile((ItemStack) stack);
                }
            }
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        tag.putBoolean(CraftMetaCrossbow.CHARGED.NBT, this.charged);
        if (this.hasChargedProjectiles()) {
            ListTag list = new ListTag();

            for (ItemStack item : this.chargedProjectiles) {
                CompoundTag saved = new CompoundTag();
                CraftItemStack.asNMSCopy(item).save(saved);
                list.add(saved);
            }

            tag.put(CraftMetaCrossbow.CHARGED_PROJECTILES.NBT, list);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.CROSSBOW;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isCrossbowEmpty();
    }

    boolean isCrossbowEmpty() {
        return !(this.hasChargedProjectiles());
    }

    @Override
    public boolean hasChargedProjectiles() {
        return this.chargedProjectiles != null;
    }

    @Override
    public List<ItemStack> getChargedProjectiles() {
        return (this.chargedProjectiles == null) ? ImmutableList.of() : ImmutableList.copyOf(this.chargedProjectiles);
    }

    @Override
    public void setChargedProjectiles(List<ItemStack> projectiles) {
        this.chargedProjectiles = null;
        this.charged = false;

        if (projectiles == null) {
            return;
        }

        for (ItemStack i : projectiles) {
            this.addChargedProjectile(i);
        }
    }

    @Override
    public void addChargedProjectile(ItemStack item) {
        Preconditions.checkArgument(item != null, "item");
        Preconditions.checkArgument(item.getType() == Material.FIREWORK_ROCKET || CraftMagicNumbers.getItem(item.getType()) instanceof ArrowItem, "Item %s is not an arrow or firework rocket", item);

        if (this.chargedProjectiles == null) {
            this.chargedProjectiles = new ArrayList<>();
        }

        this.charged = true;
        this.chargedProjectiles.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCrossbow) {
            CraftMetaCrossbow that = (CraftMetaCrossbow) meta;

            return this.charged == that.charged
                    && (this.hasChargedProjectiles() ? that.hasChargedProjectiles() && this.chargedProjectiles.equals(that.chargedProjectiles) : !that.hasChargedProjectiles());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCrossbow || this.isCrossbowEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasChargedProjectiles()) {
            hash = 61 * hash + (this.charged ? 1 : 0);
            hash = 61 * hash + this.chargedProjectiles.hashCode();
        }

        return original != hash ? CraftMetaCrossbow.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaCrossbow clone() {
        return (CraftMetaCrossbow) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        builder.put(CraftMetaCrossbow.CHARGED.BUKKIT, this.charged);
        if (this.hasChargedProjectiles()) {
            builder.put(CraftMetaCrossbow.CHARGED_PROJECTILES.BUKKIT, this.chargedProjectiles);
        }

        return builder;
    }
}
