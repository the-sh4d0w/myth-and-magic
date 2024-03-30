package myth_and_magic.compat.jade;

import myth_and_magic.MythAndMagic;
import net.minecraft.block.CandleBlock;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CandlePowerProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        tooltip.add(Text.translatable("jade.myth_and_magic.power", blockAccessor.getBlockState().get(CandleBlock.CANDLES)));
    }

    @Override
    public Identifier getUid() {
        return new Identifier(MythAndMagic.MOD_ID, "candle");
    }
}