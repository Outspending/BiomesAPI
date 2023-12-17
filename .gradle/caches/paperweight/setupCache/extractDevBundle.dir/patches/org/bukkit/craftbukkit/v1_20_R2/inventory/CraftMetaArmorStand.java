package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem implements com.destroystokyo.paper.inventory.meta.ArmorStandMeta { // Paper

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    // Paper start
    static final ItemMetaKey INVISIBLE = new ItemMetaKey("Invisible", "invisible");
    static final ItemMetaKey NO_BASE_PLATE = new ItemMetaKey("NoBasePlate", "no-base-plate");
    static final ItemMetaKey SHOW_ARMS = new ItemMetaKey("ShowArms", "show-arms");
    static final ItemMetaKey SMALL = new ItemMetaKey("Small", "small");
    static final ItemMetaKey MARKER = new ItemMetaKey("Marker", "marker");

    private Boolean invisible = null;
    private Boolean noBasePlate = null;
    private Boolean showArms = null;
    private Boolean small = null;
    private Boolean marker = null;
    // Paper end
    CompoundTag entityTag;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand armorStand = (CraftMetaArmorStand) meta;
        // Paper start
        this.invisible = armorStand.invisible;
        this.noBasePlate = armorStand.noBasePlate;
        this.showArms = armorStand.showArms;
        this.small = armorStand.small;
        this.marker = armorStand.marker;
        // Paper end
        this.entityTag = armorStand.entityTag;
    }

    CraftMetaArmorStand(CompoundTag tag) {
        super(tag);

        if (tag.contains(CraftMetaArmorStand.ENTITY_TAG.NBT)) {
            this.entityTag = tag.getCompound(CraftMetaArmorStand.ENTITY_TAG.NBT).copy();
            // Paper start
            if (entityTag.contains(INVISIBLE.NBT)) {
                invisible = entityTag.getBoolean(INVISIBLE.NBT);
            }

            if (entityTag.contains(NO_BASE_PLATE.NBT)) {
                noBasePlate = entityTag.getBoolean(NO_BASE_PLATE.NBT);
            }

            if (entityTag.contains(SHOW_ARMS.NBT)) {
                showArms = entityTag.getBoolean(SHOW_ARMS.NBT);
            }

            if (entityTag.contains(SMALL.NBT)) {
                small = entityTag.getBoolean(SMALL.NBT);
            }

            if (entityTag.contains(MARKER.NBT)) {
                marker = entityTag.getBoolean(MARKER.NBT);
            }
            // Paper end
        }
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);
        // Paper start
        this.invisible = SerializableMeta.getBoolean(map, INVISIBLE.BUKKIT);
        this.noBasePlate = SerializableMeta.getBoolean(map, NO_BASE_PLATE.BUKKIT);
        this.showArms = SerializableMeta.getBoolean(map, SHOW_ARMS.BUKKIT);
        this.small = SerializableMeta.getBoolean(map, SMALL.BUKKIT);
        this.marker = SerializableMeta.getBoolean(map, MARKER.BUKKIT);
        // Paper end
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaArmorStand.ENTITY_TAG.NBT)) {
            this.entityTag = tag.getCompound(CraftMetaArmorStand.ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (this.entityTag != null && !this.entityTag.isEmpty()) {
            internalTags.put(CraftMetaArmorStand.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        // Paper start
        if (!isArmorStandEmpty() && this.entityTag == null) {
            this.entityTag = new CompoundTag();
        }

        if (this.invisible != null) {
            this.entityTag.putBoolean(INVISIBLE.NBT, this.invisible);
        }

        if (this.noBasePlate != null) {
            this.entityTag.putBoolean(NO_BASE_PLATE.NBT, this.noBasePlate);
        }

        if (this.showArms != null) {
            this.entityTag.putBoolean(SHOW_ARMS.NBT, this.showArms);
        }

        if (this.small != null) {
            this.entityTag.putBoolean(SMALL.NBT, this.small);
        }

        if (this.marker != null) {
            this.entityTag.putBoolean(MARKER.NBT, this.marker);
        }
        // Paper end
        if (this.entityTag != null) {
            tag.put(CraftMetaArmorStand.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.ARMOR_STAND;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isArmorStandEmpty();
    }

    boolean isArmorStandEmpty() {
        return !(this.invisible != null || this.noBasePlate != null || this.showArms != null || this.small != null || this.marker != null || this.entityTag != null); // Paper
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaArmorStand) {
            CraftMetaArmorStand that = (CraftMetaArmorStand) meta;

            // Paper start
            return this.invisible == that.invisible &&
                this.noBasePlate == that.noBasePlate &&
                this.showArms == that.showArms &&
                this.small == that.small &&
                this.marker == that.marker;
            // Paper end
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmorStand || this.isArmorStandEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        // Paper start
        hash += this.entityTag != null ? 73 * hash + this.entityTag.hashCode() : 0;
        hash += this.isInvisible() ? 61 * hash + 1231 : 0;
        hash += this.hasNoBasePlate() ? 61 * hash + 1231 : 0;
        hash += this.shouldShowArms() ? 61 * hash + 1231 : 0;
        hash += this.isSmall() ? 61 * hash + 1231 : 0;
        hash += this.isMarker() ? 61 * hash + 1231 : 0;
        // Paper end

        return original != hash ? CraftMetaArmorStand.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        // Paper start
        if (invisible != null) {
            builder.put(INVISIBLE.BUKKIT, invisible);
        }

        if (noBasePlate != null) {
            builder.put(NO_BASE_PLATE.BUKKIT, noBasePlate);
        }

        if (showArms != null) {
            builder.put(SHOW_ARMS.BUKKIT, showArms);
        }

        if (small != null) {
            builder.put(SMALL.BUKKIT, small);
        }

        if (marker != null) {
            builder.put(MARKER.BUKKIT, marker);
        }
        // Paper end

        return builder;
    }

    @Override
    public CraftMetaArmorStand clone() {
        CraftMetaArmorStand clone = (CraftMetaArmorStand) super.clone();

        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.copy();
        }

        return clone;
    }

    // Paper start
    @Override
    public boolean isInvisible() {
        return invisible != null && invisible;
    }

    @Override
    public boolean hasNoBasePlate() {
        return noBasePlate != null && noBasePlate;
    }

    @Override
    public boolean shouldShowArms() {
        return showArms != null && showArms;
    }

    @Override
    public boolean isSmall() {
        return small != null && small;
    }

    @Override
    public boolean isMarker() {
        return marker != null && marker;
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Override
    public void setNoBasePlate(boolean noBasePlate) {
        this.noBasePlate = noBasePlate;
    }

    @Override
    public void setShowArms(boolean showArms) {
        this.showArms = showArms;
    }

    @Override
    public void setSmall(boolean small) {
        this.small = small;
    }

    @Override
    public void setMarker(boolean marker) {
        this.marker = marker;
    }
    // Paper end
}
