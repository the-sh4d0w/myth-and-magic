package myth_and_magic.compat.jade;

import myth_and_magic.block.InfusionTableBlock;
import myth_and_magic.block.entity.InfusionTableBlockEntity;
import myth_and_magic.entity.KnightEntity;
import net.minecraft.block.CandleBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MythAndMagicJadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(InfusionTablePowerProvider.INSTANCE, InfusionTableBlock.class);
        registration.registerBlockComponent(CandlePowerProvider.INSTANCE, CandleBlock.class);
        registration.registerEntityComponent(KnightOwnerProvider.INSTANCE, KnightEntity.class);
    }
}