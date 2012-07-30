package com.imdeity.kingdoms.cmds.kingdom;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomWithdrawCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        Kingdom kingdom = null;
        int amount = 0;
        if (resident == null) { return false; }
        if (args.length == 1) {
            if (!resident.hasTown() || resident.getTown().getKingdom() == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_KINGDOM);
                return true;
            }
            kingdom = resident.getTown().getKingdom();
            
            if (!resident.isKing()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_KING);
                return true;
            }
            
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_PRICE);
                return true;
            }
            if (!kingdom.canPay(amount)) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "Your kingdom does not have enough money");
                return true;
            }
            kingdom.pay(resident.getName(), amount, "Kingdom Withdraw");
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_KINGDOM_WITHDRAW_PLAYER, amount, kingdom.getName()));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
