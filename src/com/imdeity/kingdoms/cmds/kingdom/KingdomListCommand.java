package com.imdeity.kingdoms.cmds.kingdom;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsManager;

public class KingdomListCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, "Current Kingdoms: ");
        KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, DeityAPI.getAPI().getUtilAPI().getStringUtils().join(KingdomsManager.getSortedKingdomNames(), ", "));
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        KingdomsMain.plugin.chat.out("Current Kingdoms: ");
        KingdomsMain.plugin.chat.out(DeityAPI.getAPI().getUtilAPI().getStringUtils().join(KingdomsManager.getSortedKingdomNames(), ", "));
        return true;
    }
    
}
