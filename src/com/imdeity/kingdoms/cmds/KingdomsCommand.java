package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.kingdoms.KingdomsInfoCommand;
import com.imdeity.kingdoms.cmds.kingdoms.KingdomsMapCommand;
import com.imdeity.kingdoms.cmds.kingdoms.KingdomsPricesCommand;

public class KingdomsCommand extends DeityCommandHandler {
    
    public KingdomsCommand(String pluginName) {
        super(pluginName, "Kingdoms");
    }
    
    @Override
    public void initRegisteredCommands() {
        this.registerCommand("prices", null, "<world>", "Lists Prices for the world specified", new KingdomsPricesCommand(), "kingdoms.kingdoms.prices");
        this.registerCommand("map", null, "<help/on/off>", "Shows a map of the land", new KingdomsMapCommand(), "kingdoms.kingdoms.map");
        this.registerCommand("info", null, "", "Gives info about the plugin and towns", new KingdomsInfoCommand(), "kingdoms.kingdoms.info");
    }
}
