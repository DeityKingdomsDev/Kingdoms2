package com.imdeity.kingdoms.cmds.plot;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class PlotClaimCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        KingdomsChunk kChunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        if (kChunk == null) { return true; }
        if (kChunk.getType() == KingdomsChunk.ChunkType.WILDERNESS || !kChunk.getTown().equals(resident.getTown())) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_PLOT_INVALID_LOCATION);
            return true;
        }
        if (!kChunk.isForSale()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_PLOT_NOT_FOR_SALE);
            return true;
        }
        int price = kChunk.getPrice();
        if (!resident.canPay(price)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY);
            return true;
        }
        resident.pay(kChunk.getTown().getEconName(), price, "Plot Claim");
        kChunk.setPrice(0);
        kChunk.setForSale(false);
        kChunk.setOwner(resident.getName());
        kChunk.save();
        KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_PLOT_CLAIM_PLAYER, price));
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
