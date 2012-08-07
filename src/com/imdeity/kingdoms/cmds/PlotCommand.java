package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.plot.PlotClaimCommand;
import com.imdeity.kingdoms.cmds.plot.PlotSetCommand;
import com.imdeity.kingdoms.cmds.plot.PlotUnclaimCommand;

public class PlotCommand extends DeityCommandHandler {
    
    public PlotCommand(String pluginName) {
        super(pluginName, "Plot");
    }
    
    @Override
    public void initRegisteredCommands() {
        String[] setArgs = { "for-sale [price]", "not-for-sale", "mob-spawning [allow/deny]", "pvp [allow/deny]", "explode [allow/deny]" };
        this.registerCommand("claim", null, "", "Attemps to claim a plot", new PlotClaimCommand(), "kingdoms.plot.claim");
        this.registerCommand("set", null, setArgs, "Allows user to set flags on plot", new PlotSetCommand(), "kingdoms.plot.set");
        this.registerCommand("unclaim", null, "", "Attempts to unclaim a plot", new PlotUnclaimCommand(), "kingdoms.plot.unclaim");
    }
}
