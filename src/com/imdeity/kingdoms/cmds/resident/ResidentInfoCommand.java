package com.imdeity.kingdoms.cmds.resident;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class ResidentInfoCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length == 0) {
            for (String s : resident.showInfo(false)) {
                KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
            }
            return true;
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("-o")) {
                for (String s : resident.showInfo(true)) {
                    KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
                }
            } else {
                String res = args[0];
                boolean showOnline = false;
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("-o")) {
                        showOnline = true;
                    }
                }
                Resident shownResident = KingdomsManager.getResident(res);
                if (shownResident == null) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_RES_RESIDENT_INVALID, res));
                    return true;
                }
                for (String s : shownResident.showInfo(showOnline)) {
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
