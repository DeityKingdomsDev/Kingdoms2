package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;

public class TownRequestCommand extends DeityCommandReceiver {
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        
        return false;
    }
    
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
