package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownDeleteCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (!resident.isMayor()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_DUKE);
            return true;
        }
        Town town = resident.getTown();
        town.remove();
        KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_TOWN_DELETE_PLAYER);
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length > 0) {
            Town town = KingdomsManager.getTown(args[0]);
            if (town != null) {
                town.remove();
                KingdomsMain.plugin.chat.out(KingdomsMessageHelper.CMD_TOWN_DELETE_CONSOLE.replaceAll("%s", args[0]));
                return true;
            }
        }
        return false;
    }
}
