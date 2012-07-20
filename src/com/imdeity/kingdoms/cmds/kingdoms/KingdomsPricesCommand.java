package com.imdeity.kingdoms.cmds.kingdoms;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;

public class KingdomsPricesCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Town Prices: ");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Creation: " + KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_CREATE) + " dei");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Claim: " + KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_CLAIM) + " dei");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Plot Prices:");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    Mob-Spawning: " + KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN) + " dei");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "    PvP: " + KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_SET_PVP) + " dei");
        
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
