package com.imdeity.kingdoms.cmds.kingdom;

import java.util.regex.Matcher;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.Kingdom;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;

public class KingdomChatCommand extends DeityCommandReceiver implements CommandExecutor {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        if (args.length < 1) { return false; }
        if (!resident.hasTown()) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
            return true;
        }
        if (resident.getTown().getKingdom() == null) {
            KingdomsMain.plugin.chat.sendPlayerMessage(player, "Your town is not in a kingdom");
            // TODO move to helper
            return true;
        }
        Kingdom kingdom = resident.getTown().getKingdom();
        String messageFormat = KingdomsMain.plugin.config.getString(String.format(KingdomsConfigHelper.KINGDOM_CHAT_MESSAGE_FORMAT, kingdom.getCapital().getTownSpawnLocation().getLocation().getWorld().getName()));
        String name = "%prefix%%player%%suffix%";
        String message = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(args, " ");
        if (KingdomsMain.plugin.config.getBoolean(String.format(KingdomsConfigHelper.USE_KINGDOMS_PREFIX, kingdom.getCapital().getTownSpawnLocation().getLocation().getWorld().getName()))) {
            name = name.replaceAll("%prefix%", resident.getKingdomFriendlyTitle() + " ");
            name = name.replaceAll("%player%", resident.getName());
            name = name.replaceAll("%suffix%", "");
        } else {
            name = name.replaceAll("%prefix%", DeityAPI.getAPI().getChatAPI().getPlayerPrefix(kingdom.getCapital().getTownSpawnLocation().getLocation().getWorld(), player.getName()));
            name = name.replaceAll("%player%", resident.getName());
            name = name.replaceAll("%suffix%", DeityAPI.getAPI().getChatAPI().getPlayerSuffix(kingdom.getCapital().getTownSpawnLocation().getLocation().getWorld(), player.getName()));
        }
        messageFormat = messageFormat.replaceAll("%player%", Matcher.quoteReplacement(name));
        messageFormat = messageFormat.replaceAll("%message%", Matcher.quoteReplacement(message));
        kingdom.sendMessageNoHeader(messageFormat);
        return true;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!onPlayerRunCommand(player, args)) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, "That is an invalid sub-command, check &3/kingdom help &f for help");
            }
        } else {
            KingdomsMain.plugin.chat.outWarn("This command must be used in-game");
        }
        return true;
    }
}
