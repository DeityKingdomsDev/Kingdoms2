package com.imdeity.kingdoms.obj;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.exception.NegativeMoneyException;
import com.imdeity.deityapi.exception.NotRegisteredException;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsChunk.ChunkPermissionGroupTypes;
import com.imdeity.protect.enums.DeityChunkPermissionTypes;

public class Resident {
    
    private int id;
    private String name;
    private Town town;
    private boolean isKing;
    private boolean isMayor;
    private boolean isAssistant;
    private boolean isHelper;
    private boolean isMale;
    private int deed;
    private List<String> friends = new ArrayList<String>();
    private Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permissions;
    
    private boolean hasUpdated;
    char[] outputColor = { '6', 'e' };
    
    public Resident(int id, String name, Town town, boolean isKing, boolean isMayor, boolean isAssistant, boolean isHelper, boolean isMale, int deed, List<String> friends, Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permissions) {
        this.setAllFields(id, name, town, isKing, isMayor, isAssistant, isHelper, isMale, deed, friends, permissions);
    }
    
    public void setAllFields(int id, String name, Town town, boolean isKing, boolean isMayor, boolean isAssistant, boolean isHelper, boolean isMale, int deed, List<String> friends, Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permissions) {
        this.id = id;
        this.name = name;
        this.town = town;
        this.isKing = isKing;
        this.isMayor = isMayor;
        this.isAssistant = isAssistant;
        this.isHelper = isHelper;
        this.isMale = isMale;
        this.deed = deed;
        this.permissions = permissions;
        if (friends != null) {
            this.friends = friends;
        }
        hasUpdated = false;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean hasTown() {
        return (town != null);
    }
    
    public Town getTown() {
        return town;
    }
    
    public void setTown(Town town) {
        this.town = town;
        this.hasUpdated();
    }
    
    public boolean isKing() {
        return isKing;
    }
    
    public boolean isMayor() {
        return isMayor;
    }
    
    public boolean isAssistant() {
        return isAssistant;
    }
    
    public boolean isHelper() {
        return isHelper;
    }
    
    public boolean isMale() {
        return isMale;
    }
    
    public boolean hasDeed() {
        return deed != -1;
    }
    
    public int getDeed() {
        return deed;
    }
    
    public void setDeed(int deed) {
        this.deed = deed;
        this.hasUpdated();
    }
    
    public void setKing(boolean isKing) {
        this.isKing = isKing;
        this.hasUpdated();
    }
    
    public void setMayor(boolean isMayor) {
        this.isMayor = isMayor;
        this.hasUpdated();
    }
    
    public void setAssistant(boolean isAssistant) {
        this.isAssistant = isAssistant;
        this.hasUpdated();
    }
    
    public void setHelper(boolean isHelper) {
        this.isHelper = isHelper;
        this.hasUpdated();
    }
    
    public void setMale(boolean isMale) {
        this.isMale = isMale;
        this.hasUpdated();
    }
    
    public String getKingdomFriendlyTitle() {
        if (isMale()) {
            if (isKing()) {
                return "King";
            } else if (isMayor()) {
                return "Council Member";
            } else {
                return "";
            }
        } else {
            if (isKing()) {
                return "Queen";
            } else if (isMayor()) {
                return "Council Member";
            } else {
                return "";
            }
        }
    }
    
    public String getTownFriendlyTitle() {
        if (isMale()) {
            if (isMayor()) {
                return "Mayor";
            } else if (isAssistant()) {
                return "Assistant";
            } else if (isHelper()) {
                return "Helper";
            } else {
                return "";
            }
        } else {
            if (isMayor()) {
                return "Mayor";
            } else if (isAssistant()) {
                return "Assistant";
            } else if (isHelper()) {
                return "Helper";
            } else {
                return "";
            }
        }
    }
    
    public boolean isOnline() {
        return (DeityAPI.getAPI().getServerAPI().getOnlinePlayer(getName()) != null && Deity.server.getOnlinePlayer(getName()).isOnline());
    }
    
    public List<String> getFriends() {
        return friends;
    }
    
    public List<Resident> getResidentFriends() {
        List<Resident> tmp = new ArrayList<Resident>();
        for (String s : friends) {
            tmp.add(KingdomsManager.getResident(s));
        }
        return tmp;
    }
    
    public List<String> getOnlineFriends() {
        List<String> tmp = new ArrayList<String>();
        for (Resident r : getResidentFriends()) {
            if (r.isOnline()) {
                tmp.add(r.getName());
            }
        }
        return tmp;
    }
    
    public boolean hasFriend(String name) {
        for (String s : friends) {
            if (s.equalsIgnoreCase(name)) { return true; }
        }
        return false;
    }
    
    public void addFriend(Resident friend) {
        String sql = "SELECT id FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings") + " WHERE resident_id = ? AND friend_id = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, this.getId(), friend.getId());
        if (query == null || !query.hasRows()) {
            DeityAPI.getAPI().getDataAPI().getMySQL().write("INSERT INTO " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings") + " (resident_id, friend_id) VALUES (?,?)", this.getId(), friend.getId());
            this.friends.add(friend.getName());
        }
    }
    
    public void removeFriend(Resident friend) {
        if (this.friends.contains(friend.getName())) {
            String sql = "SELECT id FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings") + " WHERE resident_id = ? AND friend_id = ?;";
            DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, this.getId(), friend.getId());
            if (query != null && query.hasRows()) {
                int relationshipId = 0;
                try {
                    relationshipId = query.getInteger(0, "id");
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
                if (relationshipId != 0) {
                    DeityAPI.getAPI().getDataAPI().getMySQL().write("DELETE FROM " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings") + " WHERE id = ?;", relationshipId);
                    this.friends.remove(friend.getName());
                }
            }
        }
    }
    
    public int getPlotCount() {
        return KingdomsManager.getNumberOfPlots(this.getName());
    }
    
    public void hasUpdated() {
        this.hasUpdated = true;
    }
    
    public void save() {
        if (hasUpdated) {
            DeityAPI.getAPI()
                    .getDataAPI()
                    .getMySQL()
                    .write("UPDATE " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "residents") + " SET name = ?, town_id = ?, is_king = ?, is_mayor = ?, is_assistant = ?, is_helper = ?, is_male = ?, deed = ? WHERE id = ?;", name, (town != null ? town.getId() : -1),
                            (isKing() ? 1 : 0), (isMayor() ? 1 : 0), (isAssistant() ? 1 : 0), (isHelper() ? 1 : 0), (isMale() ? 1 : 0), deed, id);
            hasUpdated = false;
        }
    }
    
    public Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> getPermissions() {
        return permissions;
    }
    
    public ChunkPermissionGroupTypes getPermission(DeityChunkPermissionTypes type) {
        return permissions.get(type);
    }
    
    public void setPermissions(DeityChunkPermissionTypes type, ChunkPermissionGroupTypes group) {
        this.permissions.put(type, group);
        this.hasUpdated();
    }
    
    public boolean canPay(double cost) {
        return DeityAPI.getAPI().getEconAPI().canPay(getName(), cost);
    }
    
    public void pay(String receiver, double cost, String reason) {
        try {
            DeityAPI.getAPI().getEconAPI().sendTo(getName(), receiver, cost, reason);
        } catch (NegativeMoneyException e) {
        }
    }
    
    public void pay(double cost, String reason) {
        try {
            DeityAPI.getAPI().getEconAPI().send(getName(), cost, reason);
        } catch (NegativeMoneyException e) {
        }
    }
    
    public void sendMessage(String message) {
        if (this.isOnline()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(this.getPlayer(), message);
        }
    }
    
    public Player getPlayer() {
        return KingdomsMain.plugin.getServer().getOnlinePlayer(getName());
    }
    
    public List<String> showInfo(boolean onlineFriends) {
        List<String> out = new ArrayList<String>();
        DeityProfile player = null;
        Date firstOnline = new Date();
        Date lastOnline = new Date();
        long totalTimeOnline = 0;
        try {
            player = Deity.server.getDeityPlayer(this.getName());
            firstOnline = player.getFirstOnline();
            lastOnline = player.getLastOnline();
            totalTimeOnline = player.getTotalTime();
        } catch (NotRegisteredException e) {
        }
        out.add("&" + outputColor[0] + "+-----------------------------+");
        out.add("&" + outputColor[0] + "Resident: &" + outputColor[1] + this.getName());
        out.add("&" + outputColor[0] + "First Online: &" + outputColor[1] + DeityAPI.getAPI().getUtilAPI().getTimeUtils().getFriendlyDate(firstOnline, false));
        if (this.isOnline()) {
            out.add("&" + outputColor[0] + "Last Online: &" + outputColor[1] + "Online Now!");
        } else {
            out.add("&" + outputColor[0] + "Last Online: &" + outputColor[1] + DeityAPI.getAPI().getUtilAPI().getTimeUtils().timeApproxToDate(lastOnline));
        }
        out.add("&" + outputColor[0] + "Total Time Online: &" + outputColor[1] + DeityAPI.getAPI().getUtilAPI().getTimeUtils().timeConvert(totalTimeOnline));
        out.add("&" + outputColor[0] + "Balance: &" + outputColor[1] + DeityAPI.getAPI().getEconAPI().getBalance(name));
        if (this.hasTown()) {
            if (this.getTown().getKingdom() != null) {
                out.add("&" + outputColor[0] + "Kingdom: &" + outputColor[1] + this.getTown().getKingdom().getName() + (isKing() || isMayor() ? " &8[&7" + this.getKingdomFriendlyTitle() + "&8]" : ""));
            }
            out.add("&" + outputColor[0] + "Town: &" + outputColor[1] + this.getTown().getName() + (isMayor() || isAssistant() ? " &8[&7" + this.getTownFriendlyTitle() + "&8]" : ""));
            out.add("&" + outputColor[0] + "Plot Count: &" + outputColor[1] + this.getPlotCount());
        }
        out.add("&" + outputColor[0] + "Permissions:" + "&" + outputColor[1] + " Edit&8: &7" + this.getPermission(DeityChunkPermissionTypes.EDIT).getName() + "&" + outputColor[1] + " Use&8: &7" + this.getPermission(DeityChunkPermissionTypes.USE).getName() + "&" + outputColor[1] + " Access&8: &7"
                + this.getPermission(DeityChunkPermissionTypes.ACCESS).getName());
        if (this.getFriends() != null && !this.getFriends().isEmpty()) {
            String list = "";
            if (!onlineFriends) {
                list = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(this.getFriends(), ", ");
                out.add("&" + outputColor[0] + "Friends &" + outputColor[1] + "[" + this.friends.size() + "] &f: " + list);
            } else {
                list = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(this.getOnlineFriends(), ", ");
                if (!list.isEmpty()) {
                    out.add("&" + outputColor[0] + "Online Friends &" + outputColor[1] + "[" + this.friends.size() + "] &f: " + list);
                } else {
                    out.add("&" + outputColor[0] + "Friends &" + outputColor[1] + "[" + this.friends.size() + "] &f: &fI have no online friends");
                }
                out.add("&8Use '/resident info [name] -o' to view the full friend list");
            }
        } else {
            out.add("&" + outputColor[0] + "Friends: &" + outputColor[1] + "I have no friends");
        }
        
        return out;
    }
    
    public boolean isLeastLevelOneNoble() {
        // try {
        // return
        // (Deity.server.getDeityPlayer(this.getName()).getClassName().equalsIgnoreCase("political")
        // && (Deity.server.getDeityPlayer(this.getName()).getLevel() >= 1));
        // } catch (NotRegisteredException e) {
        // e.printStackTrace();
        // }
        return true;
    }
}
