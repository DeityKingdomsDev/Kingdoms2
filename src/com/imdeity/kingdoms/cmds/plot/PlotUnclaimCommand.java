package com.imdeity.kingdoms.cmds.plot;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsChunk.ChunkType;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class PlotUnclaimCommand extends DeityCommandReceiver {
    
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
        if (kChunk.getType() == ChunkType.WILDERNESS || !kChunk.getTown().getName().equalsIgnoreCase(resident.getTown().getName())) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_PLOT_INVALID_LOCATION);
            return true;
        }
        if (!resident.isMayor() && !resident.isSeniorAssistant() && !resident.isAssistant() && !resident.isKing()
                && !resident.getName().equalsIgnoreCase(kChunk.getOwner())) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_PLOT_NOT_OWNER);
            return true;
        }
        kChunk.setForSale(false);
        kChunk.setPrice(0);
        kChunk.setOwner(null);
        kChunk.save();
        KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_PLOT_UNCLAIM_PLAYER);
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
