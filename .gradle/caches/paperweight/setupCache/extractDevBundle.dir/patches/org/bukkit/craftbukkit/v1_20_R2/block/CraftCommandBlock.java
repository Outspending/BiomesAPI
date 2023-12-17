package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockEntity> implements CommandBlock, io.papermc.paper.commands.PaperCommandBlockHolder {

    public CraftCommandBlock(World world, CommandBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftCommandBlock(CraftCommandBlock state) {
        super(state);
    }

    @Override
    public String getCommand() {
        return this.getSnapshot().getCommandBlock().getCommand();
    }

    @Override
    public void setCommand(String command) {
        this.getSnapshot().getCommandBlock().setCommand(command != null ? command : "");
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(this.getSnapshot().getCommandBlock().getName());
    }

    @Override
    public void setName(String name) {
        this.getSnapshot().getCommandBlock().setName(CraftChatMessage.fromStringOrNull(name != null ? name : "@"));
    }

    @Override
    public CraftCommandBlock copy() {
        return new CraftCommandBlock(this);
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component name() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(getSnapshot().getCommandBlock().getName());
    }

    @Override
    public void name(net.kyori.adventure.text.Component name) {
        getSnapshot().getCommandBlock().setName(name == null ? net.minecraft.network.chat.Component.literal("@") : io.papermc.paper.adventure.PaperAdventure.asVanilla(name));
    }

    @Override
    public net.minecraft.world.level.BaseCommandBlock getCommandBlockHandle() {
        return getSnapshot().getCommandBlock();
    }
    // Paper end
}
