package com.imdeity.kingdoms.cmds.admin;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Town;

public class AdminAddBonusPlotsCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length < 2) { return false; }
        Town town = KingdomsManager.getTown(args[0]);
        int numPlotsAdded = 0;
        try {
            numPlotsAdded = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            KingdomsMain.plugin.chat.out("Invalid number of plots");
            return true;
        }
        if (town == null) {
            KingdomsMain.plugin.chat.out("The town of " + args[0] + " is invalid");
            return true;
        }
        int bonusPlots = town.getNumBonusPlots() + numPlotsAdded;
        town.setNumBonusPlots(bonusPlots);
        town.save();
        KingdomsMain.plugin.chat.out("Added " + numPlotsAdded + " bonus plots to the town of " + args[0]);
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 2) { return false; }
        Town town = KingdomsManager.getTown(args[0]);
        int numPlotsAdded = 0;
        try {
            numPlotsAdded = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "Invalid number of plots");
            return true;
        }
        if (town == null) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "The town of " + args[0] + " is invalid");
            return true;
        }
        int bonusPlots = town.getNumBonusPlots() + numPlotsAdded;
        town.setNumBonusPlots(bonusPlots);
        town.save();
        KingdomsMain.plugin.chat.sendPlayerMessage(player, "Added " + numPlotsAdded + " bonus plots to the town of " + args[0]);
        return true;
    }
    
}
