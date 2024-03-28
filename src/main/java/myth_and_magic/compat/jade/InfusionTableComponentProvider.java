package myth_and_magic.compat.jade;

import myth_and_magic.block.entity.InfusionTableBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum InfusionTableComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("power")) {
            tooltip.add(Text.translatable("jade.myth_and_magic.power", blockAccessor.getServerData().getInt("power")));
        }
    }

    @Override
    public void appendServerData(NbtCompound nbtCompound, BlockAccessor blockAccessor) {
        InfusionTableBlockEntity infusionTable = (InfusionTableBlockEntity) blockAccessor.getBlockEntity();
        nbtCompound.putInt("power", infusionTable.power);
    }

    @Override
    public Identifier getUid() {
        return MythAndMagicJadePlugin.INFUSION_POWER;
    }
}