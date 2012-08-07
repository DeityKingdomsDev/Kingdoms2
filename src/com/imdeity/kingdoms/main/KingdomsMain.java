package com.imdeity.kingdoms.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import ca.xshade.bukkit.questioner.Questioner;
import ca.xshade.questionmanager.Question;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;
import com.imdeity.kingdoms.cmds.KingdomCommand;
import com.imdeity.kingdoms.cmds.KingdomsAdminCommand;
import com.imdeity.kingdoms.cmds.KingdomsCommand;
import com.imdeity.kingdoms.cmds.PlotCommand;
import com.imdeity.kingdoms.cmds.ResidentCommand;
import com.imdeity.kingdoms.cmds.TownCommand;
import com.imdeity.kingdoms.cmds.kingdom.KingdomChatCommand;
import com.imdeity.kingdoms.cmds.town.TownChatCommand;
import com.imdeity.kingdoms.listener.KingdomsPlayerListener;
import com.imdeity.kingdoms.obj.KingdomsManager;

public class KingdomsMain extends DeityPlugin {
    
    public static KingdomsMain plugin;
    public List<String> residentsInMapMode = new ArrayList<String>();
    
    protected void initCmds() {
        this.registerCommand(new KingdomsAdminCommand(this.getName()));
        this.registerCommand(new KingdomsCommand(this.getName()));
        this.registerCommand(new KingdomCommand(this.getName()));
        this.registerCommand(new TownCommand(this.getName()));
        this.registerCommand(new PlotCommand(this.getName()));
        this.registerCommand(new ResidentCommand(this.getName()));
        this.getCommand("TownChat").setExecutor(new TownChatCommand());
        this.getCommand("KingdomChat").setExecutor(new KingdomChatCommand());
    }
    
    protected void initDatabase() {
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .writeNoError(
                        "ALTER TABLE "
                                + getResidentTableName()
                                + " CHANGE `is_assistant` `is_senior_assistant` INT( 1 ) NOT NULL, CHANGE `is_helper` `is_assistant` INT( 1 ) NOT NULL;");
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .writeNoError(
                        "ALTER TABLE "
                                + getResidentTableName()
                                + " ADD (`first_online` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `last_online` TIMESTAMP NULL, `total_time_online` INT(16) NOT NULL DEFAULT '0');");
        DeityAPI.getAPI().getDataAPI().getMySQL()
                .writeNoError("ALTER TABLE " + getTownTableName() + " ADD (`num_bonus_plots` INT(16) NOT NULL DEFAULT '0');");
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .writeNoError(
                        "UPDATE "
                                + getResidentTableName()
                                + " SET first_online = CURRENT_TIMESTAMP, last_online = CURRENT_TIMESTAMP, total_time_online = 0 WHERE last_online is null");
        DeityAPI.getAPI().getDataAPI().getMySQL()
                .writeNoError("ALTER TABLE " + getChunkTableName() + " ADD (`can_explode` INT(1) NOT NULL DEFAULT '0');");
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getKingdomTableName() + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, "
                        + " `name` VARCHAR(32) NOT NULL, " + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                        + " PRIMARY KEY (`id`), " + " UNIQUE KEY (`name`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getTownTableName() + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, "
                        + " `name` VARCHAR(32) NOT NULL, " + " `kingdom_id` INT(16) NOT NULL DEFAULT '-1', "
                        + " `town_board` VARCHAR(64) NOT NULL, " + " `default_plot_price` INT(20) NOT NULL DEFAULT '50', "
                        + " `spawn_location_id` INT(16) NOT NULL, " + " `is_public` INT(1) NOT NULL, "
                        + " `is_capital` INT(1) NOT NULL DEFAULT '0', " + " `edit_permission` INT(1) NOT NULL DEFAULT '2', "
                        + " `use_permission` INT(1) NOT NULL DEFAULT '2', " + " `access_permission` INT(1) NOT NULL DEFAULT '0', "
                        + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                        + " `num_bonus_plots` INT(16) NOT NULL DEFAULT '0', " + " PRIMARY KEY (`id`), " + " UNIQUE KEY (`name`), "
                        + " INDEX (`kingdom_id`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getTownSpawnLocationTableName() + " ("
                        + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `world` VARCHAR(64) NOT NULL, "
                        + " `x_coord` INT(16) NOT NULL, " + " `y_coord` INT(16) NOT NULL, " + " `z_coord` INT(16) NOT NULL, "
                        + " `pitch` INT(16) NOT NULL, " + " `yaw` INT(16) NOT NULL, "
                        + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (`id`) "
                        + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getChunkTableName() + "( " + "`id` INT(16) NOT NULL AUTO_INCREMENT, "
                        + "`deity_protect_id` INT(16) NOT NULL, " + " `town_id` INT(16) NOT NULL, " + " `for_sale` INT(1) NOT NULL, "
                        + " `price` INT(4) NOT NULL DEFAULT '0', " + " `can_mobs_spawn` INT(1) NOT NULL DEFAULT '1', "
                        + " `can_pvp` INT(1) NOT NULL DEFAULT '0', " + " `can_explode` INT(1) NOT NULL DEFAULT '0', "
                        + " PRIMARY KEY (`id`), INDEX (`deity_protect_id`) " + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getResidentTableName() + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, "
                        + " `name` VARCHAR(30) NOT NULL, " + " `town_id` INT(16) NOT NULL, " + " `is_king` INT(1) NOT NULL, "
                        + " `is_mayor` INT(1) NOT NULL, " + " `is_senior_assistant` INT(1) NOT NULL, "
                        + " `is_assistant` INT(1) NOT NULL, " + " `is_male` INT(1) NOT NULL, "
                        + "`deed` INT(16) NOT NULL DEFAULT '-1', " + " `edit_permission` INT(1) NOT NULL DEFAULT '4', "
                        + " `use_permission` INT(1) NOT NULL DEFAULT '4', " + " `access_permission` INT(1) NOT NULL DEFAULT '2', "
                        + "`first_online` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + "`last_online` TIMESTAMP NULL, "
                        + "`total_time_online` INT(16) NOT NULL DEFAULT '0', " + " PRIMARY KEY (`id`), " + " UNIQUE KEY (`name`) ,"
                        + " INDEX (`town_id`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getResidentFriendTableName() + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, "
                        + " `resident_id` INT(16) NOT NULL, " + " `friend_id` INT(16) NOT NULL, " + " PRIMARY KEY (`id`), "
                        + " INDEX (`resident_id`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getRequestTableName() + "("
                        + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`player_name` VARCHAR( 20 ) NOT NULL ,"
                        + "`type` VARCHAR( 64 ) NOT NULL COMMENT  'Valid values are \"TOWN\", \"KINGDOM\"',"
                        + "`requested_id` INT( 16 ) NOT NULL ," + "`is_approved` INT( 1 ) NOT NULL DEFAULT  '0',"
                        + "`is_closed` INT( 1 ) NOT NULL DEFAULT  '0'," + "`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                        + "INDEX (  `player_name` ,  `type` ,  `is_closed` )" + ") ENGINE = MYISAM ;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getWarpTableName() + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, "
                        + "`name` VARCHAR(64) NOT NULL, " + "`town_id` INT(16) NOT NULL, " + "`price` INT(4) NOT NULL DEFAULT '0', "
                        + " `world` VARCHAR(64) NOT NULL, " + " `x_coord` INT(16) NOT NULL, " + " `y_coord` INT(16) NOT NULL, "
                        + " `z_coord` INT(16) NOT NULL, " + " `pitch` INT(16) NOT NULL, " + " `yaw` INT(16) NOT NULL, "
                        + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (`id`), "
                        + "INDEX (`town_id`) " + ") ENGINE = MYISAM;");
        
    }
    
    @Override
    public void initConfig() {
        for (World world : this.getServer().getWorlds()) {
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.KINGDOM_PRICES_CREATE, world.getName()), 100000.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PRICES_CREATE, world.getName()), 50000.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PRICES_CLAIM, world.getName()), 500.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PRICES_SPAWN, world.getName()), 10.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN, world.getName()), 250.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PRICES_SET_PVP, world.getName()), 1000.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PRICES_WARP_ADD, world.getName()), 2500.0D);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.DEFAULT_TOWN_BOARD, world.getName()),
                    "Welcome to the town!");
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_BORDER, world.getName()), 20);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.KINGDOM_BORDER, world.getName()), 50);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_PLOTS_PER_RESIDENT, world.getName()), 4);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.USE_KINGDOMS_PREFIX, world.getName()), true);
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.TOWN_CHAT_MESSAGE_FORMAT, world.getName()),
                    "&7[&bTC&7] %player%&7: %message%");
            this.config.addDefaultConfigValue(String.format(KingdomsConfigHelper.KINGDOM_CHAT_MESSAGE_FORMAT, world.getName()),
                    "&7[&6KC&7] %player%&7: %message%");
        }
    }
    
    @Override
    public void initLanguage() {
    }
    
    @Override
    public void initInternalDatamembers() {
        KingdomsManager.reload();
    }
    
    @Override
    public void initListeners() {
        this.registerListener(new KingdomsPlayerListener());
    }
    
    @Override
    public void initPlugin() {
        plugin = this;
    }
    
    @Override
    public void initTasks() {
    }
    
    public static String getKingdomTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "kingdoms");
    }
    
    public static String getTownTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "towns");
    }
    
    public static String getTownSpawnLocationTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "spawn_locations");
    }
    
    public static String getChunkTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks");
    }
    
    public static String getResidentTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "residents");
    }
    
    public static String getResidentFriendTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings");
    }
    
    public static String getRequestTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "requests");
    }
    
    public static String getWarpTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "warp_locations");
    }
    
    public static void appendQuestion(Questioner questioner, Question question) throws Exception {
        questioner.appendQuestion(question);
    }
    
}