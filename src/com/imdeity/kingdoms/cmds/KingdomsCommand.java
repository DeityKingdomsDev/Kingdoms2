package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.kingdoms.KingdomsMapCommand;
import com.imdeity.kingdoms.cmds.kingdoms.KingdomsPricesCommand;

public class KingdomsCommand extends DeityCommandHandler {
    
    @Override
    public void initRegisteredCommands() {
        this.registerCommand("prices", "", "Lists Prices", new KingdomsPricesCommand());
        this.registerCommand("map", "<help/on/off>", "Shows a map of the land", new KingdomsMapCommand());
    }
}
