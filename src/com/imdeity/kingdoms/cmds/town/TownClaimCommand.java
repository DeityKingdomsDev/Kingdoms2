package com.imdeity.kingdoms.cmds.town;

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
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownClaimCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (!resident.isMayor() && !resident.isAssistant()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
            return true;
        }
        
        if (!resident.isLeastLevelOneNoble()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_NOT_NOBLE);
            return true;
        }
        
        Town town = resident.getTown();
        if (town.getLandSize() + 1 > town.getMaxLandSize()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_SIZE_TOO_SMALL);
            return true;
        }
        KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        if (chunk != null && chunk.getType() == KingdomsChunk.ChunkType.TOWN) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_LOCATION);
            return true;
        }
        double cost = KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_CLAIM);
        if (!town.canPay(cost)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
            return true;
        }
        new LandClaimRunner(player, town, chunk, cost);
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public class LandClaimRunner implements Runnable {
        
        private Player player;
        private Town town;
        private KingdomsChunk chunk;
        private double cost;
        
        public LandClaimRunner(Player player, Town town, KingdomsChunk chunk, double cost) {
            this.player = player;
            this.town = town;
            this.chunk = chunk;
            this.cost = cost;
            KingdomsMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(KingdomsMain.plugin, this);
        }
        
        @Override
        public void run() {
            try {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.VERIFING_LOCATION);
                int[] coords = KingdomsHelper.checkSurroundingPlots(player.getLocation(), town, KingdomsMain.plugin.config.getInt(KingdomsConfigHelper.TOWN_BORDER));
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
                if (!KingdomsHelper.isAdjacentPlotWithLocation(player.getLocation(), town)) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_PLOT_NOT_ADJACENT);
                    return;
                }
                if (chunk.getId() <= 0) {
                    chunk = KingdomsManager.addNewKingdomsChunk(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ(), town);
                }
                town.claim(chunk);
                town.pay(cost, "Town Claim - " + player.getName());
                town.sendMessage(String.format(KingdomsMessageHelper.CMD_TOWN_CLAIM_TOWN, player.getName(), chunk.getX(), chunk.getZ()));
            } catch (Exception e) {
            }
        }
    }
}
