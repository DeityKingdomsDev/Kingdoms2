package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.town.TownAddCommand;
import com.imdeity.kingdoms.cmds.town.TownClaimCommand;
import com.imdeity.kingdoms.cmds.town.TownCreateCommand;
import com.imdeity.kingdoms.cmds.town.TownDeleteCommand;
import com.imdeity.kingdoms.cmds.town.TownDemoteCommand;
import com.imdeity.kingdoms.cmds.town.TownDepositCommand;
import com.imdeity.kingdoms.cmds.town.TownInfoCommand;
import com.imdeity.kingdoms.cmds.town.TownKickCommand;
import com.imdeity.kingdoms.cmds.town.TownLeaveCommand;
import com.imdeity.kingdoms.cmds.town.TownListCommand;
import com.imdeity.kingdoms.cmds.town.TownPromoteCommand;
import com.imdeity.kingdoms.cmds.town.TownSetCommand;
import com.imdeity.kingdoms.cmds.town.TownSpawnCommand;
import com.imdeity.kingdoms.cmds.town.TownUnclaimCommand;
import com.imdeity.kingdoms.cmds.town.TownVoteCommand;
import com.imdeity.kingdoms.cmds.town.TownWithdrawCommand;

public class TownCommand extends DeityCommandHandler {
    
    @Override
    public void initRegisteredCommands() {
        String[] setArgs = { "town-board [message]", "plot-price [MOUNR]", "spawn", "public [on/off]", "permissions edit [P/F/T/K/TS/KS]", "permissions use [P/F/T/K/TS/KS]", "permissions access [P/F/T/K/TS/KS]" };
        String[] voteArgs = { "create", "[assistant-name]", "check" };
        this.registerCommand("list", "", "Lists all towns", new TownListCommand());
        this.registerCommand("info", "<town-name> <-o>", "Displays a towns information", new TownInfoCommand());
        this.registerCommand("create", "[town-name]", "Creates a new Town", new TownCreateCommand());
        this.registerCommand("delete", "[town-name]", "Deletes a town", new TownDeleteCommand());
        this.registerCommand("claim", "", "Claims new town plots", new TownClaimCommand());
        this.registerCommand("unclaim", "", "Unclaims this town plots", new TownUnclaimCommand());
        this.registerCommand("add", "[player-name]", "Add a resident to the town", new TownAddCommand());
        this.registerCommand("kick", "[player-name]", "Kicks a resident from the town", new TownKickCommand());
        this.registerCommand("set", setArgs, "Changes a towns options", new TownSetCommand());
        this.registerCommand("spawn", "<town-name>", "Teleports to a town's spawn", new TownSpawnCommand());
        this.registerCommand("leave", "", "Leaves the town", new TownLeaveCommand());
        this.registerCommand("deposit", "<town> [amount]", "Deposits money into the town bank", new TownDepositCommand());
        this.registerCommand("withdraw", "[amount]", "Withdraws money from the town bank", new TownWithdrawCommand());
        this.registerCommand("demote", "[resident-name]", "Demotes a resident to the next rank", new TownDemoteCommand());
        this.registerCommand("promote", "[resident-name]", "Promotes a resident to the next rank", new TownPromoteCommand());
        this.registerCommand("vote", voteArgs, "Voting on a new town mayor", new TownVoteCommand());
    }
}
