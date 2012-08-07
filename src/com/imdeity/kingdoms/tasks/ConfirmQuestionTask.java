package com.imdeity.kingdoms.tasks;

import org.bukkit.entity.Player;

import ca.xshade.bukkit.questioner.BukkitQuestionTask;

import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class ConfirmQuestionTask extends BukkitQuestionTask {
    
    protected Player questioner;
    protected Resident questionee;
    protected Town town;
    
    public ConfirmQuestionTask(Player questioner, Town town, Resident questionee) {
        this.questioner = questioner;
        this.questionee = questionee;
        this.town = town;
    }
    
    @Override
    public void run() {
    }
    
}
