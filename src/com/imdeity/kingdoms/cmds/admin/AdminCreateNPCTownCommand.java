package com.imdeity.kingdoms.cmds.admin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Town;

public class AdminCreateNPCTownCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length == 0) { return false; }
        KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        if (chunk != null && chunk.getType() == KingdomsChunk.ChunkType.TOWN) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_LOCATION);
            return true;
        }
        String townName = args[0];
        if (!KingdomsHelper.verifyName(townName)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_NAME_INVALID, townName));
            return true;
        }
        if (KingdomsHelper.verifyNameExist(townName, true)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_TOWN_CREATE_EXIST, townName));
            return true;
        }
        new Runner(player, townName, chunk);
        return true;
    }
    
    public class Runner implements Runnable {
        
        private Player player;
        private String townName;
        private KingdomsChunk chunk;
        
        public Runner(Player player, String townName, KingdomsChunk chunk) {
            this.player = player;
            this.townName = townName;
            this.chunk = chunk;
            KingdomsMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(KingdomsMain.plugin, this);
        }
        
        @Override
        public void run() {
            try {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.VERIFING_LOCATION);
                int[] coords = KingdomsHelper.checkSurroundingPlots(player.getLocation(), KingdomsMain.plugin.config.getInt(String.format(KingdomsConfigHelper.TOWN_BORDER, player.getWorld())));
                if (coords != null) {
                    Town surroundingTown = KingdomsManager.getTown(coords[0]);
                    if (surroundingTown != null) {
                        Location closestLocation = new Location(player.getWorld(), coords[1] * 16, player.getLocation().getBlockY(), coords[2] * 16);
                        String direction = DeityAPI.getAPI().getPlayerAPI().getDirectionTo(player.getLocation(), closestLocation);
                        int distance = (int) closestLocation.distance(player.getLocation());
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_TOO_CLOSE, surroundingTown.getName(), distance, direction));
                        return;
                    }
                }
                KingdomsManager.addNewSpawnLocation(player.getLocation());
                KingdomsManager.addNewTown(townName, KingdomsManager.getTownSpawnLocation(player.getLocation()), false);
                Town town = KingdomsManager.getTown(townName);
                if (chunk.getId() <= 0) {
                    chunk = KingdomsManager.addNewKingdomsChunk(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ(), town);
                }
                town.claim(chunk);
                KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_CREATE_SUCCESS_PUBLIC, player.getName(), town.getName()));
            } catch (Exception e) {
            }
        }
    }
}
