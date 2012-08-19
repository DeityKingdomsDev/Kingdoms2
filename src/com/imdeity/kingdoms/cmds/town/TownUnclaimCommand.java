package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownUnclaimCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (!resident.isKing() && !resident.isMayor() && !resident.isSeniorAssistant()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
            return true;
        }
        KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        
        if (chunk.isChunk(resident.getTown().getTownSpawnLocation().getLocation().getWorld().getName(), resident.getTown()
                .getTownSpawnLocation().getLocation().getChunk().getX(), resident.getTown().getTownSpawnLocation().getLocation()
                .getChunk().getZ())) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_UNCLAIM_TOWN_SPAWN);
            return true;
        }
        
        if (chunk == null || chunk.getType() != KingdomsChunk.ChunkType.TOWN || chunk.getTown() == null
                || !chunk.getTown().getName().equalsIgnoreCase(resident.getTown().getName())) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_CLAIM);
            return true;
        } else {
            Town town = resident.getTown();
            if (town.getLandSize() == 1) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_MIN_PLOTS);
                return true;
            }
            town.unclaim(chunk);
            town.sendMessage(String.format(KingdomsMessageHelper.CMD_TOWN_UNCLAIM_TOWN, player.getName(), chunk.getX(), chunk.getZ()));
            return true;
        }
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
