package com.imdeity.kingdoms.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;
import com.imdeity.kingdoms.cmds.KingdomCommand;
import com.imdeity.kingdoms.cmds.KingdomsAdminCommand;
import com.imdeity.kingdoms.cmds.KingdomsCommand;
import com.imdeity.kingdoms.cmds.PlotCommand;
import com.imdeity.kingdoms.cmds.ResidentCommand;
import com.imdeity.kingdoms.cmds.TownCommand;
import com.imdeity.kingdoms.listener.KingdomsPlayerListener;
import com.imdeity.kingdoms.obj.KingdomsManager;

public class KingdomsMain extends DeityPlugin {
    
    public static KingdomsMain plugin;
    public List<String> residentsInMapMode = new ArrayList<String>();
    
    protected void initCmds() {
        this.registerCommand(new KingdomsAdminCommand());
        this.registerCommand(new KingdomsCommand());
        this.registerCommand(new KingdomCommand());
        this.registerCommand(new TownCommand());
        this.registerCommand(new PlotCommand());
        this.registerCommand(new ResidentCommand());
    }
    
    protected void initDatabase() {
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "kingdoms") + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `name` VARCHAR(32) NOT NULL, " + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                        + " PRIMARY KEY (`id`), " + " UNIQUE KEY (`name`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "towns") + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `name` VARCHAR(32) NOT NULL, " + " `kingdom_id` INT(16) NOT NULL DEFAULT '-1', "
                        + " `town_board` VARCHAR(64) NOT NULL, " + " `default_plot_price` INT(20) NOT NULL DEFAULT '50', " + " `spawn_location_id` INT(16) NOT NULL, " + " `is_public` INT(1) NOT NULL, " + " `is_capital` INT(1) NOT NULL DEFAULT '0', "
                        + " `edit_permission` INT(1) NOT NULL DEFAULT '2', " + " `use_permission` INT(1) NOT NULL DEFAULT '2', " + " `access_permission` INT(1) NOT NULL DEFAULT '0', " + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (`id`), "
                        + " UNIQUE KEY (`name`), " + " INDEX (`kingdom_id`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "spawn_locations") + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `world` VARCHAR(64) NOT NULL, " + " `x_coord` INT(16) NOT NULL, " + " `y_coord` INT(16) NOT NULL, "
                        + " `z_coord` INT(16) NOT NULL, " + " `pitch` INT(16) NOT NULL, " + " `yaw` INT(16) NOT NULL, " + " `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (`id`) " + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "chunks") + "( " + "`id` INT(16) NOT NULL AUTO_INCREMENT, " + "`deity_protect_id` INT(16) NOT NULL, " + " `town_id` INT(16) NOT NULL, " + " `for_sale` INT(1) NOT NULL, "
                        + " `price` INT(4) NOT NULL DEFAULT '0', " + " `can_mobs_spawn` INT(1) NOT NULL DEFAULT '1', " + " `can_pvp` INT(1) NOT NULL DEFAULT '0', " + " PRIMARY KEY (`id`), INDEX (`deity_protect_id`) " + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "residents") + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `name` VARCHAR(30) NOT NULL, " + " `town_id` INT(16) NOT NULL, " + " `is_king` INT(1) NOT NULL, "
                        + " `is_mayor` INT(1) NOT NULL, " + " `is_assistant` INT(1) NOT NULL, " + " `is_helper` INT(1) NOT NULL, " + " `is_male` INT(1) NOT NULL, " + "`deed` INT(16) NOT NULL DEFAULT '-1', " + " `edit_permission` INT(1) NOT NULL DEFAULT '4', "
                        + " `use_permission` INT(1) NOT NULL DEFAULT '4', " + " `access_permission` INT(1) NOT NULL DEFAULT '2', " + " PRIMARY KEY (`id`), " + " UNIQUE KEY (`name`) ," + " INDEX (`town_id`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "friend_listings") + " (" + " `id` INT(16) NOT NULL AUTO_INCREMENT, " + " `resident_id` INT(16) NOT NULL, " + " `friend_id` INT(16) NOT NULL, " + " PRIMARY KEY (`id`), "
                        + " INDEX (`resident_id`)" + ") ENGINE = MYISAM;");
        
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + DeityAPI.getAPI().getDataAPI().getMySQL().tableName("kingdoms2_", "requests") + "(" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`player_name` VARCHAR( 20 ) NOT NULL ,"
                        + "`type` VARCHAR( 64 ) NOT NULL COMMENT  'Valid values are \"TOWN\", \"KINGDOM\"'," + "`requested_id` INT( 16 ) NOT NULL ," + "`is_approved` INT( 1 ) NOT NULL DEFAULT  '0'," + "`is_closed` INT( 1 ) NOT NULL DEFAULT  '0',"
                        + "`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," + "INDEX (  `player_name` ,  `type` ,  `is_closed` )" + ") ENGINE = MYISAM ;");
    }
    
    @Override
    public void initConfig() {
        this.config.addDefaultConfigValue(KingdomsConfigHelper.ENABLED_WORLD_NAME, "world");
        this.config.addDefaultConfigValue(KingdomsConfigHelper.KINGDOM_PRICES_CREATE, 100000.0D);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_PRICES_CREATE, 50000.0D);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_PRICES_CLAIM, 500.0D);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_PRICES_SPAWN, 10.0D);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_PRICES_SET_MOB_SPAWN, 250.0D);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_PRICES_SET_PVP, 1000.0D);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.DEFAULT_TOWN_BOARD, "Welcome to the town!");
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_BORDER, 20);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.KINGDOM_BORDER, 50);
        this.config.addDefaultConfigValue(KingdomsConfigHelper.TOWN_PLOTS_PER_RESIDENT, 4);
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
    
    public static World getKingdomWorld() {
        return plugin.getServer().getWorld(KingdomsMain.plugin.config.getString(KingdomsConfigHelper.ENABLED_WORLD_NAME));
    }
}