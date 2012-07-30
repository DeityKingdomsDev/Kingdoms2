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
import com.imdeity.kingdoms.cmds.town.TownWarpCommand;
import com.imdeity.kingdoms.cmds.town.TownWithdrawCommand;

public class TownCommand extends DeityCommandHandler {
    
    public TownCommand(String pluginName) {
        super(pluginName, "Town");
    }
    
    @Override
    public void initRegisteredCommands() {
        String[] setArgs = { "town-board [message]", "plot-price [amount]", "spawn", "public [on/off]", "permissions edit [P/F/T/K/TS/KS]", "permissions use [P/F/T/K/TS/KS]", "permissions access [P/F/T/K/TS/KS]" };
        String[] voteArgs = { "create", "[assistant-name]", "check" };
        String[] warpArgs = { "list", "[warp-name]", "add [warp-name]", "remove [warp-name]" };
        this.registerCommand("list", "", "Lists all towns", new TownListCommand(), "kingdoms.town.list");
        this.registerCommand("info", "<town-name> <-o>", "Displays a towns information", new TownInfoCommand(), "kingdoms.town.info");
        this.registerCommand("create", "[town-name]", "Creates a new Town", new TownCreateCommand(), "kingdoms.town.create");
        this.registerCommand("delete", "[town-name]", "Deletes a town", new TownDeleteCommand(), "kingdoms.town.delete");
        this.registerCommand("claim", "", "Claims new town plots", new TownClaimCommand(), "kingdoms.town.claim");
        this.registerCommand("unclaim", "", "Unclaims this town plots", new TownUnclaimCommand(), "kingdoms.town.unclaim");
        this.registerCommand("add", "[player-name]", "Add a resident to the town", new TownAddCommand(), "kingdoms.town.add");
        this.registerCommand("kick", "[player-name]", "Kicks a resident from the town", new TownKickCommand(), "kingdoms.town.kick");
        this.registerCommand("set", setArgs, "Changes a towns options", new TownSetCommand(), "kingdoms.town.set");
        this.registerCommand("spawn", "<town-name>", "Teleports to a town's spawn", new TownSpawnCommand(), "kingdoms.town.spawn");
        this.registerCommand("leave", "", "Leaves the town", new TownLeaveCommand(), "kingdoms.town.leave");
        this.registerCommand("deposit", "<town> [amount]", "Deposits money into the town bank", new TownDepositCommand(), "kingdoms.town.deposit");
        this.registerCommand("withdraw", "[amount]", "Withdraws money from the town bank", new TownWithdrawCommand(), "kingdoms.town.withdraw");
        this.registerCommand("demote", "[resident-name]", "Demotes a resident to the next rank", new TownDemoteCommand(), "kingdoms.town.demote");
        this.registerCommand("promote", "[resident-name]", "Promotes a resident to the next rank", new TownPromoteCommand(), "kingdoms.town.promote");
        this.registerCommand("vote", voteArgs, "Voting on a new town mayor", new TownVoteCommand(), "kingdoms.town.vote");
        this.registerCommand("warp", warpArgs, "Town warp center", new TownWarpCommand(), "kingdoms.town.warp");
    }
}
