package com.imdeity.kingdoms.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.kingdoms.cmds.admin.AdminReloadCommand;
import com.imdeity.kingdoms.cmds.admin.AdminSetCommand;
import com.imdeity.kingdoms.cmds.admin.AdminSetMayorCommand;

public class KingdomsAdminCommand extends DeityCommandHandler {
    
    public KingdomsAdminCommand(String pluginName) {
        super(pluginName, "KingdomsAdmin");
    }
    
    @Override
    public void initRegisteredCommands() {
        this.registerCommand("reload", null, "", "Reloads cache", new AdminReloadCommand(), "kingdoms.admin.reload");
        this.registerCommand("set", null, "[node] [value]", "Changes configuration options", new AdminSetCommand(), "kingdoms.admin.set");
        this.registerCommand("set-mayor", null, "[town-name] [player-name/NPC]", "Add's a resident to the town and adds them as mayor", new AdminSetMayorCommand(), "kingdoms.admin.set-mayor");
    }
}
