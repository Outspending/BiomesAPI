package org.bukkit.craftbukkit.v1_20_R2;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.Axis;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;
import org.bukkit.potion.Potion;

public class CraftEffect {
    public static <T> int getDataValue(Effect effect, T data) {
        int datavalue;
        switch (effect) {
        case PARTICLES_SCULK_CHARGE: // Paper - add missing effects
        case VILLAGER_PLANT_GROW:
            datavalue = (Integer) data;
            break;
        case POTION_BREAK:
            if (data instanceof Potion) { // Paper - use Color for POTION_BREAK
            datavalue = ((Potion) data).toDamageValue() & 0x3F;
            break;
            } // Paper - Color will fall through to cast below
        case INSTANT_POTION_BREAK:
            datavalue = ((Color) data).asRGB();
            break;
        case RECORD_PLAY:
            Preconditions.checkArgument(data == Material.AIR || ((Material) data).isRecord(), "Invalid record type for Material %s!", data);
            datavalue = Item.getId(CraftMagicNumbers.getItem((Material) data));
            break;
        case SMOKE:
            switch ((BlockFace) data) {
            case DOWN:
            // SPIGOT-6318: Fallback value for the old directions
            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:
            case SELF:
                datavalue = 0;
                break;
            case UP:
                datavalue = 1;
                break;
            case NORTH:
                datavalue = 2;
                break;
            case SOUTH:
                datavalue = 3;
                break;
            case WEST:
                datavalue = 4;
                break;
            case EAST:
                datavalue = 5;
                break;
            default:
                throw new IllegalArgumentException("Bad smoke direction!");
            }
            break;
        case STEP_SOUND:
            if (data instanceof Material) { // Paper - support BlockData
            Preconditions.checkArgument(((Material) data).isBlock(), "Material %s is not a block!", data);
            datavalue = Block.getId(CraftMagicNumbers.getBlock((Material) data).defaultBlockState());
            // Paper start - support BlockData
                break;
            }
        case PARTICLES_AND_SOUND_BRUSH_BLOCK_COMPLETE:
            datavalue = Block.getId(((org.bukkit.craftbukkit.v1_20_R2.block.data.CraftBlockData) data).getState());
            // Paper end
            break;
        case COMPOSTER_FILL_ATTEMPT:
            datavalue = ((Boolean) data) ? 1 : 0;
            break;
        case BONE_MEAL_USE:
            datavalue = (Integer) data;
            break;
        case ELECTRIC_SPARK:
            if (data == null) {
                datavalue = -1;
            } else {
                switch ((Axis) data) {
                case X:
                    datavalue = 0;
                    break;
                case Y:
                    datavalue = 1;
                    break;
                case Z:
                    datavalue = 2;
                    break;
                default:
                    throw new IllegalArgumentException("Bad electric spark axis!");
                }
            }
            break;
        default:
            datavalue = 0;
        }
        return datavalue;
    }
}
