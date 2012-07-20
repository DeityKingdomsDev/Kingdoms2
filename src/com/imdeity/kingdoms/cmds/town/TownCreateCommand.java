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
        if (!resident.isLeastLevelOneNoble()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_NOT_NOBLE);
            return true;
        }
        KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        if (chunk != null && chunk.getType() == KingdomsChunk.ChunkType.TOWN) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_INVALID_LOCATION);
            return true;
        }
        double cost = KingdomsMain.plugin.config.getDouble(KingdomsConfigHelper.TOWN_PRICES_CREATE);
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
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_FAIL_TOWN_CREATE_EXIST, townName));
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
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.VERIFING_LOCATION);
                int[] coords = KingdomsHelper.checkSurroundingPlots(player.getLocation(), KingdomsMain.plugin.config.getInt(KingdomsConfigHelper.TOWN_BORDER));
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
                if (resident != null) {
                    coords = null;
                    if (resident.hasDeed()) {
                        Kingdom kingdom = KingdomsManager.getKingdom(resident.getDeed());
                        if (kingdom != null) {
                            coords = KingdomsHelper.checkSurroundingPlots(player.getLocation(), kingdom, KingdomsMain.plugin.config.getInt(KingdomsConfigHelper.KINGDOM_BORDER));
                        }
                    } else {
                        coords = KingdomsHelper.checkSurroundingPlots(player.getLocation(), KingdomsMain.plugin.config.getInt(KingdomsConfigHelper.KINGDOM_BORDER));
                    }
                    if (coords != null) {
                        Town surroundingTown = KingdomsManager.getTown(coords[0]);
                        if (surroundingTown != null && surroundingTown.getKingdom() != null) {
                            Location closestLocation = new Location(player.getWorld(), coords[1] * 16, player.getLocation().getBlockY(), coords[2] * 16);
                            String direction = DeityAPI.getAPI().getPlayerAPI().getDirectionTo(player.getLocation(), closestLocation);
                            int distance = (int) closestLocation.distance(player.getLocation());
                            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_KINGDOM_TOO_CLOSE, surroundingTown.getKingdom().getName(), distance, direction));
                            return;
                        }
                    }
                }
                KingdomsManager.addNewSpawnLocation(player.getLocation());
                KingdomsManager.addNewTown(townName, KingdomsManager.getTownSpawnLocation(player.getLocation()), false);
                Town town = KingdomsManager.getTown(townName);
                if (chunk.getId() <= 0) {
                    chunk = KingdomsManager.addNewKingdomsChunk(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ(), town);
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
                KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_CREATE_SUCCESS_PUBLIC, resident.getName(), town.getName()));
            } catch (Exception e) {
            }
        }
    }
}
