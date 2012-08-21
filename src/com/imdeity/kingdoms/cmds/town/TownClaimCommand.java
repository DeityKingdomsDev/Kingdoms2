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
        if (!resident.isKing() && !resident.isMayor() && !resident.isSeniorAssistant()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
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
        double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_CLAIM, player.getLocation()
                .getWorld().getName()));
        if (!town.canPay(cost)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
            return true;
        }
        new Runner(player, town, chunk, cost);
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public class Runner implements Runnable {
        
        private Player player;
        private Town town;
        private KingdomsChunk chunk;
        private double cost;
        
        public Runner(Player player, Town town, KingdomsChunk chunk, double cost) {
            this.player = player;
            this.town = town;
            this.chunk = chunk;
            this.cost = cost;
            KingdomsMain.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(KingdomsMain.plugin, this);
        }
        
        @Override
        public void run() {
            try {
                Location playerLocation = player.getLocation();
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.VERIFING_LOCATION);
                
                int townBorder = KingdomsMain.plugin.config.getInt(String.format(KingdomsConfigHelper.TOWN_BORDER, playerLocation
                        .getWorld().getName()));
                if (chunk.isTooClose(playerLocation, townBorder)) {
                    Town closestTown = chunk.getClosestTownNotInTown(playerLocation, townBorder, town.getId());
                    if (closestTown != null) {
                        Location closestLocation = closestTown.getClosestLocation(playerLocation);
                        if (closestLocation != null) {
                            String direction = DeityAPI.getAPI().getPlayerAPI().getDirectionTo(playerLocation, closestLocation);
                            int distance = (int) closestLocation.distance(playerLocation);
                            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_TOO_CLOSE,
                                    closestTown.getName(), distance, direction));
                            return;
                        }
                    }
                }
                int kingdomBorder = KingdomsMain.plugin.config.getInt(String.format(KingdomsConfigHelper.KINGDOM_BORDER,
                        playerLocation.getWorld().getName()));
                if (chunk.isTooClose(playerLocation, kingdomBorder)) {
                    Town closestTown = null;
                    if (town.getKingdom() != null) {
                        closestTown = chunk.getClosestTownNotInKingdom(playerLocation, kingdomBorder, town.getKingdom().getId());
                    } else {
                        closestTown = chunk.getClosestTownNotInKingdom(playerLocation, kingdomBorder);
                    }
                    if (closestTown != null) {
                        Location closestLocation = closestTown.getClosestLocation(playerLocation);
                        if (closestLocation != null) {
                            String direction = DeityAPI.getAPI().getPlayerAPI().getDirectionTo(playerLocation, closestLocation);
                            int distance = (int) closestLocation.distance(playerLocation);
                            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(
                                    KingdomsMessageHelper.CMD_KINGDOM_TOO_CLOSE, closestTown.getKingdom().getName(), distance,
                                    direction));
                            return;
                        }
                    }
                }
                if (!KingdomsHelper.isAdjacentPlotWithLocation(playerLocation, town)) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_PLOT_NOT_ADJACENT);
                    return;
                }
                if (chunk.getId() <= 0) {
                    chunk = KingdomsManager.addNewKingdomsChunk(playerLocation.getWorld(), playerLocation.getChunk().getX(), player
                            .getLocation().getChunk().getZ(), town);
                }
                town.claim(chunk);
                town.pay(cost, "Town Claim - " + player.getName());
                town.sendMessage(String.format(KingdomsMessageHelper.CMD_TOWN_CLAIM_TOWN, player.getName(), chunk.getX(), chunk.getZ()));
            } catch (Exception e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "There was an error claiming the plot. Please inform an admin.");
                e.printStackTrace();
            }
        }
    }
}
