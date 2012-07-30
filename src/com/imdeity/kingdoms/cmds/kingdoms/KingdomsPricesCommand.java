package com.imdeity.kingdoms.cmds.kingdoms;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;

public class KingdomsPricesCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Kingdom Prices: ");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Creation: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.KINGDOM_PRICES_CREATE)));
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Town Prices: ");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Creation: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_CREATE) + " dei"));
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Claim: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_CLAIM) + " dei"));
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Spawn: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_SPAWN) + " dei"));
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Warp-Add: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_WARP_ADD) + " dei"));
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Plot Prices:");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Mob-Spawning: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN) + " dei"));
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    PvP: " + DeityAPI.getAPI().getEconAPI().getFormattedBalance(KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_SET_PVP) + " dei"));
        
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
