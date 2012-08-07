package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsChunk.ChunkPermissionGroupTypes;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;
import com.imdeity.protect.enums.DeityChunkPermissionTypes;

public class TownSetCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (!resident.isMayor() && !resident.isSeniorAssistant()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
            return true;
        }
        Town town = resident.getTown();
        if (args.length == 0) {
            return false;
        } else if (args[0].equalsIgnoreCase("permissions")) {
            String node = args[1];
            String value = args[2];
            if (DeityChunkPermissionTypes.EDIT.name().equalsIgnoreCase(node)) {
                ChunkPermissionGroupTypes group = ChunkPermissionGroupTypes.getFromString(value);
                if (group != null) {
                    town.setPermissions(DeityChunkPermissionTypes.EDIT, group);
                    town.save();
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_PERMISSIONS_UPDATED, DeityChunkPermissionTypes.EDIT.toString(), group.toString()));
                    return true;
                }
            } else if (DeityChunkPermissionTypes.USE.name().equalsIgnoreCase(node)) {
                ChunkPermissionGroupTypes group = ChunkPermissionGroupTypes.getFromString(value);
                if (group != null) {
                    town.setPermissions(DeityChunkPermissionTypes.USE, group);
                    town.save();
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_PERMISSIONS_UPDATED, DeityChunkPermissionTypes.USE.toString(), group.toString()));
                    return true;
                }
            } else if (DeityChunkPermissionTypes.ACCESS.name().equalsIgnoreCase(node)) {
                ChunkPermissionGroupTypes group = ChunkPermissionGroupTypes.getFromString(value);
                if (group != null) {
                    town.setPermissions(DeityChunkPermissionTypes.ACCESS, group);
                    town.save();
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_PERMISSIONS_UPDATED, DeityChunkPermissionTypes.ACCESS.toString(), group.toString()));
                    return true;
                }
            }
            return false;
        } else if (args[0].equalsIgnoreCase("spawn")) {
            KingdomsChunk kChunk = KingdomsManager.getKingdomsChunk(player.getLocation(), false);
            if (kChunk.getTown() == null || !kChunk.getTown().getName().equalsIgnoreCase(town.getName()) || kChunk.getOwner() != null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_TOWN_SET_SPAWN);
                return true;
            }
            town.getTownSpawnLocation().setLocation(player.getLocation());
            town.getTownSpawnLocation().save();
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_SPAWN_PLAYER, player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
            return true;
        } else if (args[0].equalsIgnoreCase("town-board")) {
            if (args.length <= 1) { return false; }
            String newTownBoard = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(DeityAPI.getAPI().getUtilAPI().getStringUtils().remFirstArg(args), " ");
            town.setTownBoard(newTownBoard);
            town.save();
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_BOARD_PLAYER, newTownBoard));
            return true;
        } else if (args[0].equalsIgnoreCase("plot-price")) {
            if (args.length == 1) { return false; }
            int newPrice = 50;
            try {
                newPrice = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_WARN_INVALID_PRICE);
            }
            town.setDefaultPlotPrice(newPrice);
            town.save();
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_PLOT_PRICE_PLAYER, newPrice));
            return true;
        } else if (args[0].equalsIgnoreCase("public")) {
            if (args.length == 0) { return false; }
            boolean isPublic = args[0].equalsIgnoreCase("on");
            town.setPublic(isPublic);
            if (!isPublic) {
                town.setPermissions(DeityChunkPermissionTypes.ACCESS, ChunkPermissionGroupTypes.TOWN);
            }
            town.save();
            KingdomsMain.plugin.chat.sendPlayerMessage(player, String.format(KingdomsMessageHelper.CMD_TOWN_SET_PUBLIC_PLAYER, (isPublic ? "public" : "private")));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
