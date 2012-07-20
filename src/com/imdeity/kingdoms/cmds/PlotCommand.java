package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.plot.PlotClaimCommand;
import com.imdeity.kingdoms.cmds.plot.PlotSetCommand;
import com.imdeity.kingdoms.cmds.plot.PlotUnclaimCommand;

public class PlotCommand extends DeityCommandHandler {
    
    @Override
    public void initRegisteredCommands() {
        String[] setArgs = { "for-sale [price]", "not-for-sale", "mob-spawning [allow/deny]", "pvp [allow/deny]" };
        this.registerCommand("claim", "", "Attemps to claim a plot", new PlotClaimCommand());
        this.registerCommand("set", setArgs, "Allows user to set flags on plot", new PlotSetCommand());
        this.registerCommand("unclaim", "", "Attempts to unclaim a plot", new PlotUnclaimCommand());
    }
}
