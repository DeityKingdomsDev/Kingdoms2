package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.resident.ResidentAddFriendCommand;
import com.imdeity.kingdoms.cmds.resident.ResidentInfoCommand;
import com.imdeity.kingdoms.cmds.resident.ResidentRemoveFriendCommand;
import com.imdeity.kingdoms.cmds.resident.ResidentSetCommand;

public class ResidentCommand extends DeityCommandHandler {
    
    public ResidentCommand(String pluginName) {
        super(pluginName, "Resident");
    }
    
    @Override
    public void initRegisteredCommands() {
        String[] setArgs = { "gender [male/female]", "permissions edit [P/F/T/K/TS/KS]", "permissions use [P/F/T/K/TS/KS]", "permissions access [P/F/T/K/TS/KS]" };
        this.registerCommand("info", "<player-name> <-o>", "Shows a residents status", new ResidentInfoCommand(), "kingdoms.resident.info");
        this.registerCommand("add-friend", "[player-name]", "Attempts to add a friend", new ResidentAddFriendCommand(), "kingdoms.resident.add-friend");
        this.registerCommand("remove-friend", "[player-name]", "Attempts to remove a friend", new ResidentRemoveFriendCommand(), "kingdoms.resident.remove-friend");
        this.registerCommand("set", setArgs, "Changes options about your character", new ResidentSetCommand(), "kingdoms.resident.set");
    }
}
