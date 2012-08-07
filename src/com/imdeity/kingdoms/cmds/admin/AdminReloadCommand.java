package com.imdeity.kingdoms.cmds.admin;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;

public class AdminReloadCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (!player.isOp()) { return false; }
        if (args.length == 1) {
            KingdomsManager.reload();
            KingdomsMain.plugin.reloadPlugin();
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_ADMIN_RELOAD);
        } else {
            String sql = "DELETE FROM kingdoms2_chunks WHERE deity_protect_id = -1;";
            DeityAPI.getAPI().getDataAPI().getMySQL().write(sql);
            sql = "DELETE FROM deity_protect_chunks WHERE id NOT IN (SELECT deity_protect_id FROM kingdoms2_chunks);";
            DeityAPI.getAPI().getDataAPI().getMySQL().write(sql);
            KingdomsManager.reload();
            KingdomsMain.plugin.reloadPlugin();
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_ADMIN_RELOAD);
        }
        return true;
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        KingdomsManager.reload();
        KingdomsMain.plugin.reloadPlugin();
        KingdomsMain.plugin.chat.out(KingdomsMessageHelper.CMD_ADMIN_RELOAD);
        return false;
    }
    
}
