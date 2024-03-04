package myth_and_magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class StateSaverAndLoader extends PersistentState {
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach(((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerNbt.putInt(MythAndMagic.MOD_ID + ".worthiness", playerData.worthiness);
            playerNbt.putBoolean(MythAndMagic.MOD_ID + ".boundSword", playerData.boundSword);
            playersNbt.put(uuid.toString(), playerNbt);
        }));
        nbt.put(MythAndMagic.MOD_ID + ".players", playersNbt);
        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        NbtCompound playersNbt = tag.getCompound(MythAndMagic.MOD_ID + ".players");
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            playerData.worthiness = playersNbt.getCompound(key).getInt(MythAndMagic.MOD_ID + ".worthiness");
            playerData.boundSword = playersNbt.getCompound(key).getBoolean(MythAndMagic.MOD_ID + ".boundSword");
            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });
        return state;
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateSaverAndLoader state = persistentStateManager.getOrCreate(StateSaverAndLoader::createFromNbt, StateSaverAndLoader::new, MythAndMagic.MOD_ID);
        state.markDirty();
        return state;
    }

    public static PlayerData getPlayerState(LivingEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
    }
}