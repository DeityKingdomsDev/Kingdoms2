package com.imdeity.kingdoms.obj;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.kingdoms.main.KingdomsMain;

public class KingdomsHelper {
    
    private static final String PLAYER_MARKER = "&7[&cP&7]";
    private static final int MAP_X_SIZE = 5;
    private static final int MAP_Z_SIZE = 9;
    
    public static List<String> getMap(Player player, Location location, int direction, int maxXCoord, int maxZCoord) {
        List<String> out = new ArrayList<String>();
        out.add("&6+--------------------------+");
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        
        int xMax = (maxXCoord / 2);
        int zMax = (maxZCoord / 2);
        
        char frontFriendlyDirection = DeityAPI.getAPI().getPlayerAPI().getFrontFriendlyCardinalDirection(direction);
        char leftFriendlyDirection = DeityAPI.getAPI().getPlayerAPI().getLeftFriendlyCardinalDirection(direction);
        char rightFriendlyDirection = DeityAPI.getAPI().getPlayerAPI().getRightFriendlyCardinalDirection(direction);
        char backFriendlyDirection = DeityAPI.getAPI().getPlayerAPI().getBackFriendlyCardinalDirection(direction);
        for (int x = 0; x < maxXCoord; x++) {
            String output = "";
            for (int z = 0; z < maxZCoord; z++) {
                int newXCoord = 0;
                int newZCoord = 0;
                switch (direction) {
                    case 0:
                        // SOUTH
                        newXCoord = ((xCoord) + xMax) - x;
                        newZCoord = ((zCoord) - zMax) + z;
                        break;
                    case 1:
                        // WEST
                        newZCoord = ((zCoord) + xMax) - x;
                        newXCoord = ((xCoord) + zMax) - z;
                        break;
                    case 2:
                        // NORTH
                        newXCoord = ((xCoord) - xMax) + x;
                        newZCoord = ((zCoord) + zMax) - z;
                        break;
                    case 3:
                        // EAST
                        newZCoord = ((zCoord) - xMax) + x;
                        newXCoord = ((xCoord) - zMax) + z;
                        break;
                }
                if (newXCoord == xCoord && newZCoord == zCoord) {
                    output += PLAYER_MARKER;
                } else {
                    KingdomsChunk chunk = KingdomsManager.getKingdomsChunk(location.getWorld(), newXCoord, newZCoord, false);
                    if (KingdomsManager.getResident(player.getName()) != null) {
                        output += chunk.getMapName(KingdomsManager.getResident(player.getName()));
                    }
                }
                output += " ";
            }
            switch (x) {
                case 0:
                    output += "&6|";
                    break;
                case 1:
                    output += "&6|       &4" + frontFriendlyDirection;
                    break;
                case 2:
                    output += "&6|     &7" + leftFriendlyDirection + " . " + rightFriendlyDirection;
                    break;
                case 3:
                    output += "&6|       &7" + backFriendlyDirection;
                    break;
                case 4:
                    output += "&6|    &8" + "(&7" + xCoord + "&8,&7" + zCoord + "&8)";
                    break;
                default:
                    break;
            }
            out.add(output);
        }
        return out;
    }
    
    public static void getMapHelp(Player player) {
        KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(
                player,
                KingdomsChunk.getFormat(0) + " &f= Wilderness, " + KingdomsChunk.getFormat(2) + " &f= Your plots, " + KingdomsChunk.getFormat(1) + " &f= For Sale Plots, " + KingdomsChunk.getFormat(3) + " &f= Owned Plots, " + KingdomsChunk.getFormat(4) + " &f= Blank Plots, "
                        + KingdomsChunk.getFormat(5) + " &f= Other Towns, " + PLAYER_MARKER + " &f= Your Location");
    }
    
    public static void sendMap(Player player, Location location) {
        for (String s : getMap(player, location, DeityAPI.getAPI().getPlayerAPI().getCardinalDirection(player), MAP_X_SIZE, MAP_Z_SIZE)) {
            KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
        }
    }
    
    public static Town checkAdjacentPlots(Location location, int diameter) {
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        
        String sql = "SELECT kc.town_id FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks")
                + " kc WHERE dpc.id = kc.deity_protect_id  AND (dpc.x_coord-? <= ? AND ? <= dpc.x_coord+?) AND (dpc.z_coord-? <= ? AND ? <= dpc.z_coord+?);";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, diameter, xCoord, xCoord, diameter, diameter, zCoord, zCoord, diameter);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    return KingdomsManager.getTown(query.getInteger(i, "town_id"));
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static Town checkAdjacentPlots(Location location, Town town, int diameter) {
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        
        String sql = "=SELECT kc.town_id FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks")
                + " kc WHERE kc.town_id != ? AND dpc.id = kc.deity_protect_id AND (dpc.x_coord-? <= ? AND ? <= dpc.x_coord+?) AND (dpc.z_coord-? <= ? AND ? <= dpc.z_coord+?);";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, town.getId(), diameter, xCoord, xCoord, diameter, diameter, zCoord, zCoord, diameter);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    return KingdomsManager.getTown(query.getInteger(i, "town_id"));
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static int[] checkSurroundingPlots(Location location, int diameter) {
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        
        String sql = "SELECT kc.town_id, dpc.x_coord, dpc.z_coord FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks")
                + " kc WHERE dpc.id = kc.deity_protect_id AND (dpc.x_coord-? <= ? AND ? <= dpc.x_coord+?) AND (dpc.z_coord-? <= ? AND ? <= dpc.z_coord+?);";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, diameter, xCoord, xCoord, diameter, diameter, zCoord, zCoord, diameter);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int[] tmp = { query.getInteger(i, "town_id"), query.getInteger(i, "x_coord"), query.getInteger(i, "z_coord") };
                    return tmp;
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static int[] checkSurroundingPlots(Location location, Town town, int diameter) {
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        String sql = "SELECT kc.town_id, dpc.x_coord, dpc.z_coord FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks")
                + " kc WHERE kc.town_id != ? AND dpc.id = kc.deity_protect_id AND (dpc.x_coord-? <= ? AND ? <= dpc.x_coord+?) AND (dpc.z_coord-? <= ? AND ? <= dpc.z_coord+?);";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, town.getId(), diameter, xCoord, xCoord, diameter, diameter, zCoord, zCoord, diameter);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int[] tmp = { query.getInteger(i, "town_id"), query.getInteger(i, "x_coord"), query.getInteger(i, "z_coord") };
                    return tmp;
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static int[] checkSurroundingPlots(Location location, Kingdom kingdom, int diameter) {
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        
        String sql = "SELECT kc.town_id, dpc.x_coord, dpc.z_coord FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "towns") + "kt,"
                + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks") + " kc WHERE kt.kingdom_id != ? AND kt.id = kc.town_id AND dpc.id = kc.deity_protect_id AND (dpc.x_coord-? <= ? AND ? <= dpc.x_coord+?) AND (dpc.z_coord-? <= ? AND ? <= dpc.z_coord+?);";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, kingdom.getId(), diameter, xCoord, xCoord, diameter, diameter, zCoord, zCoord, diameter);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int[] tmp = { query.getInteger(i, "town_id"), query.getInteger(i, "x_coord"), query.getInteger(i, "z_coord") };
                    return tmp;
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static boolean isAdjacentPlotWithLocation(Location location, Town town) {
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        
        String sql = "SELECT dpc.x_coord, dpc.z_coord FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks")
                + " kc WHERE kc.town_id = ? AND dpc.id = kc.deity_protect_id AND (((dpc.x_coord = (?+1) OR dpc.x_coord = (?-1)) AND dpc.z_coord = ?) OR ((dpc.z_coord = (?+1) OR dpc.z_coord = (?-1)) AND dpc.x_coord = ?));";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, town.getId(), xCoord, xCoord, zCoord, zCoord, zCoord, xCoord);
        if (query != null && query.hasRows()) { return true; }
        return false;
    }
    
    public static boolean verifyName(String name) {
        if (name.length() > 20) { return false; }
        for (int i = 0; i < name.length(); i++) {
            if (('a' <= name.charAt(i) && name.charAt(i) <= 'z') || ('A' <= name.charAt(i) && name.charAt(i) <= 'Z') || '-' == name.charAt(i) || name.charAt(i) == '_') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    public static boolean verifyNameExist(String name, boolean isTown) {
        if (isTown) {
            return KingdomsManager.doesTownExist(name);
        } else {
            return KingdomsManager.doesKingdomExist(name);
        }
    }
}
