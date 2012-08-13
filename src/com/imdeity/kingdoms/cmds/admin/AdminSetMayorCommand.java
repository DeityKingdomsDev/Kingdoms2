package com.imdeity.kingdoms.cmds.admin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class AdminSetMayorCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length < 2) { return false; }
        if (args[1].equalsIgnoreCase("npc")) {
            Town town = KingdomsManager.getTown(args[0]);
            if (town == null) {
                KingdomsMain.plugin.chat.out("The town of " + args[0] + " is invalid");
                return true;
            }
            town.setMayor(null);
        } else {
            Resident resident = KingdomsManager.getResident(args[1]);
            Town town = KingdomsManager.getTown(args[0]);
            if (town == null) {
                KingdomsMain.plugin.chat.out("The town of " + args[1] + " is invalid");
                return true;
            }
            if (resident == null) {
                KingdomsMain.plugin.chat.out("The resident " + args[1] + " is invalid");
                return true;
            }
            List<String> townResidents = town.getResidentsNames();
            for(String townResident : townResidents) { //Set all other residents to not be mayor.
            	if(KingdomsManager.getResident(townResident).isMayor()) KingdomsManager.getResident(townResident).setMayor(false);
            }
            if(!townResidents.contains(resident.getName())) {
            	town.addResident(resident);
            }
            
            town.setMayor(resident);
        }
        KingdomsMain.plugin.chat.out("Set the mayor of " + args[0] + " to " + args[1]);
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 2) { return false; }
        if (args[1].equalsIgnoreCase("npc")) {
            Town town = KingdomsManager.getTown(args[0]);
            if (town == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "The town of " + args[1] + " is invalid");
                return true;
            }
            town.setMayor(null);
        } else {
            Resident resident = KingdomsManager.getResident(args[1]);
            Town town = KingdomsManager.getTown(args[0]);
            if (town == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "The town of " + args[1] + " is invalid");
                return true;
            }
            if (resident == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "The resident " + args[1] + " is invalid");
                return true;
            }
            List<String> townResidents = town.getResidentsNames();
            for(String townResident : townResidents) { //Set all other residents to not be mayor.
            	if(KingdomsManager.getResident(townResident).isMayor()) KingdomsManager.getResident(townResident).setMayor(false);
            }
            if(!townResidents.contains(resident.getName())) {
            	town.addResident(resident);
            }
            town.setMayor(resident);
        }
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Set the mayor of " + args[0] + " to " + args[1]);
        return true;
    }
    
}
