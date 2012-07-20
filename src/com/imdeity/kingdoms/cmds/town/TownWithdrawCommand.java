package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownWithdrawCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        Town town = null;
        int amount = 0;
        if (args.length == 1) {
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            town = resident.getTown();
            
            if (!resident.isMayor() && !resident.isAssistant()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
                return true;
            }
            
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_PRICE);
                return true;
            }
            if (!resident.canPay(amount)) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY);
                return true;
            }
            town.pay(resident.getName(), amount, "Town Withdraw");
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_WITHDRAW_PLAYER, amount, town.getName()));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
