package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaMusicInstrument extends CraftMetaItem implements MusicInstrumentMeta {

    static final ItemMetaKey GOAT_HORN_INSTRUMENT = new ItemMetaKey("instrument");
    private MusicInstrument instrument;

    CraftMetaMusicInstrument(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaMusicInstrument) {
            CraftMetaMusicInstrument craftMetaMusicInstrument = (CraftMetaMusicInstrument) meta;
            this.instrument = craftMetaMusicInstrument.instrument;
        }
    }

    CraftMetaMusicInstrument(CompoundTag tag) {
        super(tag);

        if (tag.contains(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.NBT)) {
            String string = tag.getString(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.NBT);
            this.instrument = Registry.INSTRUMENT.get(NamespacedKey.fromString(string));
        }
    }

    CraftMetaMusicInstrument(Map<String, Object> map) {
        super(map);

        String instrumentString = SerializableMeta.getString(map, CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.BUKKIT, true);
        if (instrumentString != null) {
            this.instrument = Registry.INSTRUMENT.get(NamespacedKey.fromString(instrumentString));
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (this.instrument != null) {
            tag.putString(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.NBT, this.instrument.getKey().toString());
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.GOAT_HORN;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaMusicInstrument) {
            CraftMetaMusicInstrument that = (CraftMetaMusicInstrument) meta;
            return this.instrument == that.instrument;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaMusicInstrument || this.isInstrumentEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isInstrumentEmpty();
    }

    boolean isInstrumentEmpty() {
        return this.instrument == null;
    }

    @Override
    int applyHash() {
        final int orginal;
        int hash = orginal = super.applyHash();

        if (this.hasInstrument()) {
            hash = 61 * hash + this.instrument.hashCode();
        }

        return orginal != hash ? CraftMetaMusicInstrument.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaMusicInstrument clone() {
        CraftMetaMusicInstrument meta = (CraftMetaMusicInstrument) super.clone();
        meta.instrument = this.instrument;
        return meta;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasInstrument()) {
            builder.put(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.BUKKIT, this.instrument.getKey().toString());
        }

        return builder;
    }

    @Override
    public MusicInstrument getInstrument() {
        return this.instrument;
    }

    public boolean hasInstrument() {
        return this.instrument != null;
    }

    @Override
    public void setInstrument(MusicInstrument instrument) {
        this.instrument = instrument;
    }
}
