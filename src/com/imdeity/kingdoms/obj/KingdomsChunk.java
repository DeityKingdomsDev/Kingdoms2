package com.imdeity.kingdoms.obj;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.protect.api.DeityChunk;
import com.imdeity.protect.enums.DeityChunkPermissionTypes;

public class KingdomsChunk extends DeityChunk {
    
    private int kingdomsId;
    private ChunkType type;
    private Town town = null;
    private boolean forSale = false;
    private int price = 0;
    private boolean canMobsSpawn = true;
    private boolean canPvp = false;
    private boolean updated = false;
    
    public KingdomsChunk(int id, World world, int xCoord, int zCoord, String owner, int kingdomsId, ChunkType type, Town town, boolean forSale, int price, boolean canMobsSpawn, boolean canPvp) {
        super(id, world, xCoord, zCoord, owner);
        this.kingdomsId = kingdomsId;
        this.type = type;
        this.town = town;
        this.forSale = forSale;
        this.price = price;
        this.canMobsSpawn = canMobsSpawn;
        this.canPvp = canPvp;
    }
    
    public KingdomsChunk(World world, int xCoord, int zCoord) {
        super(-1, world, xCoord, zCoord, null);
        this.type = ChunkType.WILDERNESS;
    }
    
    public int getKingdomsId() {
        return kingdomsId;
    }
    
    public ChunkType getType() {
        return this.type;
    }
    
    public boolean isOwned() {
        return ((this.getType() == ChunkType.TOWN) && getOwner() != null);
    }
    
    public Town getTown() {
        return town;
    }
    
    public void setTown(Town town) {
        this.town = town;
        this.type = ChunkType.TOWN;
        this.setUpdated();
    }
    
    public boolean isForSale() {
        return forSale;
    }
    
    public void setForSale(boolean forSale) {
        this.forSale = forSale;
        this.setUpdated();
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
        this.setUpdated();
    }
    
    public boolean canMobsSpawn() {
        return this.canMobsSpawn;
    }
    
    public void setMobSpawning(boolean canMobsSpawn) {
        this.canMobsSpawn = canMobsSpawn;
        this.setUpdated();
    }
    
    public boolean canPvp() {
        return canPvp;
    }
    
    public void setPvp(boolean canPvp) {
        this.canPvp = canPvp;
        this.setUpdated();
    }
    
    public Resident getResidentOwner() {
        return KingdomsManager.getResident(getOwner());
    }
    
    @Override
    public boolean runPermissionCheck(DeityChunkPermissionTypes type, String resident) {
        if (getType() == ChunkType.TOWN) {
            
            if (type == DeityChunkPermissionTypes.MOB_SPAWNING) { return this.canMobsSpawn(); }
            if (type == DeityChunkPermissionTypes.PVP) { return this.canPvp(); }
            
            Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permission = new HashMap<DeityChunkPermissionTypes, ChunkPermissionGroupTypes>();
            
            if (getOwner() != null) {
                permission = getResidentOwner().getPermissions();
                if (getOwner().equalsIgnoreCase(resident)) { return true; }
                if (getResidentOwner().hasFriend(resident)) { return true; }
            } else {
                permission = getTown().getPermissions();
            }
            
            if (getTown().getMayor().getName().equalsIgnoreCase(resident)) { return true; }
            if (getTown().getKingdom() != null && getTown().getKingdom().getKing().getName().equalsIgnoreCase(resident)) { return true; }
            
            if (permission != null) {
                if (permission.get(type) == ChunkPermissionGroupTypes.PUBLIC) {
                    return true;
                } else if (permission.get(type) == ChunkPermissionGroupTypes.FRIEND) {
                    if (getOwner() != null && getResidentOwner().hasFriend(resident)) { return true; }
                } else if (permission.get(type) == ChunkPermissionGroupTypes.TOWN) {
                    if (getTown() != null && getTown().hasResident(resident)) { return true; }
                } else if (permission.get(type) == ChunkPermissionGroupTypes.KINGDOM) {
                    if (getTown() != null && getTown().getKingdom() != null && getTown().getKingdom().hasResident(resident)) { return true; }
                } else if (permission.get(type) == ChunkPermissionGroupTypes.TOWN_STAFF) {
                    if (getTown() != null && getTown().hasStaff(resident)) { return true; }
                } else if (permission.get(type) == ChunkPermissionGroupTypes.KINGDOM_STAFF) {
                    if (getTown() != null && getTown().getKingdom() != null && getTown().getKingdom().hasStaff(resident)) { return true; }
                }
            }
            return false;
        } else {
            return true;
        }
    }
    
    public void setUpdated() {
        this.updated = true;
    }
    
    public String getMapName(Resident resident) {
        if (this.getType() == ChunkType.WILDERNESS) {
            return getFormat(0);
        } else {
            if (resident.getTown() != null) {
                if (resident.getTown().getName().equalsIgnoreCase(this.getTown().getName())) {
                    if (getOwner() != null) {
                        if (getOwner().equalsIgnoreCase(resident.getName())) {
                            return getFormat(2);
                        } else {
                            return getFormat(3);
                        }
                    } else if (this.isForSale()) { return getFormat(1); }
                    return getFormat(4);
                }
            }
            return getFormat(5);
        }
    }
    
    public static String getFormat(int levelOfAccess) {
        String output = "&7[";
        switch (levelOfAccess) {
            default:
            case 0:
                // wilderness
                output += "&2-";
                break;
            case 1:
                // for sale
                output += "&6$";
                break;
            case 2:
                // your plots
                output += "&aT";
                break;
            case 3:
                // owned plots
                output += "&eT";
                break;
            case 4:
                // free plots
                output += "&6T";
                break;
            case 5:
                // other towns
                output += "&8T";
                break;
        }
        return output + "&7]";
    }
    
    public String getMoveMessage() {
        String output = "";
        if (this.getType() == KingdomsChunk.ChunkType.WILDERNESS) {
            output = KingdomsMessageHelper.MOVEMENT_WILDERNESS;
        } else {
            if (this.isForSale()) {
                output = String.format(KingdomsMessageHelper.MOVEMENT_TOWN_UNCLAIMED_SALE, this.getPrice(), this.getTown().getName());
            } else if (this.getOwner() != null) {
                output = String.format(KingdomsMessageHelper.MOVEMENT_TOWN_CLAIMED, this.getOwner(), this.getTown().getName());
            } else {
                output = String.format(KingdomsMessageHelper.MOVEMENT_TOWN_UNCLAIMED, this.getTown().getName());
            }
        }
        if (canPvp()) {
            output += " &6[PvP]";
        }
        return output;
    }
    
    public enum ChunkType {
        TOWN, WILDERNESS;
    }
    
    public enum ChunkPermissionGroupTypes {
        PUBLIC, FRIEND, TOWN, KINGDOM, TOWN_STAFF, KINGDOM_STAFF;
        
        public static ChunkPermissionGroupTypes getFromId(int id) {
            if (id == 0) {
                return PUBLIC;
            } else if (id == 1) {
                return FRIEND;
            } else if (id == 2) {
                return TOWN;
            } else if (id == 3) {
                return KINGDOM;
            } else if (id == 4) {
                return TOWN_STAFF;
            } else if (id == 5) {
                return KINGDOM_STAFF;
            } else {
                return PUBLIC;
            }
        }
        
        public static ChunkPermissionGroupTypes getFromString(String name) {
            if (ChunkPermissionGroupTypes.PUBLIC.getName().equalsIgnoreCase(name) || name.equalsIgnoreCase("P")) {
                return ChunkPermissionGroupTypes.PUBLIC;
            } else if (ChunkPermissionGroupTypes.FRIEND.getName().equalsIgnoreCase(name) || name.equalsIgnoreCase("F")) {
                return ChunkPermissionGroupTypes.FRIEND;
            } else if (ChunkPermissionGroupTypes.TOWN.getName().equalsIgnoreCase(name) || name.equalsIgnoreCase("T")) {
                return ChunkPermissionGroupTypes.TOWN;
            } else if (ChunkPermissionGroupTypes.KINGDOM.getName().equalsIgnoreCase(name) || name.equalsIgnoreCase("K")) {
                return ChunkPermissionGroupTypes.KINGDOM;
            } else if (ChunkPermissionGroupTypes.TOWN_STAFF.getName().equalsIgnoreCase(name) || name.equalsIgnoreCase("TS")) {
                return ChunkPermissionGroupTypes.TOWN_STAFF;
            } else if (ChunkPermissionGroupTypes.KINGDOM_STAFF.getName().equalsIgnoreCase(name) || name.equalsIgnoreCase("KS")) {
                return ChunkPermissionGroupTypes.KINGDOM_STAFF;
            } else {
                return null;
            }
        }
        
        public String getName() {
            if (this == ChunkPermissionGroupTypes.PUBLIC) {
                return "P";
            } else if (this == ChunkPermissionGroupTypes.FRIEND) {
                return "F";
            } else if (this == ChunkPermissionGroupTypes.TOWN) {
                return "T";
            } else if (this == ChunkPermissionGroupTypes.KINGDOM) {
                return "K";
            } else if (this == ChunkPermissionGroupTypes.TOWN_STAFF) {
                return "TS";
            } else if (this == ChunkPermissionGroupTypes.KINGDOM_STAFF) {
                return "KS";
            } else {
                return "P";
            }
        }
    }
    
    public void save() {
        if (this.updated) {
            try {
                super.save();
                String sql = "UPDATE " + KingdomsMain.getChunkTableName() + " SET town_id = ?, for_sale = ?, price = ?, can_mobs_spawn = ?, can_pvp = ? WHERE id = ?;";
                DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, town.getId(), (forSale ? 1 : 0), price, (canMobsSpawn ? 1 : 0), (canPvp ? 1 : 0), kingdomsId);
                this.updated = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void remove() {
        super.remove();
        String sql = "DELETE FROM  " + KingdomsMain.getChunkTableName() + " WHERE id = ?;";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, kingdomsId);
        KingdomsManager.removeKingdomsChunk(this);
    }
}
