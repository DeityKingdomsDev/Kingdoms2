package com.imdeity.kingdoms.cmds.kingdom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Request;
import com.imdeity.kingdoms.obj.Request.RequestType;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomRequestCommand extends DeityCommandReceiver {
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length == 0) { return false; }
        
        if (args[0].equalsIgnoreCase("list")) {
            if (resident.getTown() == null || resident.getTown().getKingdom() == null || !resident.isKing()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_KINGDOM_REQUEST_NOT_KING);
                return true;
            }
            Kingdom kingdom = resident.getTown().getKingdom();
            List<String> output = new ArrayList<String>();
            for (Request r : kingdom.getRequests()) {
                output.add(r.showInfo());
            }
            for (String s : output) {
                KingdomsMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("accept")) {
            if (resident.getTown() == null || resident.getTown().getKingdom() == null || !resident.isKing()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_KINGDOM_REQUEST_NOT_KING);
                return true;
            }
            Kingdom kingdom = resident.getTown().getKingdom();
            if (args.length <= 1) { return false; }
            int requestId = Integer.parseInt(args[1]);
            Request request = kingdom.getRequest(requestId);
            if (request == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "That request was invalid");
                return true;
            }
            request.setApproved(true);
            request.setClosed(true);
            request.save();
            if (request.getType() == RequestType.KINGDOM_JOIN) {
                Resident requesteeResident = KingdomsManager.getResident(request.getRequestee());
                if (requesteeResident != null && requesteeResident.getTown() != null) {
                    kingdom.addTown(requesteeResident.getTown(), false);
                }
            } else if (request.getType() == RequestType.KINGDOM_TOWN_CREATE) {
                Resident requesteeResident = KingdomsManager.getResident(request.getRequestee());
                if (requesteeResident != null && requesteeResident.getTown() == null) {
                    requesteeResident.setDeed(kingdom.getId());
                    requesteeResident.save();
                }
            }
            KingdomsMain.plugin.chat.sendPlayerMessage(player,
                    String.format(KingdomsMessageHelper.CMD_REQUEST_ACCEPT_CONFIRM, request.getRequestee()));
            try {
                DeityAPI.getAPI()
                        .getChatAPI()
                        .sendMailToPlayer("KingdomsRequest", request.getRequestee(),
                                String.format(KingdomsMessageHelper.CMD_REQUEST_ACCEPT_MAIL, kingdom.getName()));
            } catch (Exception e) {
            }
            return true;
        } else if (args[0].equalsIgnoreCase("deny")) {
            if (resident.getTown() == null || resident.getTown().getKingdom() == null || !resident.isKing()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_KINGDOM_REQUEST_NOT_KING);
                return true;
            }
            Kingdom kingdom = resident.getTown().getKingdom();
            if (args.length <= 1) { return false; }
            int requestId = Integer.parseInt(args[1]);
            Request request = kingdom.getRequest(requestId);
            request.setApproved(false);
            request.setClosed(true);
            request.save();
            KingdomsMain.plugin.chat.sendPlayerMessage(player,
                    String.format(KingdomsMessageHelper.CMD_REQUEST_DENIED_CONFIRM, request.getRequestee()));
            try {
                DeityAPI.getAPI()
                        .getChatAPI()
                        .sendMailToPlayer("KingdomsRequest", request.getRequestee(),
                                String.format(KingdomsMessageHelper.CMD_REQUEST_DENIED_MAIL, kingdom.getName()));
            } catch (Exception e) {
            }
            return true;
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length <= 1) { return false; }
            String kingdomName = args[1];
            Kingdom kingdom = KingdomsManager.getKingdom(kingdomName);
            if (kingdom == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player,
                        String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_KINGDOM, kingdomName));
                return true;
            }
            if (resident.hasTown()) {
                if (resident.getTown().getKingdom() != null) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, "Your town already has a kingdom");
                    return true;
                }
                Request request = KingdomsManager.addNewRequest(resident.getName(), RequestType.KINGDOM_JOIN, kingdom.getId());
                kingdom.addRequest(request);
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_REQUEST_CONFIRM);
            } else {
                for (Request r : kingdom.getRequests()) {
                    if (r.getRequestee().equals(player.getName())) {
                        KingdomsMain.plugin.chat.sendPlayerMessage(player, "You already have a request open");
                        return true;
                    }
                }
                Request request = KingdomsManager.addNewRequest(resident.getName(), RequestType.KINGDOM_TOWN_CREATE, kingdom.getId());
                kingdom.addRequest(request);
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_REQUEST_CONFIRM);
            }
            return true;
        }
        return false;
    }
    
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
