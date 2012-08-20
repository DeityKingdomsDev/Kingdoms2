package com.imdeity.kingdoms.cmds.town;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownCreateCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length == 0) { return false; }
        if (resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_CREATE_RESIDENT_IN_TOWN);
            return true;
        }
        KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        if (chunk != null && chunk.getType() == KingdomsChunk.ChunkType.TOWN) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_LOCATION);
            return true;
        }
        double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_CREATE, player.getWorld()
                .getName()));
        if (!resident.canPay(cost)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY);
            return true;
        }
        String townName = args[0];
        if (!KingdomsHelper.verifyName(townName)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_NAME_INVALID, townName));
            return true;
        }
        if (KingdomsHelper.verifyNameExist(townName, true)) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player,
                    String.format(KingdomsMessageHelper.CMD_FAIL_TOWN_CREATE_EXIST, townName));
            return true;
        }
        new Runner(resident, player, townName, chunk, cost);
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public class Runner implements Runnable {
        
        private Resident resident;
        private Player player;
        private String townName;
        private KingdomsChunk chunk;
        private double cost;
        
        public Runner(Resident resident, Player player, String townName, KingdomsChunk chunk, double cost) {
            this.resident = resident;
            this.player = player;
            this.townName = townName;
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
                    Town closestTown = chunk.getClosestTownNotInTown(playerLocation, townBorder);
                    if (closestTown != null) {
                        Location closestLocation = closestTown.getClosestLocation(playerLocation);
                        String direction = DeityAPI.getAPI().getPlayerAPI().getDirectionTo(playerLocation, closestLocation);
                        int distance = (int) closestLocation.distance(playerLocation);
                        KingdomsMain.plugin.chat.sendPlayerMessage(player,
                                String.format(KingdomsMessageHelper.CMD_TOWN_TOO_CLOSE, closestTown.getName(), distance, direction));
                        return;
                    }
                }
                int kingdomBorder = KingdomsMain.plugin.config.getInt(String.format(KingdomsConfigHelper.KINGDOM_BORDER,
                        playerLocation.getWorld().getName()));
                if (chunk.isTooClose(playerLocation, kingdomBorder)) {
                    Town closestTown = null;
                    if (resident.hasDeed()) {
                        Kingdom kingdom = KingdomsManager.getKingdom(resident.getDeed());
                        closestTown = chunk.getClosestTownNotInKingdom(playerLocation, kingdomBorder, kingdom.getId());
                    } else {
                        closestTown = chunk.getClosestTownNotInKingdom(playerLocation, kingdomBorder);
                    }
                    if (closestTown != null) {
                        Location closestLocation = closestTown.getClosestLocation(playerLocation);
                        String direction = DeityAPI.getAPI().getPlayerAPI().getDirectionTo(playerLocation, closestLocation);
                        int distance = (int) closestLocation.distance(playerLocation);
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_KINGDOM_TOO_CLOSE,
                                closestTown.getKingdom().getName(), distance, direction));
                        return;
                    }
                }
                KingdomsManager.addNewSpawnLocation(player.getLocation());
                KingdomsManager.addNewTown(townName, KingdomsManager.getTownSpawnLocation(player.getLocation()), false);
                Town town = KingdomsManager.getTown(townName);
                if (town == null) {
                    town = KingdomsManager.getTown(townName);
                }
                if (chunk.getId() <= 0) {
                    chunk = KingdomsManager.addNewKingdomsChunk(playerLocation.getWorld(), playerLocation.getChunk().getX(), player
                            .getLocation().getChunk().getZ(), town);
                }
                town.addResident(resident);
                town.setMayor(resident);
                town.claim(chunk);
                if (resident.hasDeed()) {
                    Kingdom kingdom = KingdomsManager.getKingdom(resident.getDeed());
                    if (kingdom != null) {
                        kingdom.addTown(town, false);
                    }
                    resident.setDeed(-1);
                }
                resident.save();
                resident.pay(cost, "Town Creation");
                KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_CREATE_SUCCESS_PUBLIC,
                        resident.getName(), town.getName()));
            } catch (Exception e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "There was an error claiming the plot. Please inform an admin.");
                e.printStackTrace();
            }
        }
    }
}
