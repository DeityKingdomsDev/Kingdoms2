package com.imdeity.kingdoms.obj;

import java.util.Date;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;

public class Request {
    private int id;
    private String requestee;
    private RequestType type;
    private int requestTypeId;
    private boolean isClosed;
    private boolean isApproved;
    private Date date;
    private boolean hasUpdated = false;
    
    public Request(int id, String requestee, RequestType type, int requestTypeId, boolean isClosed, boolean isApproved, Date date) {
        this.id = id;
        this.requestee = requestee;
        this.type = type;
        this.requestTypeId = requestTypeId;
        this.isClosed = isClosed;
        this.isApproved = isApproved;
        this.date = date;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getRequestee() {
        return this.requestee;
    }
    
    public RequestType getType() {
        return this.type;
    }
    
    public int getRequestTypeId() {
        return this.requestTypeId;
    }
    
    public boolean isClosed() {
        return this.isClosed;
    }
    
    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
        this.hasUpdated = true;
    }
    
    public boolean isApproved() {
        return isApproved;
    }
    
    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
        this.hasUpdated = true;
    }
    
    public Date getDate() {
        return date;
    }
    
    public enum RequestType {
        KINGDOM_JOIN, KINGDOM_TOWN_CREATE;
        
        public static RequestType getFromString(String type) {
            for (RequestType rt : RequestType.values()) {
                if (rt.name().equalsIgnoreCase(type)) { return rt; }
            }
            return null;
        }
    }
    
    public String showInfo() {
        String output = this.isApproved() ? "&a" : "";
        if (isClosed()) {
            output += "&m";
        }
        switch (type) {
            case KINGDOM_JOIN:
                output += String.format(KingdomsMessageHelper.CMD_REQUEST_KINGDOM_JOIN_OUTPUT, id, requestee, DeityAPI.getAPI().getUtilAPI().getTimeUtils().printTime(date));
                break;
            case KINGDOM_TOWN_CREATE:
                output += String.format(KingdomsMessageHelper.CMD_REQUEST_KINGDOM_CREATE_JOIN_OUTPUT, id, requestee, DeityAPI.getAPI().getUtilAPI().getTimeUtils().printTime(date));
                break;
            default:
                return null;
        }
        return output;
    }
    
    public void save() {
        if (hasUpdated) {
            DeityAPI.getAPI().getDataAPI().getMySQL().write("UPDATE " + KingdomsMain.getRequestTableName() + " SET player_name = ?, type = ?, requested_id = ?, is_approved = ?, is_closed = ? WHERE id = ?;", requestee, type.name(), requestTypeId, (isApproved ? 1 : 0), (isClosed ? 1 : 0), id);
            hasUpdated = false;
        }
    }
}
