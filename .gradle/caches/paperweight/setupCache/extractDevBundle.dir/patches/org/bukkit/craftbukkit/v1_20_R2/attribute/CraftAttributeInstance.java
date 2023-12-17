package org.bukkit.craftbukkit.v1_20_R2.attribute;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

public class CraftAttributeInstance implements AttributeInstance {

    private final net.minecraft.world.entity.ai.attributes.AttributeInstance handle;
    private final Attribute attribute;

    public CraftAttributeInstance(net.minecraft.world.entity.ai.attributes.AttributeInstance handle, Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }

    @Override
    public Attribute getAttribute() {
        return this.attribute;
    }

    @Override
    public double getBaseValue() {
        return this.handle.getBaseValue();
    }

    @Override
    public void setBaseValue(double d) {
        this.handle.setBaseValue(d);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        List<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (net.minecraft.world.entity.ai.attributes.AttributeModifier nms : this.handle.getModifiers()) {
            result.add(CraftAttributeInstance.convert(nms));
        }

        return result;
    }

    // Paper start
    @Override
    public AttributeModifier getModifier(java.util.UUID uuid) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        net.minecraft.world.entity.ai.attributes.AttributeModifier modifier = this.handle.getModifier(uuid);
        return modifier == null ? null : CraftAttributeInstance.convert(modifier);
    }

    @Override
    public void removeModifier(java.util.UUID uuid) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        this.handle.removeModifier(uuid);
    }
    // Paper end

    @Override
    public void addModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        this.handle.addPermanentModifier(CraftAttributeInstance.convert(modifier));
    }

    // Paper start - Transient modifier API
    @Override
    public void addTransientModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        this.handle.addTransientModifier(CraftAttributeInstance.convert(modifier));
    }
    // Paper end

    @Override
    public void removeModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        this.handle.removeModifier(CraftAttributeInstance.convert(modifier));
    }

    @Override
    public double getValue() {
        return this.handle.getValue();
    }

    @Override
    public double getDefaultValue() {
       return this.handle.getAttribute().getDefaultValue();
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeModifier convert(AttributeModifier bukkit) {
        return new net.minecraft.world.entity.ai.attributes.AttributeModifier(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.values()[bukkit.getOperation().ordinal()]);
    }

    public static AttributeModifier convert(net.minecraft.world.entity.ai.attributes.AttributeModifier nms) {
        return new AttributeModifier(nms.getId(), nms.getName(), nms.getAmount(), AttributeModifier.Operation.values()[nms.getOperation().ordinal()]);
    }

    public static AttributeModifier convert(net.minecraft.world.entity.ai.attributes.AttributeModifier nms, EquipmentSlot slot) {
        return new AttributeModifier(nms.getId(), nms.getName(), nms.getAmount(), AttributeModifier.Operation.values()[nms.getOperation().ordinal()], slot);
    }
}
