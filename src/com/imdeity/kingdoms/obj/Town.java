package com.imdeity.kingdoms.obj;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.exception.NegativeMoneyException;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsChunk.ChunkPermissionGroupTypes;
import com.imdeity.protect.enums.DeityChunkPermissionTypes;

public class Town {
    
    private static final String ECON_PREFIX = "town_";
    private int id;
    private String name;
    private Kingdom kingdom;
    private String townBoard;
    private int defaultPlotPrice;
    private TownSpawnLocation spawnLocation;
    private boolean isPublic;
    private boolean isCapital;
    private Date creationDate;
    private int numBonusPlots;
    private List<String> residents = new ArrayList<String>();
    private List<Integer> land = new ArrayList<Integer>();
    private Map<String, Integer> warps = new HashMap<String, Integer>();
    private Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permissions = new HashMap<DeityChunkPermissionTypes, ChunkPermissionGroupTypes>();
    
    private boolean hasUpdated = false;
    private char[] outputColor = { '6', 'e' };
    
    public Town(int id, String name, Kingdom kingdom, String townBoard, int defaultPlotPrice, TownSpawnLocation spawnLocation,
            boolean isPublic, boolean isCapital, Date creationDate,
            Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permissions, int numBonusPlots) {
        this.setAllFields(id, name, kingdom, townBoard, defaultPlotPrice, spawnLocation, isPublic, isCapital, creationDate,
                permissions, numBonusPlots);
    }
    
    public void setAllFields(int id, String name, Kingdom kingdom, String townBoard, int defaultPlotPrice,
            TownSpawnLocation spawnLocation, boolean isPublic, boolean isCapital, Date creationDate,
            Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> permissions, int numBonusPlots) {
        this.id = id;
        this.name = name;
        this.kingdom = kingdom;
        this.townBoard = townBoard;
        this.defaultPlotPrice = defaultPlotPrice;
        this.spawnLocation = spawnLocation;
        this.isPublic = isPublic;
        this.isCapital = isCapital;
        this.creationDate = creationDate;
        this.permissions = permissions;
        this.numBonusPlots = numBonusPlots;
        this.hasUpdated = false;
        initLand();
        initResidents();
        initWarps();
    }
    
    public void initLand() {
        KingdomsManager.loadAllChunks(this);
    }
    
    public void initResidents() {
        KingdomsManager.loadAllResident(this);
    }
    
    public void initWarps() {
        KingdomsManager.loadAllWarps(this);
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Kingdom getKingdom() {
        return kingdom;
    }
    
    public String getTownBoard() {
        return townBoard;
    }
    
    public TownSpawnLocation getTownSpawnLocation() {
        return spawnLocation;
    }
    
    public Location getSpawnLocation() {
        return spawnLocation.getLocation();
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public boolean isCapital() {
        return isCapital;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public Resident getMayor() {
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            if (r.isMayor()) { return r; }
        }
        return null;
    }
    
    public List<Resident> getSeniorAssistants() {
        List<Resident> tmp = new ArrayList<Resident>();
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            if (r.isSeniorAssistant()) {
                tmp.add(r);
            }
        }
        return tmp;
    }
    
    public List<String> getSeniorAssistantNames() {
        List<String> tmp = new ArrayList<String>();
        for (Resident r : getSeniorAssistants()) {
            tmp.add(r.getName());
        }
        return tmp;
    }
    
    public List<Resident> getAssistants() {
        List<Resident> tmp = new ArrayList<Resident>();
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            if (r.isAssistant()) {
                tmp.add(r);
            }
        }
        return tmp;
    }
    
    public List<String> getAssistantNames() {
        List<String> tmp = new ArrayList<String>();
        for (Resident r : getAssistants()) {
            tmp.add(r.getName());
        }
        return tmp;
    }
    
    public List<String> getResidentsNames() {
        return residents;
    }
    
    public List<String> getOnlineResidents() {
        List<String> tmp = new ArrayList<String>();
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            if (r.isOnline()) {
                tmp.add(r.getName());
            }
        }
        return tmp;
    }
    
    public List<KingdomsChunk> getLand() {
        List<KingdomsChunk> tmp = new ArrayList<KingdomsChunk>();
        for (int i : land) {
            tmp.add(KingdomsManager.getKingdomsChunk(i));
        }
        return tmp;
    }
    
    public int getLandSize() {
        return land.size();
    }
    
    public int getMaxLandSize() {
        return (residents.size() * KingdomsMain.plugin.config.getInt(String.format(KingdomsConfigHelper.TOWN_PLOTS_PER_RESIDENT, this
                .getSpawnLocation().getWorld().getName())))
                + numBonusPlots;
    }
    
    public int getDefaultPlotPrice() {
        return this.defaultPlotPrice;
    }
    
    public Map<DeityChunkPermissionTypes, ChunkPermissionGroupTypes> getPermissions() {
        return permissions;
    }
    
    public ChunkPermissionGroupTypes getPermission(DeityChunkPermissionTypes type) {
        return permissions.get(type);
    }
    
    public TownWarp getWarp(String name) {
        if (warps.get(name.toLowerCase()) != null) { return KingdomsManager.getTownWarp(warps.get(name.toLowerCase())); }
        return null;
    }
    
    public List<TownWarp> getAllWarps() {
        List<TownWarp> townWarps = new ArrayList<TownWarp>();
        for (Integer i : warps.values()) {
            townWarps.add(KingdomsManager.getTownWarp(i));
        }
        return townWarps;
    }
    
    public List<String> getAllWarpNames() {
        List<String> warps = new ArrayList<String>();
        for (TownWarp warp : getAllWarps()) {
            warps.add(warp.getName());
        }
        return warps;
    }
    
    public int getNumBonusPlots() {
        return numBonusPlots;
    }
    
    public String getEconName() {
        return ECON_PREFIX + getName();
    }
    
    public double getBalance() {
        return DeityAPI.getAPI().getEconAPI().getBalance(getEconName());
    }
    
    public String getFormattedBalance() {
        return DeityAPI.getAPI().getEconAPI().getFormattedBalance(getEconName());
    }
    
    public void setDefaultPlotPrice(int price) {
        this.defaultPlotPrice = price;
        this.hasUpdated();
    }
    
    public void setResidents(List<String> residents) {
        if (residents != null) {
            this.residents = residents;
        }
    }
    
    public void setMayor(Resident resident) {
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            if (r.isMayor()) {
                r.setMayor(false);
            }
        }
        if (resident != null) {
            resident.setMayor(true);
            resident.setAssistant(false);
            resident.save();
        }
    }
    
    public void setCapital(boolean isCapital) {
        this.isCapital = isCapital;
        this.hasUpdated();
    }
    
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        this.hasUpdated();
    }
    
    public void setTownBoard(String newTownBoard) {
        this.townBoard = newTownBoard;
        this.hasUpdated();
    }
    
    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
        this.hasUpdated();
    }
    
    public void setNumBonusPlots(int numBonusPlots) {
        this.numBonusPlots = numBonusPlots;
        this.hasUpdated();
    }
    
    public void setLand(List<Integer> townLand) {
        this.land = townLand;
    }
    
    public void setWarps(Map<String, Integer> warps) {
        this.warps = warps;
    }
    
    public void setPermissions(DeityChunkPermissionTypes type, ChunkPermissionGroupTypes group) {
        this.permissions.put(type, group);
        this.hasUpdated();
    }
    
    public boolean hasStaff(String resident) {
        if (getKingdom() != null && getKingdom().getKing().getName().equalsIgnoreCase(resident)) { return true; }
        if (getMayor().getName().equalsIgnoreCase(resident)) { return true; }
        return getSeniorAssistantNames().contains(resident) || getAssistantNames().contains(resident);
    }
    
    public boolean hasResident(String resident) {
        return getResidentsNames().contains(resident);
    }
    
    public void addResident(Resident resident) {
        residents.add(resident.getName());
        resident.setTown(this);
        resident.save();
    }
    
    public void addWarp(String name, Location location, int price) {
        warps.put(name.toLowerCase(), KingdomsManager.addNewTownWarp(name, this.getId(), location, price).getId());
    }
    
    public void removeResident(Resident resident) {
        for (KingdomsChunk chunk : this.getLand()) {
            if (chunk.getOwner() != null && chunk.getOwner().equalsIgnoreCase(resident.getName())) {
                chunk.setOwner(null);
                chunk.save();
            }
        }
        residents.remove(resident.getName());
        resident.setTown(null);
        resident.save();
    }
    
    public void removeWarp(String name) {
        if (warps.containsKey(name)) {
            warps.remove(name);
        }
    }
    
    public void claim(KingdomsChunk chunk) {
        chunk.setTown(this);
        chunk.save();
        this.land.add(chunk.getId());
    }
    
    public void unclaim(KingdomsChunk chunk) {
        this.land.remove(chunk.getId());
        chunk.remove();
    }
    
    public void createBankAccount() {
        DeityAPI.getAPI().getEconAPI().createAccount(getEconName());
    }
    
    public void hasUpdated() {
        hasUpdated = true;
    }
    
    public boolean canPay(double cost) {
        return DeityAPI.getAPI().getEconAPI().canPay(getEconName(), cost);
    }
    
    public void pay(double cost, String note) {
        try {
            DeityAPI.getAPI().getEconAPI().send(getEconName(), cost, note);
        } catch (NegativeMoneyException e) {
        }
    }
    
    public void pay(String receiver, double cost, String note) {
        try {
            DeityAPI.getAPI().getEconAPI().sendTo(getEconName(), receiver, cost, note);
        } catch (NegativeMoneyException e) {
        }
    }
    
    public void sendMessage(String message) {
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            r.sendMessage(message);
        }
    }
    
    public void sendMessageNoHeader(String message) {
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            r.sendMessageNoHeader(message);
        }
    }
    
    public List<String> showInfo(boolean onlineList) {
        List<String> out = new ArrayList<String>();
        out.add("&" + outputColor[0] + "+-----------------------------+");
        out.add("&" + outputColor[0] + "Town: &" + outputColor[1] + this.getName());
        out.add("&" + outputColor[0] + "Size: &" + outputColor[1] + this.getLandSize() + "    &" + outputColor[0] + "Max Size: &"
                + outputColor[1] + this.getMaxLandSize());
        if (this.getKingdom() != null) {
            out.add("&" + outputColor[0] + "Kingdom: &" + outputColor[1] + this.getKingdom().getName());
        }
        if (this.getMayor() != null) {
            out.add("&" + outputColor[0] + this.getMayor().getTownFriendlyTitle() + ": &" + outputColor[1] + this.getMayor().getName());
        }
        if (this.getSeniorAssistantNames() != null && this.getSeniorAssistantNames().size() > 0) {
            out.add("&" + outputColor[0] + "Senior Assistants: &" + outputColor[1]
                    + DeityAPI.getAPI().getUtilAPI().getStringUtils().join(this.getSeniorAssistantNames(), ", "));
        }
        if (this.getAssistantNames() != null && this.getAssistantNames().size() > 0) {
            out.add("&" + outputColor[0] + "Assistants: &" + outputColor[1]
                    + DeityAPI.getAPI().getUtilAPI().getStringUtils().join(this.getAssistantNames(), ", "));
        }
        out.add("&"
                + outputColor[0]
                + "Creation Date: &"
                + outputColor[1]
                + (this.getCreationDate() == null ? "Right Now" : DeityAPI.getAPI().getUtilAPI().getTimeUtils()
                        .getFriendlyDate(this.getCreationDate(), false)));
        out.add("&" + outputColor[0] + "Balance: &" + outputColor[1] + this.getFormattedBalance());
        out.add("&" + outputColor[0] + "Permissions:" + "&" + outputColor[1] + " Edit&8: &7"
                + this.getPermission(DeityChunkPermissionTypes.EDIT).getName() + "&" + outputColor[1] + " Use&8: &7"
                + this.getPermission(DeityChunkPermissionTypes.USE).getName() + "&" + outputColor[1] + " Access&8: &7"
                + this.getPermission(DeityChunkPermissionTypes.ACCESS).getName());
        if (this.getResidentsNames() != null && !this.getResidentsNames().isEmpty()) {
            String list = "";
            if (!onlineList) {
                list = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(this.getResidentsNames(), ", ");
                out.add("&" + outputColor[0] + "Residents &" + outputColor[1] + "[" + this.getResidentsNames().size() + "] &f: "
                        + list);
            } else {
                list = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(this.getOnlineResidents(), "&7, " + outputColor[1]);
                if (!list.isEmpty()) {
                    out.add("&" + outputColor[0] + "Online Residents &" + outputColor[1] + "[" + this.getOnlineResidents().size()
                            + "] &f: " + list);
                } else {
                    out.add("&" + outputColor[0] + "Residents &" + outputColor[1] + "[" + this.getResidentsNames().size()
                            + "] &f: &fWe have no online residents");
                }
                out.add("&8Use '/town info [name] -o' to view the full resident list");
            }
        } else {
            out.add("&" + outputColor[0] + "Residents: &fWe have no residents");
        }
        return out;
    }
    
    public void save() {
        if (hasUpdated) {
            DeityAPI.getAPI()
                    .getDataAPI()
                    .getMySQL()
                    .write("UPDATE "
                            + KingdomsMain.getTownTableName()
                            + " SET name = ?, kingdom_id = ?, town_board = ?, default_plot_price = ?, spawn_location_id = ?, is_public = ?, is_capital = ?, creation_date = ?, num_bonus_plots = ? WHERE id = ?;",
                            name, (kingdom != null ? kingdom.getId() : -1), townBoard, defaultPlotPrice, spawnLocation.getId(),
                            (isPublic() ? 1 : 0), (isCapital() ? 1 : 0), creationDate, numBonusPlots, id);
            hasUpdated = false;
        }
    }
    
    public void remove() {
        DeityAPI.getAPI().getEconAPI().removeAccount(getEconName());
        for (KingdomsChunk chunk : this.getLand()) {
            chunk.remove();
        }
        this.land.clear();
        for (String s : residents) {
            Resident r = KingdomsManager.getResident(s);
            r.setMayor(false);
            r.setKing(false);
            r.setSeniorAssistant(false);
            r.setAssistant(false);
            r.setTown(null);
            r.save();
        }
        
        this.residents.clear();
        DeityAPI.getAPI().getDataAPI().getMySQL().write("DELETE FROM " + KingdomsMain.getTownTableName() + " WHERE id = ?;", id);
    }
}
