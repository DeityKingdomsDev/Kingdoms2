package com.imdeity.kingdoms.obj;

import org.bukkit.Location;
import org.bukkit.World;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.kingdoms.main.KingdomsMain;

public class TownSpawnLocation {
    
    private int id;
    private Location location;
    
    public TownSpawnLocation(int id, Location location) {
        this.setAllFields(id, location);
    }
    
    public TownSpawnLocation(int id, World world, int xCoord, int yCoord, int zCoord, int pitch, int yaw) {
        this.setAllFields(id, new Location(world, xCoord, yCoord, zCoord, pitch, yaw));
    }
    
    private void setAllFields(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    
    public int getId() {
        return id;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public void save() {
        String sql = "UPDATE " + KingdomsMain.getTownSpawnLocationTableName() + " SET world = ?, x_coord = ?, y_coord = ?, z_coord = ?, pitch = ?, yaw = ? WHERE id = ?";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.getLocation().getWorld().getName(), this.getLocation().getBlockX(), this.getLocation().getBlockY(), this.getLocation().getBlockZ(), this.getLocation().getPitch(), this.getLocation().getYaw(), this.getId());
    }
    
    public void remove() {
        String sql = "DELETE FROM " + KingdomsMain.getTownSpawnLocationTableName() + " WHERE id = ?";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.getId());
    }
}
