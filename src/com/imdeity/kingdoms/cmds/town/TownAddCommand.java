package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownAddCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (!resident.isMayor() && !resident.isSeniorAssistant()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
            return true;
        }
        
        Town town = resident.getTown();
        if (args.length == 1) {
            Resident newResident = KingdomsManager.getResident(args[0]);
            if (newResident == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_RESIDENT, args[0]));
                return true;
            }
            if (newResident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_ALREADY_IN_TOWN);
                return true;
            }
            town.addResident(newResident);
            KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_ADD_GLOBAL, newResident.getName(), town.getName()));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length == 2) {
            Town town = KingdomsManager.getTown(args[0]);
            Resident newResident = KingdomsManager.getResident(args[1]);
            if (newResident == null) {
                KingdomsMain.plugin.chat.out(String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_RESIDENT, args[0]));
                return true;
            }
            if (newResident.hasTown()) {
                KingdomsMain.plugin.chat.out(KingdomsMessageHelper.CMD_FAIL_ALREADY_IN_TOWN);
                return true;
            }
            town.addResident(newResident);
            KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_ADD_GLOBAL, newResident.getName(), town.getName()));
            return true;
        }
        return false;
    }
}
