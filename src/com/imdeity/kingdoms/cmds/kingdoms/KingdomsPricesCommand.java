package com.imdeity.kingdoms.cmds.kingdoms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;

public class KingdomsPricesCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        String worldReq = null;
        World validWorld = null;
        if (args.length > 0) {
            worldReq = args[0];
        } else {
            worldReq = player.getWorld().getName();
        }
        for (World world : KingdomsMain.plugin.getServer().getWorlds()) {
            if (world.getName().equalsIgnoreCase(worldReq)) {
                validWorld = world;
                break;
            }
        }
        
        if (validWorld == null) {
            // TODO correct output
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "Sorry, I do not know what the world the " + worldReq + " is.");
            return true;
        }
        KingdomsMain.plugin.chat
                .sendPlayerMessageNoHeader(player, "&3+-----[&bWorld Prices &7(" + validWorld.getName() + ")&3]-----+");
        for (String s : getCostsPerWorld(validWorld)) {
            KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
        }
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public List<String> getCostsPerWorld(World validWorld) {
        List<String> output = new ArrayList<String>();
        output.add("-    &3Kingdom Prices: ");
        output.add("-        &3Creation: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.KINGDOM_PRICES_CREATE,
                                        validWorld.getName()))));
        output.add("-    &3Town Prices: ");
        output.add("-        &3Creation: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_CREATE,
                                        validWorld.getName()))));
        output.add("-        &3Claim: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_CLAIM,
                                        validWorld.getName()))));
        output.add("-        &3Spawn: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SPAWN,
                                        validWorld.getName()))));
        output.add("-        &3Warp-Add: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_WARP_ADD,
                                        validWorld.getName()))));
        output.add("-    &3Plot Prices:");
        output.add("-        &3Mob-Spawning: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN,
                                        validWorld.getName()))));
        output.add("-        &3PvP: &b"
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_PVP,
                                        validWorld.getName()))));
        return output;
    }
}
