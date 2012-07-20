package com.imdeity.kingdoms.cmds.kingdom;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomInfoCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length == 0) {
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            Kingdom kingdom = resident.getTown().getKingdom();
            for (String s : kingdom.showInfo()) {
                KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
            }
            return true;
        } else if (args.length >= 1) {
            String kingdomName = args[0];
            Kingdom kingdom = KingdomsManager.getKingdom(kingdomName);
            if (kingdom == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_KINGDOM, kingdomName));
                return true;
            }
            for (String s : kingdom.showInfo()) {
                KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
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
