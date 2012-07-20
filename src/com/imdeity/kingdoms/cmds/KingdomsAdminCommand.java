package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.admin.AdminReloadCommand;
import com.imdeity.kingdoms.cmds.admin.AdminSetCommand;

public class KingdomsAdminCommand extends DeityCommandHandler {
    
    @Override
    public void initRegisteredCommands() {
        this.registerCommand("reload", "", "Reloads cache", new AdminReloadCommand());
        this.registerCommand("set", "[node] [value]", "Changes configuration options", new AdminSetCommand());
    }
}
