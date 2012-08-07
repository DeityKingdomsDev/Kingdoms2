package com.imdeity.kingdoms.cmds.kingdom;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomRemoveCommand extends DeityCommandReceiver {
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length == 0) { return false; }
        
        if (resident.getTown() == null || resident.getTown().getKingdom() == null || !resident.isKing()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_KINGDOM_REQUEST_NOT_KING);
            return true;
        }
        Kingdom kingdom = resident.getTown().getKingdom();
        String townName = args[0];
        if (kingdom.getCapital().getName().equalsIgnoreCase(townName)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_KINGDOM_REMOVE_CAPITAL);
            return true;
        }
        if (kingdom.getTownNames().contains(townName)) {
            kingdom.removeTown(KingdomsManager.getTown(townName));
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_KINGDOM_REMOVE_TOWN, townName));
            return true;
        } else {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_KINGDOM_REMOVE_INVALID, townName));
            return true;
        }
    }
    
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
