package com.imdeity.kingdoms.obj;

import org.bukkit.Location;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.kingdoms.main.KingdomsMain;

public class TownWarp {
    
    private int id;
    private String warpName;
    private int townId;
    private Location location;
    private int cost;
    
    public TownWarp(int id, String warpName, int townId, Location location, int cost) {
        this.setAllFields(id, warpName, townId, location, cost);
    }
    
    private void setAllFields(int id, String warpName, int townId, Location location, int cost) {
        this.id = id;
        this.warpName = warpName;
        this.townId = townId;
        this.location = location;
        this.cost = cost;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return warpName;
    }
    
    public int getTownId() {
        return townId;
    }
    
    public Town getTown() {
        return KingdomsManager.getTown(townId);
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public int getCost() {
        return cost;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    public void save() {
        String sql = "UPDATE " + KingdomsMain.getWarpTableName() + " SET world = ?, x_coord = ?, y_coord = ?, z_coord = ?, pitch = ?, yaw = ? WHERE id = ?";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.getLocation().getWorld().getName(), this.getLocation().getBlockX(), this.getLocation().getBlockY(), this.getLocation().getBlockZ(), this.getLocation().getPitch(), this.getLocation().getYaw(), this.getId());
    }
    
    public void remove() {
        String sql = "DELETE FROM " + KingdomsMain.getWarpTableName() + " WHERE id = ?";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.getId());
    }
}
