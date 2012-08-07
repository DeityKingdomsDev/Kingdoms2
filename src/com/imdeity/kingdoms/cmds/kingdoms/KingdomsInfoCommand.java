package com.imdeity.kingdoms.cmds.kingdoms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomsInfoCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        for (String s : getOutput()) {
            KingdomsMain.plugin.chat.out(s);
        }
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        for (String s : getOutput()) {
            resident.sendMessageNoHeader(s);
        }
        return true;
    }
    
    private List<String> getOutput() {
        ArrayList<String> out = new ArrayList<String>();
        out.add("&3-----[&bKingdoms-2 Information&3]-----");
        out.add("&3#&0-&b###&0-");
        out.add("&3&0--&b#&0--&b#" + "    &3There " + (KingdomsManager.getNumTowns() == 1 ? "is" : "are") + " &b"
                + KingdomsManager.getNumTowns() + " &3Town" + (KingdomsManager.getNumTowns() == 1 ? "" : "s") + " with &b"
                + KingdomsManager.getNumResidents() + " &3Resident" + (KingdomsManager.getNumResidents() == 1 ? "" : "s"));
        out.add("&3#&0-&b#&0--&b#" + "    &3Largest town: &b" + KingdomsManager.getLargestTown().getName() + " &3with &b"
                + KingdomsManager.getLargestTown().getResidentsNames().size() + " &3resident"
                + (KingdomsManager.getLargestTown().getResidentsNames().size() == 1 ? "" : "s"));
        out.add("&3#&0-&b#&0--&b#" + "    &3Plugin Developed by: &bvanZeben &7[&3v" + KingdomsMain.plugin.getDescription().getVersion()
                + "&7]");
        out.add("&3#&0-&b###&0-");
        return out;
    }
    
}
