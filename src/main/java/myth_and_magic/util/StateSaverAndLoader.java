package myth_and_magic.util;

import myth_and_magic.MythAndMagic;
import net.minecraft.datafixer.DataFixTypes;
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
            playerNbt.putInt("worthiness", playerData.worthiness);
            playerNbt.putBoolean("boundSword", playerData.boundSword);
            playerNbt.putBoolean("swordDestroyed", playerData.swordDestroyed);
            playerNbt.put("data", playerData.data);
            playersNbt.put(uuid.toString(), playerNbt);
        }));
        nbt.put("players", playersNbt);
        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        NbtCompound playersNbt = tag.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            playerData.worthiness = playersNbt.getCompound(key).getInt("worthiness");
            playerData.boundSword = playersNbt.getCompound(key).getBoolean("boundSword");
            playerData.swordDestroyed = playersNbt.getCompound(key).getBoolean("swordDestroyed");
            playerData.data = playersNbt.getCompound(key).getCompound("data");
            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });
        return state;
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateSaverAndLoader state = persistentStateManager.getOrCreate(new Type<>(StateSaverAndLoader::new,
                StateSaverAndLoader::createFromNbt, DataFixTypes.PLAYER), MythAndMagic.MOD_ID);
        state.markDirty();
        return state;
    }

    public static PlayerData getPlayerState(World world, UUID uuid) {
        StateSaverAndLoader serverState = getServerState(world.getServer());
        return serverState.players.computeIfAbsent(uuid, newUuid -> new PlayerData());
    }
}