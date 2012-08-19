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
        List<String> output = new ArrayList<String>();
        output.add("    Kingdom Prices: ");
        output.add("        Creation: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.KINGDOM_PRICES_CREATE,
                                        validWorld))));
        output.add("    Town Prices: ");
        output.add("        Creation: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String
                                        .format(KingdomsConfigHelper.TOWN_PRICES_CREATE, validWorld))));
        output.add("        Claim: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_CLAIM, validWorld))));
        output.add("        Spawn: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SPAWN, validWorld))));
        output.add("        Warp-Add: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_WARP_ADD,
                                        validWorld))));
        output.add("    Plot Prices:");
        output.add("        Mob-Spawning: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN,
                                        validWorld))));
        output.add("        PvP: "
                + DeityAPI
                        .getAPI()
                        .getEconAPI()
                        .getFormattedBalance(
                                KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_PVP,
                                        validWorld))));
        KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, "+-----[World Prices (" + validWorld.getName() + ")]-----+");
        for (String s : output) {
            KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
            
        }
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
