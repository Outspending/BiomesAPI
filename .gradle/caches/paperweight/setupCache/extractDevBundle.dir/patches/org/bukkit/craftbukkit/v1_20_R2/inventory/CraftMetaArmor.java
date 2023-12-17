package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaArmor extends CraftMetaItem implements ArmorMeta {

    private static final Set<Material> ARMOR_MATERIALS = Sets.newHashSet(
            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_BOOTS,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.GOLDEN_HELMET,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS,
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.TURTLE_HELMET
    );

    static final ItemMetaKey TRIM = new ItemMetaKey("Trim", "trim");
    static final ItemMetaKey TRIM_MATERIAL = new ItemMetaKey("material");
    static final ItemMetaKey TRIM_PATTERN = new ItemMetaKey("pattern");

    private ArmorTrim trim;

    CraftMetaArmor(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaArmor armorMeta) {
            this.trim = armorMeta.trim;
        }
    }

    CraftMetaArmor(CompoundTag tag) {
        super(tag);

        if (tag.contains(CraftMetaArmor.TRIM.NBT)) {
            CompoundTag trimCompound = tag.getCompound(CraftMetaArmor.TRIM.NBT);

            if (trimCompound.contains(CraftMetaArmor.TRIM_MATERIAL.NBT, net.minecraft.nbt.Tag.TAG_STRING) && trimCompound.contains(CraftMetaArmor.TRIM_PATTERN.NBT, net.minecraft.nbt.Tag.TAG_STRING)) { // Paper - for now, ignore inline definitions of trim material & pattern
                // Paper start
                TrimMaterial trimMaterial = getRegistry(Registry.TRIM_MATERIAL, trimCompound.getString(TRIM_MATERIAL.NBT));
                TrimPattern trimPattern = getRegistry(Registry.TRIM_PATTERN, trimCompound.getString(TRIM_PATTERN.NBT));
                // Paper end

                this.trim = trimMaterial == null || trimPattern == null ? null : new ArmorTrim(trimMaterial, trimPattern); // Paper
            }
        }
    }
    // Paper start
    public <T extends org.bukkit.Keyed> T getRegistry(Registry<T> registry, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        NamespacedKey namespacedKey = NamespacedKey.fromString(value);
        if (namespacedKey == null) {
            return null;
        }

        T registryValue = registry.get(namespacedKey);
        if (registryValue == null) {
            return null;
        }

        return registryValue;
    }
    // Paper end

    CraftMetaArmor(Map<String, Object> map) {
        super(map);

        Map<?, ?> trimData = SerializableMeta.getObject(Map.class, map, CraftMetaArmor.TRIM.BUKKIT, true);
        if (trimData != null) {
            String materialKeyString = SerializableMeta.getString(trimData, CraftMetaArmor.TRIM_MATERIAL.BUKKIT, true);
            String patternKeyString = SerializableMeta.getString(trimData, CraftMetaArmor.TRIM_PATTERN.BUKKIT, true);

            if (materialKeyString != null && patternKeyString != null) {
                NamespacedKey materialKey = NamespacedKey.fromString(materialKeyString);
                NamespacedKey patternKey = NamespacedKey.fromString(patternKeyString);

                if (materialKey != null && patternKey != null) {
                    TrimMaterial trimMaterial = Registry.TRIM_MATERIAL.get(materialKey);
                    TrimPattern trimPattern = Registry.TRIM_PATTERN.get(patternKey);

                    if (trimMaterial != null && trimPattern != null) {
                        this.trim = new ArmorTrim(trimMaterial, trimPattern);
                    }
                }
            }
        }
    }

    @Override
    void applyToItem(CompoundTag itemTag) {
        super.applyToItem(itemTag);

        if (this.hasTrim()) {
            CompoundTag trimCompound = new CompoundTag();
            trimCompound.putString(CraftMetaArmor.TRIM_MATERIAL.NBT, this.trim.getMaterial().getKey().toString());
            trimCompound.putString(CraftMetaArmor.TRIM_PATTERN.NBT, this.trim.getPattern().getKey().toString());
            itemTag.put(CraftMetaArmor.TRIM.NBT, trimCompound);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return CraftMetaArmor.ARMOR_MATERIALS.contains(type);
    }

    @Override
    boolean equalsCommon(CraftMetaItem that) {
        if (!super.equalsCommon(that)) {
            return false;
        }

        if (that instanceof CraftMetaArmor armorMeta) {
            return Objects.equals(this.trim, armorMeta.trim);
        }

        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmor || this.isArmorEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isArmorEmpty();
    }

    private boolean isArmorEmpty() {
        return !this.hasTrim();
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasTrim()) {
            hash = 61 * hash + this.trim.hashCode();
        }

        return original != hash ? CraftMetaArmor.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaArmor clone() {
        CraftMetaArmor meta = (CraftMetaArmor) super.clone();
        meta.trim = this.trim;
        return meta;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasTrim()) {
            Map<String, String> trimData = new HashMap<>();
            trimData.put(CraftMetaArmor.TRIM_MATERIAL.BUKKIT, this.trim.getMaterial().getKey().toString());
            trimData.put(CraftMetaArmor.TRIM_PATTERN.BUKKIT, this.trim.getPattern().getKey().toString());
            builder.put(CraftMetaArmor.TRIM.BUKKIT, trimData);
        }

        return builder;
    }

    @Override
    public boolean hasTrim() {
        return this.trim != null;
    }

    @Override
    public void setTrim(ArmorTrim trim) {
        this.trim = trim;
    }

    @Override
    public ArmorTrim getTrim() {
        return this.trim;
    }
}
