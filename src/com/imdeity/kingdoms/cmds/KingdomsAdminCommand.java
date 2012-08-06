package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.admin.AdminCreateNPCTownCommand;
import com.imdeity.kingdoms.cmds.admin.AdminReloadCommand;
import com.imdeity.kingdoms.cmds.admin.AdminSetCommand;
import com.imdeity.kingdoms.cmds.admin.AdminTownAddCommand;

public class KingdomsAdminCommand extends DeityCommandHandler {
    
    public KingdomsAdminCommand(String pluginName) {
        super(pluginName, "KingdomsAdmin");
    }
    
    @Override
    public void initRegisteredCommands() {
        String[] createNpcAliases = { "cnt" };
        this.registerCommand("reload", null, "", "Reloads cache", new AdminReloadCommand(), "kingdoms.admin.reload");
        this.registerCommand("set", null, "[node] [value]", "Changes configuration options", new AdminSetCommand(), "kingdoms.admin.set");
        this.registerCommand("mayor-set", null, "[town-name]", "Add's a resident to the town and adds them as mayor", new AdminTownAddCommand(), "kingdoms.admin.mayor-add");
        this.registerCommand("create-npc-town", createNpcAliases, "<town-name>", "Creates a town without a mayor", new AdminCreateNPCTownCommand(), "kingdoms.admin.create-npc-town");
    }
}
