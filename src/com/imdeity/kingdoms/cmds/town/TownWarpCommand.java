package com.imdeity.kingdoms.cmds.town;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;
import com.imdeity.kingdoms.obj.TownWarp;

public class TownWarpCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) { return false; }
        if (args[0].equalsIgnoreCase("list")) {
            if (!DeityAPI.getAPI().getPermAPI().hasPermission(player, "kingdoms.town.warp.list")) {
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "You have &cinvalid &fpermissions");
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "Type &e/town help &ffor a list of valid commands");
                return true;
            }
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident == null) { return false; }
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            Town town = resident.getTown();
            
            List<String> output = new ArrayList<String>();
            output.add("&6Town Warps: ");
            output.add("&6+------------+");
            output.add(DeityAPI.getAPI().getUtilAPI().getStringUtils().join(town.getAllWarpNames(), "&7, &f"));
            for (String s : output) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, s);
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!DeityAPI.getAPI().getPermAPI().hasPermission(player, "kingdoms.town.warp.add")) {
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "You have &cinvalid &fpermissions");
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "Type &e/town help &ffor a list of valid commands");
                return true;
            }
            if (args.length < 2) { return false; }
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident == null) { return false; }
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            if (!resident.isKing() && !resident.isMayor() && !resident.isAssistant()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
                return true;
            }
            Town town = resident.getTown();
            double price = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_WARP_ADD, town
                    .getSpawnLocation().getWorld().getName()));
            if (!town.canPay(price)) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
                return true;
            }
            KingdomsChunk kChunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
            if (kChunk.getTown() == null || !kChunk.getTown().getName().equalsIgnoreCase(town.getName()) || kChunk.getOwner() != null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_WARP);
                return true;
            }
            
            if (!kChunk.getWorld().getName().equalsIgnoreCase(town.getSpawnLocation().getWorld().getName())) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "Warp has to be in the same world as the town...");
                return true;
            }
            int cost = 0;
            if (args.length > 2) {
                try {
                    cost = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    cost = 0;
                }
            }
            
            new WarpAdd(player, resident, town, args[1], cost, price);
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!DeityAPI.getAPI().getPermAPI().hasPermission(player, "kingdoms.town.warp.remove")) {
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "You have &cinvalid &fpermissions");
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "Type &e/town help &ffor a list of valid commands");
                return true;
            }
            if (args.length < 2) { return false; }
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident == null) { return false; }
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            if (!resident.isKing() && !resident.isMayor() && !resident.isAssistant()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
                return true;
            }
            Town town = resident.getTown();
            new WarpRemove(player, town, args[1]);
        } else {
            if (!DeityAPI.getAPI().getPermAPI().hasPermission(player, "kingdoms.town.warp.use")) {
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "You have &cinvalid &fpermissions");
                DeityAPI.getAPI().getChatAPI()
                        .sendPlayerMessage(player, KingdomsMain.plugin.getName(), "Type &e/town help &ffor a list of valid commands");
                return true;
            }
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident == null) { return false; }
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            Town town = resident.getTown();
            TownWarp warp = town.getWarp(args[0]);
            if (warp == null) {
                resident.sendMessage("Something went wrong. Please try again..");
                return true;
            }
            if (!town.canPay(warp.getCost())) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
                return true;
            }
            resident.pay(warp.getCost(), "Town Warp Use");
            resident.teleport(warp.getLocation());
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "Welcome to " + warp.getName());
        }
        return true;
    }
    
    private class WarpAdd implements Runnable {
        
        private Player player;
        private Resident resident;
        private Town town;
        private String name;
        private int cost;
        private double price;
        
        public WarpAdd(Player player, Resident resident, Town town, String name, int cost, double price) {
            this.player = player;
            this.resident = resident;
            this.town = town;
            this.name = name;
            this.cost = cost;
            this.price = price;
        }
        
        public void run() {
            if (town.getWarp(name) != null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "Warp " + name + " already exists");
                return;
            }
            resident.pay(price, "Town Warp Create");
            town.addWarp(name, player.getLocation(), cost);
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "Added the warp: " + name);
        }
    }
    
    private class WarpRemove implements Runnable {
        
        private Player player;
        private Town town;
        private String name;
        
        public WarpRemove(Player player, Town town, String name) {
            this.player = player;
            this.town = town;
            this.name = name;
        }
        
        public void run() {
            TownWarp warp = town.getWarp(name);
            if (warp != null) {
                KingdomsManager.removeTownWarp(warp.getId());
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "Removed " + name);
            } else {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "Cannot remove warp " + name);
            }
        }
    }
}
