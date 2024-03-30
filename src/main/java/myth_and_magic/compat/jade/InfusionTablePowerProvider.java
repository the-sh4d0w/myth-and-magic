package myth_and_magic.compat.jade;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.InfusionTableBlock;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum InfusionTablePowerProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        World world = blockAccessor.getBlockEntity().getWorld();
        BlockPos pos = blockAccessor.getPosition();
        int power = 0;
        for (BlockPos blockPos : InfusionTableBlock.POWER_PROVIDER_OFFSETS) {
            power += InfusionTableBlock.getPower(world, pos, blockPos);
        }
        tooltip.add(Text.translatable("jade.myth_and_magic.power", power));
    }

    @Override
    public Identifier getUid() {
        return new Identifier(MythAndMagic.MOD_ID, "infusion_table");
    }
}