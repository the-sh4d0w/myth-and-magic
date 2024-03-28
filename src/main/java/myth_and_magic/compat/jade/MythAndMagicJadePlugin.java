package myth_and_magic.compat.jade;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.InfusionTableBlock;
import myth_and_magic.block.entity.InfusionTableBlockEntity;
import net.minecraft.block.CandleBlock;
import net.minecraft.util.Identifier;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MythAndMagicJadePlugin implements IWailaPlugin {
    public static final Identifier INFUSION_POWER = new Identifier(MythAndMagic.MOD_ID, "infusion_power");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(InfusionTableComponentProvider.INSTANCE, InfusionTableBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(InfusionTableComponentProvider.INSTANCE, InfusionTableBlock.class);
        registration.registerBlockComponent(CandleComponentProvider.INSTANCE, CandleBlock.class);
    }
}