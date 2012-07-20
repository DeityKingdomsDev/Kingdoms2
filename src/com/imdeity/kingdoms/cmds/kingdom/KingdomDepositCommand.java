package com.imdeity.kingdoms.cmds.kingdom;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomDepositCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        Kingdom kingdom = null;
        int amount = 0;
        if (args.length == 1) {
            if (!resident.hasTown() || resident.getTown().getKingdom() == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_KINGDOM);
                return true;
            }
            kingdom = resident.getTown().getKingdom();
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_PRICE);
                return true;
            }
        } else {
            kingdom = KingdomsManager.getKingdom(args[0]);
            if (kingdom == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_KINGDOM, args[0]));
                return true;
            }
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_PRICE);
                return true;
            }
        }
        if (!resident.canPay(amount)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY);
            return true;
        }
        resident.pay(kingdom.getEconName(), amount, "Kingdom Deposit");
        KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_KINGDOM_DEPOSIT_PLAYER, amount, kingdom.getName()));
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
