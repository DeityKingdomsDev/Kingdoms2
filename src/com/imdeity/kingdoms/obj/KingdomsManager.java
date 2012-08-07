package com.imdeity.kingdoms.obj;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsChunk.ChunkPermissionGroupTypes;
import com.imdeity.kingdoms.obj.Request.RequestType;
import com.imdeity.protect.ProtectionManager;
import com.imdeity.protect.enums.DeityChunkPermissionTypes;

public class KingdomsManager {
    
    private static Map<String, Kingdom> kingdoms = new HashMap<String, Kingdom>();
    private static Map<String, Town> towns = new HashMap<String, Town>();
    private static List<KingdomsChunk> loadedChunks = new ArrayList<KingdomsChunk>();
    private static Map<String, Resident> residents = new HashMap<String, Resident>();
    private static List<TownWarp> townWarps = new ArrayList<TownWarp>();
    
    public static void loadAllKingdoms() {
        String sql = "SELECT * FROM " + KingdomsMain.getKingdomTableName() + ";";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int id = query.getInteger(i, "id");
                    String name = query.getString(i, "name");
                    Date creationDate = query.getDate(i, "creation_date");
                    Kingdom kingdom = new Kingdom(id, name, null, creationDate);
                    kingdoms.put(name.toLowerCase(), kingdom);
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void loadOpenRequests(Kingdom kingdom) {
        String sql = "SELECT * FROM " + KingdomsMain.getRequestTableName() + " WHERE requested_id = ? AND type LIKE 'KINGDOM_%' AND is_closed = 0;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, kingdom.getId());
        if (query != null && query.hasRows()) {
            List<Request> requests = new ArrayList<Request>();
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int id = query.getInteger(i, "id");
                    String requestee = query.getString(i, "player_name");
                    RequestType type = RequestType.getFromString(query.getString(i, "type"));
                    int requestTypeId = query.getInteger(i, "requested_id");
                    boolean isApproved = query.getInteger(i, "is_approved") == 1;
                    Date date = query.getDate(i, "date");
                    requests.add(new Request(id, requestee, type, requestTypeId, false, isApproved, date));
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
            kingdom.setRequests(requests);
        }
    }
    
    public static void loadAllTowns() {
        String sql = "SELECT * FROM " + KingdomsMain.getTownTableName() + ";";
        
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                if (query != null && query.hasRows()) {
                    try {
                        String name = query.getString(i, "name");
                        if (towns.containsKey(name)) {
                            continue;
                        }
                        int id = query.getInteger(i, "id");
                        Kingdom kingdom = getKingdom(query.getInteger(i, "kingdom_id"));
                        String townBoard = query.getString(i, "town_board");
                        int defaultPlotPrice = query.getInteger(i, "default_plot_price");
                        TownSpawnLocation spawnLocation = getTownSpawnLocation(query.getInteger(i, "spawn_location_id"));
                        boolean isPublic = (query.getInteger(i, "is_public") == 1);
                        boolean isCapital = (query.getInteger(i, "is_capital") == 1);
                        Date creationDate = query.getDate(i, "creation_date");
                        Map<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes> permissions = new HashMap<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes>();
                        permissions.put(DeityChunkPermissionTypes.EDIT, ChunkPermissionGroupTypes.getFromId(query.getInteger(i, "edit_permission")));
                        permissions.put(DeityChunkPermissionTypes.USE, ChunkPermissionGroupTypes.getFromId(query.getInteger(i, "use_permission")));
                        permissions.put(DeityChunkPermissionTypes.ACCESS, ChunkPermissionGroupTypes.getFromId(query.getInteger(i, "access_permission")));
                        
                        Town town = new Town(id, name, kingdom, townBoard, defaultPlotPrice, spawnLocation, isPublic, isCapital, creationDate, permissions);
                        towns.put(name.toLowerCase(), town);
                    } catch (SQLDataException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static void loadAllTowns(Kingdom kingdom) {
        List<String> kingdomTowns = new ArrayList<String>();
        String sql = "SELECT * FROM " + KingdomsMain.getTownTableName() + " WHERE kingdom_id = ?;";
        
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, kingdom.getId());
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    String name = query.getString(i, "name");
                    if (towns.containsKey(name)) {
                        continue;
                    }
                    int id = query.getInteger(i, "id");
                    String townBoard = query.getString(i, "town_board");
                    int defaultPlotPrice = query.getInteger(i, "default_plot_price");
                    TownSpawnLocation spawnLocation = getTownSpawnLocation(query.getInteger(i, "spawn_location_id"));
                    boolean isPublic = (query.getInteger(i, "is_public") == 1);
                    boolean isCapital = (query.getInteger(i, "is_capital") == 1);
                    Date creationDate = query.getDate(i, "creation_date");
                    Map<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes> permissions = new HashMap<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes>();
                    permissions.put(DeityChunkPermissionTypes.EDIT, ChunkPermissionGroupTypes.getFromId(query.getInteger(i, "edit_permission")));
                    permissions.put(DeityChunkPermissionTypes.USE, ChunkPermissionGroupTypes.getFromId(query.getInteger(i, "use_permission")));
                    permissions.put(DeityChunkPermissionTypes.ACCESS, ChunkPermissionGroupTypes.getFromId(query.getInteger(i, "access_permission")));
                    
                    Town town = new Town(id, name, kingdom, townBoard, defaultPlotPrice, spawnLocation, isPublic, isCapital, creationDate, permissions);
                    towns.put(name.toLowerCase(), town);
                    kingdomTowns.add(town.getName());
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        kingdom.setTowns(kingdomTowns);
    }
    
    public static void loadAllResident(Town town) {
        List<String> townResidents = new ArrayList<String>();
        String residentSql = "SELECT * FROM " + KingdomsMain.getResidentTableName() + " WHERE town_id = ?;";
        
        DatabaseResults residentQuery = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(residentSql, town.getId());
        if (residentQuery != null && residentQuery.hasRows()) {
            for (int i = 0; i < residentQuery.rowCount(); i++) {
                try {
                    int id = residentQuery.getInteger(i, "id");
                    String name = residentQuery.getString(i, "name");
                    boolean isKing = residentQuery.getInteger(i, "is_king") == 1;
                    boolean isMayor = residentQuery.getInteger(i, "is_mayor") == 1;
                    boolean isSeniorAssistant = residentQuery.getInteger(i, "is_senior_assistant") == 1;
                    boolean isAssistant = residentQuery.getInteger(i, "is_assistant") == 1;
                    boolean isMale = residentQuery.getInteger(i, "is_male") == 1;
                    int deed = residentQuery.getInteger(i, "deed");
                    Date firstOnline = residentQuery.getDate(i, "first_online");
                    Date lastOnline = residentQuery.getDate(i, "last_online");
                    int totalOnline = residentQuery.getInteger(i, "total_time_online");
                    Map<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes> permissions = new HashMap<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes>();
                    permissions.put(DeityChunkPermissionTypes.EDIT, ChunkPermissionGroupTypes.getFromId(residentQuery.getInteger(i, "edit_permission")));
                    permissions.put(DeityChunkPermissionTypes.USE, ChunkPermissionGroupTypes.getFromId(residentQuery.getInteger(i, "use_permission")));
                    permissions.put(DeityChunkPermissionTypes.ACCESS, ChunkPermissionGroupTypes.getFromId(residentQuery.getInteger(i, "access_permission")));
                    
                    String friendsSql = "SELECT res.name FROM " + KingdomsMain.getResidentTableName() + " res, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings") + " WHERE resident_id = ? AND res.id = friend_id;";
                    DatabaseResults friendsQuery = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(friendsSql, id);
                    List<String> friends = null;
                    if (friendsQuery != null && friendsQuery.hasRows()) {
                        friends = new ArrayList<String>();
                        for (int ii = 0; ii < friendsQuery.rowCount(); ii++) {
                            String friendName = friendsQuery.getString(ii, "name");
                            friends.add(friendName);
                        }
                    }
                    Resident resident = new Resident(id, name, null, isKing, isMayor, isSeniorAssistant, isAssistant, isMale, deed, friends, permissions, lastOnline, firstOnline, totalOnline);
                    residents.put(name, resident);
                    resident.setTown(town);
                    townResidents.add(resident.getName());
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        town.setResidents(townResidents);
    }
    
    public static void loadAllChunks(Town town) {
        List<Integer> townLand = new ArrayList<Integer>();
        String sql = "SELECT dpc.id AS 'dpcId', dpc.owner AS 'owner', dpc.world, dpc.x_coord, dpc.z_coord, kc.id, kc.town_id, kc.for_sale, kc.price, kc.can_mobs_spawn, kc.can_pvp FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, "
                + KingdomsMain.getChunkTableName() + " kc" + " WHERE kc.town_id = ? AND dpc.id = kc.deity_protect_id;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, town.getId());
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int protectionId = query.getInteger(i, "dpcId");
                    String owner = query.getString(i, "owner");
                    String world = query.getString(i, "world");
                    int xCoord = query.getInteger(i, "x_coord");
                    int zCoord = query.getInteger(i, "z_coord");
                    
                    boolean forSale = (query.getInteger(i, "for_sale") == 1);
                    int price = query.getInteger(i, "price");
                    boolean canMobsSpawn = (query.getInteger(i, "can_mobs_spawn") == 1);
                    boolean canPvp = (query.getInteger(i, "can_pvp") == 1);
                    boolean canExplode = (query.getInteger(i, "can_explode") == 1);
                    
                    KingdomsChunk chunk = new KingdomsChunk(protectionId, KingdomsMain.plugin.getServer().getWorld(world), xCoord, zCoord, owner, town.getId(), KingdomsChunk.ChunkType.TOWN, town, forSale, price, canMobsSpawn, canPvp, canExplode);
                    KingdomsManager.addKingdomsChunkToCache(chunk);
                    townLand.add(chunk.getId());
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        town.setLand(townLand);
    }
    
    public static void loadAllWarps(Town town) {
        Map<String, Integer> innerTownWarps = new HashMap<String, Integer>();
        String sql = "SELECT id, name FROM " + KingdomsMain.getWarpTableName() + " WHERE town_id = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, town.getId());
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int id = query.getInteger(i, "id");
                    String warpName = query.getString(i, "name");
                    Location location = new Location(KingdomsMain.plugin.getServer().getWorld(query.getString(i, "world")), (double) query.getInteger(i, "x_coord"), (double) query.getInteger(i, "y_coord"), (double) query.getInteger(i, "z_coord"), (float) query.getInteger(i, "yaw"),
                            (float) query.getInteger(i, "pitch"));
                    int cost = query.getInteger(i, "cost");
                    TownWarp warp = new TownWarp(id, warpName, town.getId(), location, cost);
                    townWarps.add(warp);
                    innerTownWarps.put(warpName, id);
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        town.setWarps(innerTownWarps);
    }
    
    public static Kingdom getKingdom(String kingdomName) {
        kingdomName = kingdomName.toLowerCase();
        if (kingdoms.containsKey(kingdomName)) { return kingdoms.get(kingdomName); }
        String sql = "SELECT * FROM " + KingdomsMain.getKingdomTableName() + " WHERE name = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, kingdomName);
        if (query != null && query.hasRows()) {
            try {
                int id = query.getInteger(0, "id");
                String name = query.getString(0, "name");
                Date creationDate = query.getDate(0, "creation_date");
                Kingdom kingdom = new Kingdom(id, name, null, creationDate);
                kingdoms.put(name.toLowerCase(), kingdom);
                KingdomsManager.loadOpenRequests(kingdom);
                return kingdom;
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static Kingdom getKingdom(int id) {
        String sql = "SELECT name FROM " + KingdomsMain.getKingdomTableName() + " WHERE id = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, id);
        if (query != null && query.hasRows()) {
            try {
                return getKingdom(query.getString(0, "name"));
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static List<String> getSortedKingdomNames() {
        List<String> kingdomNames = new ArrayList<String>();
        for (Kingdom kingdom : kingdoms.values()) {
            kingdomNames.add(kingdom.getName());
        }
        
        Collections.sort(kingdomNames);
        return kingdomNames;
    }
    
    public static Town getTown(int id) {
        String sql = "SELECT name FROM " + KingdomsMain.getTownTableName() + " WHERE id = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, id);
        if (query != null && query.hasRows()) {
            try {
                return getTown(query.getString(0, "name"));
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static Town getTown(String townName) {
        townName = townName.toLowerCase();
        if (towns.containsKey(townName)) { return towns.get(townName); }
        String sql = "SELECT * FROM " + KingdomsMain.getTownTableName() + " WHERE name = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, townName);
        if (query != null && query.hasRows()) {
            try {
                int id = query.getInteger(0, "id");
                String name = query.getString(0, "name");
                Kingdom kingdom = getKingdom(query.getInteger(0, "kingdom_id"));
                String townBoard = query.getString(0, "town_board");
                int defaultPlotPrice = query.getInteger(0, "default_plot_price");
                TownSpawnLocation spawnLocation = getTownSpawnLocation(query.getInteger(0, "spawn_location_id"));
                boolean isPublic = (query.getInteger(0, "is_public") == 1);
                boolean isCapital = (query.getInteger(0, "is_capital") == 1);
                Date creationDate = query.getDate(0, "creation_date");
                Map<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes> permissions = new HashMap<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes>();
                permissions.put(DeityChunkPermissionTypes.EDIT, ChunkPermissionGroupTypes.getFromId(query.getInteger(0, "edit_permission")));
                permissions.put(DeityChunkPermissionTypes.USE, ChunkPermissionGroupTypes.getFromId(query.getInteger(0, "use_permission")));
                permissions.put(DeityChunkPermissionTypes.ACCESS, ChunkPermissionGroupTypes.getFromId(query.getInteger(0, "access_permission")));
                
                Town town = new Town(id, name, kingdom, townBoard, defaultPlotPrice, spawnLocation, isPublic, isCapital, creationDate, permissions);
                towns.put(name.toLowerCase(), town);
                return town;
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static List<String> getSortedTownNames() {
        List<String> townNames = new ArrayList<String>();
        for (Town town : towns.values()) {
            townNames.add(town.getName());
        }
        
        Collections.sort(townNames);
        return townNames;
    }
    
    public static TownSpawnLocation getTownSpawnLocation(int spawnId) {
        String sql = "SELECT * FROM " + KingdomsMain.getTownSpawnLocationTableName() + " WHERE id = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, spawnId);
        if (query != null && query.hasRows()) {
            try {
                int id = query.getInteger(0, "id");
                String world = query.getString(0, "world");
                int xCoord = query.getInteger(0, "x_coord");
                int yCoord = query.getInteger(0, "y_coord");
                int zCoord = query.getInteger(0, "z_coord");
                int pitch = query.getInteger(0, "pitch");
                int yaw = query.getInteger(0, "yaw");
                TownSpawnLocation location = new TownSpawnLocation(id, new Location(KingdomsMain.plugin.getServer().getWorld(world), xCoord, yCoord, zCoord, pitch, yaw));
                return location;
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static TownSpawnLocation getTownSpawnLocation(Location location) {
        String sql = "SELECT id FROM " + KingdomsMain.getTownSpawnLocationTableName() + " WHERE world = ? AND x_coord = ? AND y_coord = ? AND z_coord = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (query != null && query.hasRows()) {
            try {
                int id = query.getInteger(0, "id");
                return getTownSpawnLocation(id);
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static KingdomsChunk getKingdomsChunk(int id) {
        for (KingdomsChunk chunk : loadedChunks) {
            if (chunk.getId() == id) { return chunk; }
        }
        return null;
    }
    
    public static KingdomsChunk getKingdomsChunk(World world, int xCoord, int zCoord, boolean checkDatabase) {
        for (KingdomsChunk chunk : loadedChunks) {
            if (chunk.isChunk(world.getName(), xCoord, zCoord) && chunk.getKingdomsId() > 0) { return chunk; }
        }
        if (checkDatabase) {
            String sql = "SELECT dpc.id AS 'dpcId', dpc.owner AS 'owner', kc.id, kc.town_id, kc.for_sale, kc.price, kc.can_mobs_spawn, kc.can_pvp FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_protect_", "chunks") + " dpc, " + KingdomsMain.getChunkTableName() + " kc"
                    + " WHERE dpc.world = ? AND dpc.x_coord = ? AND dpc.z_coord = ? AND dpc.id = kc.deity_protect_id;";
            DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, world.getName(), xCoord, zCoord);
            if (query != null && query.hasRows()) {
                try {
                    int protectId = query.getInteger(0, "dpcId");
                    String owner = query.getString(0, "owner");
                    int id = query.getInteger(0, "id");
                    Town town = getTown(query.getInteger(0, "town_id"));
                    boolean forSale = (query.getInteger(0, "for_sale") == 1);
                    int price = query.getInteger(0, "price");
                    boolean canMobsSpawn = (query.getInteger(0, "can_mobs_spawn") == 1);
                    boolean canPvp = (query.getInteger(0, "can_pvp") == 1);
                    boolean canExplode = (query.getInteger(0, "can_explode") == 1);
                    KingdomsChunk chunk = new KingdomsChunk(protectId, world, xCoord, zCoord, owner, id, (town != null ? KingdomsChunk.ChunkType.TOWN : KingdomsChunk.ChunkType.WILDERNESS), town, forSale, price, canMobsSpawn, canPvp, canExplode);
                    loadedChunks.add(chunk);
                    return chunk;
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return new KingdomsChunk(-1, world, xCoord, zCoord, null, -1, KingdomsChunk.ChunkType.WILDERNESS, null, false, 0, true, false, false);
    }
    
    public static KingdomsChunk getKingdomsChunk(Location location, boolean checkDatabase) {
        World world = location.getWorld();
        int xCoord = location.getChunk().getX();
        int zCoord = location.getChunk().getZ();
        return getKingdomsChunk(world, xCoord, zCoord, checkDatabase);
    }
    
    public static Resident getResident(String resident_name) {
        if (residents.containsKey(resident_name)) { return residents.get(resident_name); }
        String residentSql = "SELECT * FROM " + KingdomsMain.getResidentTableName() + " WHERE name = ?;";
        
        DatabaseResults residentQuery = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(residentSql, resident_name);
        if (residentQuery != null && residentQuery.hasRows()) {
            try {
                int id = residentQuery.getInteger(0, "id");
                String name = residentQuery.getString(0, "name");
                boolean isKing = residentQuery.getInteger(0, "is_king") == 1;
                boolean isMayor = residentQuery.getInteger(0, "is_mayor") == 1;
                boolean isSeniorAssistant = residentQuery.getInteger(0, "is_senior_assistant") == 1;
                boolean isAssistant = residentQuery.getInteger(0, "is_assistant") == 1;
                boolean isMale = residentQuery.getInteger(0, "is_male") == 1;
                int townId = residentQuery.getInteger(0, "town_id");
                int deed = residentQuery.getInteger(0, "deed");
                Date firstOnline = residentQuery.getDate(0, "first_online");
                Date lastOnline = residentQuery.getDate(0, "last_online");
                int totalOnline = residentQuery.getInteger(0, "total_time_online");
                Map<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes> permissions = new HashMap<DeityChunkPermissionTypes, KingdomsChunk.ChunkPermissionGroupTypes>();
                permissions.put(DeityChunkPermissionTypes.EDIT, ChunkPermissionGroupTypes.getFromId(residentQuery.getInteger(0, "edit_permission")));
                permissions.put(DeityChunkPermissionTypes.USE, ChunkPermissionGroupTypes.getFromId(residentQuery.getInteger(0, "use_permission")));
                permissions.put(DeityChunkPermissionTypes.ACCESS, ChunkPermissionGroupTypes.getFromId(residentQuery.getInteger(0, "access_permission")));
                
                String friendsSql = "SELECT res.name FROM " + KingdomsMain.getResidentTableName() + " res, " + KingdomsMain.getResidentFriendTableName() + " WHERE resident_id = ? AND res.id = friend_id;";
                DatabaseResults friendsQuery = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(friendsSql, id);
                List<String> friends = null;
                if (friendsQuery != null && friendsQuery.hasRows()) {
                    friends = new ArrayList<String>();
                    for (int i = 0; i < friendsQuery.rowCount(); i++) {
                        String friendName = friendsQuery.getString(i, "name");
                        friends.add(friendName);
                    }
                }
                Resident resident = new Resident(id, name, null, isKing, isMayor, isSeniorAssistant, isAssistant, isMale, deed, friends, permissions, firstOnline, lastOnline, totalOnline);
                residents.put(name, resident);
                System.out.println("7");
                
                if (townId > 0) {
                    resident.setTown(getTown(townId));
                }
                return resident;
            } catch (SQLDataException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    
    public static Resident getResident(int id) {
        String sql = "SELECT name FROM " + KingdomsMain.getResidentTableName() + " WHERE id = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, id);
        if (query != null && query.hasRows()) {
            try {
                return getResident(query.getString(0, "name"));
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private static Request getRequest(String name, RequestType type, int requestedId) {
        String sql = "SELECT * FROM " + KingdomsMain.getRequestTableName() + " WHERE player_name = ? AND type = ? AND requested_id = ? AND is_closed = 0;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, name, type.name(), requestedId);
        if (query != null && query.hasRows()) {
            try {
                int id = query.getInteger(0, "id");
                boolean isApproved = query.getInteger(0, "is_approved") == 1;
                Date date = query.getDate(0, "date");
                return new Request(id, name, type, requestedId, false, isApproved, date);
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static TownWarp getTownWarp(int id) {
        for (TownWarp tw : townWarps) {
            if (tw.getId() == id) { return tw; }
        }
        
        String sql = "SELECT * FROM " + KingdomsMain.getWarpTableName() + " WHERE id = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, id);
        if (query != null && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int townId = query.getInteger(i, "town_id");
                    String warpName = query.getString(i, "name");
                    Location location = new Location(KingdomsMain.plugin.getServer().getWorld(query.getString(i, "world")), (double) query.getInteger(i, "x_coord"), (double) query.getInteger(i, "y_coord"), (double) query.getInteger(i, "z_coord"), (float) query.getInteger(i, "yaw"),
                            (float) query.getInteger(i, "pitch"));
                    int cost = query.getInteger(i, "cost");
                    TownWarp warp = new TownWarp(id, warpName, townId, location, cost);
                    townWarps.add(warp);
                    return warp;
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static TownWarp getTownWarp(String warpName, int townId) {
        String sql = "SELECT id FROM " + KingdomsMain.getWarpTableName() + " WHERE name = ? AND town_id = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, warpName, townId);
        if (query != null && query.hasRows()) {
            int id = -1;
            try {
                id = query.getInteger(0, "id");
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
            return getTownWarp(id);
        }
        return null;
    }
    
    public static int getNumberOfPlots(String playerName) {
        String sql = "SELECT kc.id FROM " + KingdomsMain.getChunkTableName() + " dpc, " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks") + " kc" + " WHERE dpc.owner = ? AND dpc.id = kc.deity_protect_id;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, playerName);
        if (query != null && query.hasRows()) { return query.rowCount(); }
        return 0;
    }
    
    public static int getNumTowns() {
        return towns.size();
    }
    
    public static int getNumResidents() {
        return residents.size();
    }
    
    public static Town getLargestTown() {
        if (towns.isEmpty()) { return null; }
        Town largestTown = towns.values().iterator().next();
        for (Town town : towns.values()) {
            if (largestTown.getResidentsNames().size() < town.getResidentsNames().size()) {
                largestTown = town;
            }
        }
        return largestTown;
    }
    
    public static Kingdom addNewKingdom(String kingdomName) {
        String sql = "INSERT INTO " + KingdomsMain.getKingdomTableName() + " (name, creation_date) VALUES (?, NOW());";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, kingdomName);
        getKingdom(kingdomName).createBankAccount();
        return getKingdom(kingdomName);
    }
    
    public static Request addNewRequest(String name, RequestType type, int requestedId) {
        if (getRequest(name, type, requestedId) == null) {
            String sql = "INSERT INTO " + KingdomsMain.getRequestTableName() + " (player_name, type, requested_id, is_closed) VALUES (?,?,?,?);";
            DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, name, type.name(), requestedId, 0);
        }
        return getRequest(name, type, requestedId);
    }
    
    public static Town addNewTown(String townName, TownSpawnLocation location, boolean isCapital) {
        String sql = "INSERT INTO " + KingdomsMain.getTownTableName() + " (name, town_board, spawn_location_id, is_public, is_capital, creation_date) VALUES " + " (?,?,?,?,?,NOW());";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, townName, KingdomsMain.plugin.config.getString(String.format(KingdomsConfigHelper.DEFAULT_TOWN_BOARD, location.getLocation().getWorld().getName())), location.getId(), 1, (isCapital ? 1 : 0));
        getTown(townName).createBankAccount();
        return getTown(townName);
    }
    
    public static TownSpawnLocation addNewSpawnLocation(Location location) {
        String sql = "INSERT INTO " + KingdomsMain.getTownSpawnLocationTableName() + " (world, x_coord, y_coord, z_coord, pitch, yaw) VALUES (?,?,?,?,?,?);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), (int) location.getPitch(), (int) location.getYaw());
        return getTownSpawnLocation(location);
    }
    
    public static KingdomsChunk addNewKingdomsChunk(World world, int xCoord, int zCoord, Town town) {
        int protectionId = ProtectionManager.addNewDeityChunk(world.getName(), xCoord, zCoord);
        String sql = "INSERT INTO " + KingdomsMain.getChunkTableName() + " (deity_protect_id, town_id, for_sale, price) VALUES (?, ?, 0, 0)";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, protectionId, town.getId());
        return getKingdomsChunk(world, xCoord, zCoord, true);
    }
    
    public static void addNewResident(String playerName) {
        String sql = "INSERT INTO " + KingdomsMain.getResidentTableName() + " (name, town_id, is_king, is_mayor, is_senior_assistant, is_assistant, is_male, last_online, total_time_online) VALUES " + " (?,?,?,?,?,?,?, CURRENT_TIMESTAMP, 0);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, playerName, -1, 0, 0, 0, 0, 1);
        getResident(playerName);
    }
    
    public static void addKingdomsChunkToCache(KingdomsChunk chunk) {
        for (KingdomsChunk c : loadedChunks) {
            if (c.isChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ())) { return; }
        }
        loadedChunks.add(chunk);
    }
    
    public static TownWarp addNewTownWarp(String warpName, int townId, Location location, int price) {
        String sql = "INSERT INTO " + KingdomsMain.getWarpTableName() + " (name, town_id, price, world, x_coord, y_coord, z_coord, yaw, pitch) VALUES (?, ?,?,?,?,?,?,?,?);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, warpName, townId, price, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
        return getTownWarp(warpName, townId);
    }
    
    public static void removeKingdom(String kingdomName) {
        getKingdom(kingdomName);
    }
    
    public static void removeTown(Town town) {
        town.remove();
        towns.remove(town.getName().toLowerCase());
    }
    
    public static void removeTown(String townName) {
        removeTown(getTown(townName));
    }
    
    public static void removeTownSpawnLocation(int spawnLocationId) {
        getTownSpawnLocation(spawnLocationId);
    }
    
    public static void removeKingdomsChunk(World world, int xCoord, int zCoord) {
        getKingdomsChunk(world, xCoord, zCoord, false).remove();
    }
    
    public static void removeKingdomsChunk(KingdomsChunk chunk) {
        int index = -1;
        for (int i = 0; i < loadedChunks.size(); i++) {
            KingdomsChunk c = loadedChunks.get(i);
            if (c.isChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            loadedChunks.remove(index);
        }
    }
    
    public static void removeResident(String playerName) {
        if (residents.containsKey(playerName)) {
            residents.remove(playerName);
        }
    }
    
    public static void removeTownWarp(int id) {
        TownWarp warp = getTownWarp(id);
        int index = -1;
        for (int i = 0; i < townWarps.size(); i++) {
            TownWarp tw = townWarps.get(i);
            if (tw.getId() == id) {
                index = i;
            }
        }
        townWarps.remove(index);
        warp.getTown().removeWarp(warp.getName());
        warp.remove();
    }
    
    public static void reload() {
        kingdoms.clear();
        towns.clear();
        residents.clear();
        loadedChunks.clear();
        townWarps.clear();
        for (Player player : KingdomsMain.plugin.getServer().getOnlinePlayers()) {
            if (player != null) {
                KingdomsManager.getResident(player.getName());
            }
        }
        loadAllKingdoms();
        loadAllTowns();
        KingdomsMain.plugin.chat.out(kingdoms.size() + " kingdoms loaded...");
        KingdomsMain.plugin.chat.out(towns.size() + " towns loaded...");
        KingdomsMain.plugin.chat.out(townWarps.size() + " warps loaded...");
        KingdomsMain.plugin.chat.out(loadedChunks.size() + " chunks loaded...");
        KingdomsMain.plugin.chat.out(residents.size() + " residents loaded...");
    }
    
    public static boolean doesTownExist(String name) {
        name = name.toLowerCase();
        return towns.containsKey(name);
    }
    
    public static boolean doesKingdomExist(String name) {
        name = name.toLowerCase();
        return kingdoms.containsKey(name);
    }
}
