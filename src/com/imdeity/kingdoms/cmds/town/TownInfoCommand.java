package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownInfoCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        
        Town town = null;
        if (args.length == 0) {
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            town = resident.getTown();
            for (String s : town.showInfo(true)) {
                KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
            }
            return true;
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("-o")) {
                if (!resident.hasTown()) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                    return true;
                }
                town = resident.getTown();
                for (String s : town.showInfo(false)) {
                    KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
                }
            } else {
                String townName = args[0];
                boolean showOnline = true;
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("-o")) {
                        showOnline = false;
                    }
                }
                town = KingdomsManager.getTown(townName);
                if (town == null) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_TOWN, townName));
                    return true;
                }
                for (String s : town.showInfo(showOnline)) {
                    KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
