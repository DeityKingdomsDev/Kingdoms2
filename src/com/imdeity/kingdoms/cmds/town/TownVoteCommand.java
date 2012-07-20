package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;

public class TownVoteCommand extends DeityCommandReceiver {
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (true) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "This command is not yet enabled. Please check the dev thread for info...");
        }
        return true;
    }
    
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
