package com.imdeity.kingdoms.cmds.resident;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsChunk.ChunkPermissionGroupTypes;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.protect.enums.DeityChunkPermissionTypes;

public class ResidentSetCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("gender")) {
                boolean isMale = false;
                String output = "";
                if (args[1].equalsIgnoreCase("male") || args[1].equalsIgnoreCase("m")) {
                    isMale = true;
                    output = "male";
                } else {
                    isMale = false;
                    output = "female";
                }
                resident.setMale(isMale);
                resident.save();
                KingdomsMain.plugin.chat.sendPlayerMessage(player,
                        String.format(KingdomsMessageHelper.CMD_RESIDENT_CHANGE_GENDER, output));
                return true;
            } else if (args[0].equalsIgnoreCase("permissions")) {
                String node = args[1];
                String value = args[2];
                if (DeityChunkPermissionTypes.EDIT.name().equalsIgnoreCase(node)) {
                    ChunkPermissionGroupTypes group = ChunkPermissionGroupTypes.getFromString(value);
                    if (group != null) {
                        resident.setPermissions(DeityChunkPermissionTypes.EDIT, group);
                        resident.save();
                        KingdomsMain.plugin.chat.sendPlayerMessage(
                                player,
                                String.format(KingdomsMessageHelper.CMD_RES_SET_PERMISSIONS_UPDATED,
                                        DeityChunkPermissionTypes.EDIT.toString(), group.toString()));
                        return true;
                    }
                } else if (DeityChunkPermissionTypes.USE.name().equalsIgnoreCase(node)) {
                    ChunkPermissionGroupTypes group = ChunkPermissionGroupTypes.getFromString(value);
                    if (group != null) {
                        resident.setPermissions(DeityChunkPermissionTypes.USE, group);
                        resident.save();
                        KingdomsMain.plugin.chat.sendPlayerMessage(
                                player,
                                String.format(KingdomsMessageHelper.CMD_RES_SET_PERMISSIONS_UPDATED,
                                        DeityChunkPermissionTypes.USE.toString(), group.toString()));
                        return true;
                    }
                } else if (DeityChunkPermissionTypes.ACCESS.name().equalsIgnoreCase(node)) {
                    ChunkPermissionGroupTypes group = ChunkPermissionGroupTypes.getFromString(value);
                    if (group != null) {
                        resident.setPermissions(DeityChunkPermissionTypes.ACCESS, group);
                        resident.save();
                        KingdomsMain.plugin.chat.sendPlayerMessage(
                                player,
                                String.format(KingdomsMessageHelper.CMD_RES_SET_PERMISSIONS_UPDATED,
                                        DeityChunkPermissionTypes.ACCESS.toString(), group.toString()));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
}
