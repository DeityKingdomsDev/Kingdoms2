package com.imdeity.kingdoms.cmds.town;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ca.xshade.bukkit.questioner.Questioner;
import ca.xshade.questionmanager.Option;
import ca.xshade.questionmanager.Question;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;
import com.imdeity.kingdoms.tasks.ConfirmQuestionTask;

public class TownAddCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (!resident.isMayor() && !resident.isSeniorAssistant()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_TOWN_STAFF);
            return true;
        }
        
        Town town = resident.getTown();
        if (args.length == 1) {
            Resident newResident = KingdomsManager.getResident(args[0]);
            if (newResident == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player,
                        String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_RESIDENT, args[0]));
                return true;
            }
            if (newResident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_ALREADY_IN_TOWN);
                return true;
            }
            sendConfirmation(player, town, newResident);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length == 2) {
            Town town = KingdomsManager.getTown(args[0]);
            Resident newResident = KingdomsManager.getResident(args[1]);
            if (newResident == null) {
                KingdomsMain.plugin.chat.out(String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_RESIDENT, args[0]));
                return true;
            }
            if (newResident.hasTown()) {
                KingdomsMain.plugin.chat.out(KingdomsMessageHelper.CMD_FAIL_ALREADY_IN_TOWN);
                return true;
            }
            town.addResident(newResident);
            KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_ADD_GLOBAL, newResident.getName(),
                    town.getName()));
            return true;
        }
        return false;
    }
    
    private void sendConfirmation(Player player, Town town, Resident newResident) {
        String phrase = player.getName() + " has invited you to join " + town.getName();
        
        Plugin tmp = KingdomsMain.plugin.getServer().getPluginManager().getPlugin("Questioner");
        if (tmp != null && tmp instanceof Questioner && tmp.isEnabled()) {
            Questioner questioner = (Questioner) tmp;
            questioner.loadClasses();
            
            List<Option> options = new ArrayList<Option>();
            options.add(new Option("accept", new ConfirmQuestionTask(player, town, newResident) {
                @Override
                public void run() {
                    town.addResident(questionee);
                    KingdomsMain.plugin.chat.sendGlobalMessage(String.format(KingdomsMessageHelper.CMD_TOWN_ADD_GLOBAL,
                            questionee.getName(), town.getName()));
                }
            }));
            
            options.add(new Option("deny", new ConfirmQuestionTask(player, town, newResident) {
                @Override
                public void run() {
                    questionee.sendMessage("You declined " + questioner.getName() + "'s request to join " + town.getName());
                    KingdomsMain.plugin.chat.sendPlayerMessage(questioner, questionee.getName() + " declined your request");
                }
            }));
            Question question = new Question(newResident.getName(), phrase, options);
            try {
                KingdomsMain.appendQuestion(questioner, question);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
