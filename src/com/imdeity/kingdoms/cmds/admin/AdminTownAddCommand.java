package com.imdeity.kingdoms.cmds.admin;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class AdminTownAddCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        Town town = KingdomsManager.getTown(args[0]);
        town.addResident(resident);
        town.setMayor(resident);
        resident.setMayor(true);
        resident.setAssistant(false);
        resident.save();
        resident.sendMessage("Added you to the town");
        return true;
    }
    
}
