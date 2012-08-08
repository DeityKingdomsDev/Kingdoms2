package com.imdeity.kingdoms.listener;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityListener;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsChunk;
import com.imdeity.kingdoms.obj.KingdomsHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomsPlayerListener extends DeityListener {
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (KingdomsManager.getResident(player.getName()) == null) {
                KingdomsManager.addNewResident(player.getName());
            }
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident != null) {
                resident.setLoginTime();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident != null) {
            resident.setTotalTimeOnline();
            resident.setLastOnline();
            resident.save();
        }
        if (player != null) {
            KingdomsManager.removeResident(player.getName());
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            Resident resident = KingdomsManager.getResident(player.getName());
            if (resident != null && resident.hasTown()) {
                event.setRespawnLocation(resident.getTown().getSpawnLocation());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (DeityAPI.getAPI().getUtilAPI().fixLocation(event.getFrom()).distance(DeityAPI.getAPI().getUtilAPI().fixLocation(event.getTo())) == 0.0) { return; }
        Player p = event.getPlayer();
        World world = event.getFrom().getWorld();
        Chunk chunkFrom = world.getChunkAt(event.getFrom());
        Chunk chunkTo = world.getChunkAt(event.getTo());
        if (!chunkFrom.equals(chunkTo)) {
            KingdomsChunk kChunkFrom = KingdomsManager.getKingdomsChunk(chunkFrom.getWorld(), chunkFrom.getX(), chunkFrom.getZ(), false);
            KingdomsChunk kChunkTo = KingdomsManager.getKingdomsChunk(chunkTo.getWorld(), chunkTo.getX(), chunkTo.getZ(), false);
            
            if (KingdomsMain.plugin.residentsInMapMode.contains(p.getName())) {
                KingdomsHelper.sendMap(p, event.getTo());
                return;
            }
            
            if (kChunkFrom.getType() == kChunkTo.getType()) {
                if (kChunkTo.getType() == KingdomsChunk.ChunkType.TOWN) {
                    if (kChunkTo.canPvp() == kChunkFrom.canPvp() && kChunkFrom.getTown()!= null && kChunkTo.getTown() != null &&kChunkFrom.getTown().getName().equalsIgnoreCase(kChunkTo.getTown().getName()) && kChunkFrom.getOwner() != null && kChunkTo.getOwner() != null && kChunkFrom.getOwner().equalsIgnoreCase(kChunkTo.getOwner())) { return; }
                    if (kChunkTo.canPvp() == kChunkFrom.canPvp() && kChunkFrom.getTown()!= null && kChunkTo.getTown() != null &&kChunkFrom.getTown().getName().equalsIgnoreCase(kChunkTo.getTown().getName()) && kChunkFrom.getOwner() == null && kChunkTo.getOwner() == null) { return; }
                } else {
                    return;
                }
            }
            
            if (!KingdomsMain.plugin.residentsInMapMode.contains(p.getName())) {
                KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(p, kChunkTo.getMoveMessage());
                return;
            }
        }
    }
}