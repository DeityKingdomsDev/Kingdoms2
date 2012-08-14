package com.imdeity.kingdoms.cmds.plot;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class PlotSetCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        Town town = resident.getTown();
        KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
        if (chunk == null) { return true; }
        if (chunk.getType() == KingdomsChunk.ChunkType.WILDERNESS || !chunk.getTown().equals(resident.getTown())) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_PLOT_INVALID_LOCATION);
            return true;
        }
        if (!resident.isMayor() && !resident.isSeniorAssistant() && !resident.isAssistant() && (chunk.getOwner() == null || !chunk.getOwner().equals(player.getName()))) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
            return true;
        }
        
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("For-Sale")) {
                int price = town.getDefaultPlotPrice();
                if (args.length > 1) {
                    try {
                        price = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_WARN_INVALID_PRICE);
                    }
                }
                chunk.setForSale(true);
                chunk.setPrice(price);
                chunk.setUpdated();
                chunk.save();
                if(chunk.isForSale()) {
                	KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_PLOT_SET_FORSALE_PLAYER, price));
                } else {
                	KingdomsMain.plugin.chat.sendPlayerMessage(player, "There was an error setting this plot for sale");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("Not-For-Sale")) {
            	if(!chunk.isForSale())
            	{
            		KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_PLOT_NOT_FOR_SALE);
            		return true;
            	}
                chunk.setOwner(null);
                chunk.setForSale(false);
                chunk.setPrice(0);
                chunk.setUpdated();
                chunk.save();
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_PLOT_SET_NOTFORSALE_PLAYER);
                return true;
            } else if (args[0].equalsIgnoreCase("mob-spawning")) {
                boolean allow = args[1].equalsIgnoreCase("allow");
                if (allow) {
                    double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN, player.getWorld().getName()));
                    if (!town.canPay(cost)) {
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
                        return true;
                    }
                    town.pay(cost, "Plot Set Mob-Spawning - " + player.getName());
                }
                chunk.setMobSpawning(allow);
                chunk.setUpdated();
                chunk.save();
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_PLOT_SET_MOBSPAWN_PLAYER, (allow ? "allow" : "deny")));
                return true;
            } else if (args[0].equalsIgnoreCase("pvp")) {
                boolean allow = args[1].equalsIgnoreCase("allow");
                if (allow) {
                    double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_PVP, player.getWorld().getName()));
                    if (!town.canPay(cost)) {
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
                        return true;
                    }
                    town.pay(cost, "Plot Set PVP - " + player.getName());
                }
                chunk.setPvp(allow);
                chunk.setUpdated();
                chunk.save();
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_PLOT_SET_PVP_PLAYER, (allow ? "allow" : "deny")));
                return true;
            } else if (args[0].equalsIgnoreCase("explode")) {
                boolean allow = args[1].equalsIgnoreCase("allow");
                if (allow) {
                    double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_EXPLODE, player.getWorld().getName()));
                    if (!town.canPay(cost)) {
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY_TOWN);
                        return true;
                    }
                    town.pay(cost, "Plot Set Explode - " + player.getName());
                }
                chunk.setExplode(allow);
                chunk.setUpdated();
                chunk.save();
                KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_PLOT_SET_EXPLODE_PLAYER, (allow ? "allow" : "deny")));
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
