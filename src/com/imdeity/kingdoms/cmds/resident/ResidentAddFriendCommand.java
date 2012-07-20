package com.imdeity.kingdoms.cmds.resident;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class ResidentAddFriendCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length == 1) {
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident == null) { return false; }
            Resident friend = KingdomsManager.getResident(args[0]);
            if (friend != null) {
                resident.addFriend(friend);
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_RES_ADD_FRIEND_PLAYER, friend.getName()));
            } else {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_RES_RESIDENT_INVALID, args[0]));
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
