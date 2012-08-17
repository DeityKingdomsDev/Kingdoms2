package com.imdeity.kingdoms.main;

public class KingdomsMessageHelper {
    
    public static final String CMD_ADMIN_RELOAD = "Reloaded the cache";
    public static final String CMD_TOWN_CREATE_IN_TOWN = "You already belong to a town, please leave it before making a new one.";
    public static final String CMD_TOWN_CREATE_SUCCESS_PUBLIC = "%s created a new town named %s";
    public static final String CMD_TOWN_ADD_GLOBAL = "%s was added to the town of %s.";
    public static final String CMD_PLOT_CLAIM_PLAYER = "You claimed this plot for $%s";
    public static final String CMD_PLOT_UNCLAIM_PLAYER = "You unclaimed this plot";
    public static final String CMD_PLOT_SET_FORSALE_PLAYER = "You put this plot up for sale at $%s";
    public static final String CMD_PLOT_SET_NOTFORSALE_PLAYER = "You took this plot down from sale";
    public static final String CMD_PLOT_SET_MOBSPAWN_PLAYER = "You set this plot to %s mob spawning";
    public static final String CMD_PLOT_SET_PVP_PLAYER = "You set this plot to %s pvp";
    public static final String CMD_PLOT_SET_EXPLODE_PLAYER = "You set this plot to %s explosions";
    public static final String CMD_RES_ADD_FRIEND_PLAYER = "You added %s to your friends";
    public static final String CMD_RES_REMOVE_FRIEND_PLAYER = "You removed %s from your friends";
    public static final String CMD_RES_SET_PERMISSIONS_UPDATED = "You updated your %s permission to %s";
    public static final String CMD_TOWN_CLAIM_TOWN = "%s claimed a new plot (%s, %s)";
    public static final String CMD_TOWN_UNCLAIM_TOWN = "%s unclaimed a plot (%s, %s)";
    public static final String CMD_TOWN_DELETE_PLAYER = "You deleted your town";
    public static final String CMD_TOWN_DELETE_CONSOLE = "You deleted %s";
    public static final String CMD_TOWN_KICK_PLAYER = "You kicked %s from the town";
    public static final String CMD_TOWN_LEAVE_PLAYER = "You left the town";
    public static final String CMD_TOWN_SET_PERMISSIONS_UPDATED = "You updated the towns %s permission to %s";
    public static final String CMD_TOWN_SET_SPAWN_PLAYER = "You set the town spawn to %s, %s, %s";
    public static final String CMD_TOWN_SET_VISCOUNT = "You set %s as a viscount";
    public static final String CMD_KINGDOMS_MAP_ON = "You have turned Map Mode On!";
    public static final String CMD_KINGDOMS_MAP_OFF = "You have turned Map Mode Off!";
    public static final String CMD_TOWN_SET_BOARD_PLAYER = "You set the town board to %s";
    public static final String CMD_TOWN_SET_PLOT_PRICE_PLAYER = "You set the default plot price to %s";
    public static final String CMD_TOWN_SET_PUBLIC_PLAYER = "You set the town status to %s";
    public static final String CMD_TOWN_DEPOSIT_PLAYER = "You deposited %s into the town of %s";
    public static final String CMD_TOWN_WITHDRAW_PLAYER = "You withdrew %s from the town of %s";
    public static final String CMD_TOWN_PROMOTE_HELPER_TOWN = "%s was promoted to the rank of Helper";
    public static final String CMD_TOWN_PROMOTE_ASSISTANT_TOWN = "%s was promoted to the rank of Assistant";
    public static final String CMD_TOWN_PROMOTE_MAYOR_TOWN = "%s was promoted to the rank of Mayor";
    public static final String CMD_TOWN_DEMOTE_HELPER_TOWN = "%s was demoted to the rank of Resident";
    public static final String CMD_TOWN_DEMOTE_ASSISTANT_TOWN = "%s was demoted to the rank of Helper";
    public static final String CMD_TOWN_REQUEST_SENT = "%s has been invited to town.";
    public static final String CMD_KINGDOM_CREATE_SUCCESS_PUBLIC = "%s created a new Kingdom named %s";
    public static final String CMD_RESIDENT_CHANGE_GENDER = "Changed your gender to %s";
    public static final String CMD_KINGDOM_TOWN_ADD = "%s was added to the %s kingdom";
    public static final String CMD_KINGDOM_REMOVE_TOWN = "%s was removed from the kingdom";
    public static final String CMD_KINGDOM_LEAVE = "Your town left %s";
    public static final String CMD_KINGDOM_DEPOSIT_PLAYER = "You deposited %s into the kingdom of %s";
    public static final String CMD_KINGDOM_WITHDRAW_PLAYER = "You withdrew %s from the kingdom of %s";
    public static final String CMD_KINGDOM_ADMIN_SET = "You set %s to %s";
    
    public static final String CMD_FAIL_INVALID_LOCATION = "There is an existing town here. Please move away before creating your town.";
    public static final String CMD_FAIL_RESIDENT_NOT_IN_TOWN = "%s does not belong to your town.";
    public static final String CMD_FAIL_NOT_IN_TOWN = "You do not belong to a town.";
    public static final String CMD_FAIL_NOT_IN_KINGDOM = "Your town does not belong to a kingdom, or you do not have a town";
    public static final String CMD_FAIL_PERMISSION_KING = "You need to be a king/queen before you can run that command.";
    public static final String CMD_FAIL_NO_MONEY = "You do not have enough money. Please check /kingdoms prices";
    public static final String CMD_FAIL_NO_MONEY_TOWN = "Your town does not have enough money. Please check /kingdoms prices";
    public static final String CMD_FAIL_PLAYER_NULL = "That resident does not exist.";
    public static final String CMD_FAIL_ALREADY_IN_TOWN = "That resident already belongs to a town.";
    public static final String CMD_FAIL_NAME_INVALID = "The name %s is invalid. Name must be under 20 characters and can only contain letters, underscores and hyphens";
    public static final String CMD_FAIL_TOWN_CREATE_EXIST = "The town %s already exists";
    public static final String CMD_FAIL_KINGDOM_CREATE_EXIST = "The kingdom %s already exists";
    public static final String CMD_FAIL_NOT_TOWN_STAFF = "You are not the Mayor or Assistant of this town";
    public static final String CMD_FAIL_NOT_TOWN_DUKE = "You are not the Mayor of this town";
    public static final String CMD_FAIL_NOT_KING = "You are not the King of this kingdom";
    public static final String CMD_FAIL_CANNOT_FIND_RESIDENT = "The resident %s is invalid";
    public static final String CMD_FAIL_CANNOT_FIND_TOWN = "The town %s is invalid";
    public static final String CMD_FAIL_CANNOT_FIND_KINGDOM = "The kingdom %s is invalid";
    public static final String CMD_FAIL_PLOT_NOT_FOR_SALE = "Sorry, this plot is not for sale";
    public static final String CMD_FAIL_PLOT_INVALID_LOCATION = "This plot does not belong to your town";
    public static final String CMD_FAIL_PLOT_WILDERNESS = "This plot is in wilderness.";
    public static final String CMD_FAIL_PLOT_NOT_OWNER = "You are not the owner of this plot";
    public static final String CMD_FAIL_PLOT_SET_NEED_ARGUMENT = "Please specify if you wish to allow or deny.";
    public static final String CMD_FAIL_RES_RESIDENT_INVALID = "%s can not be found";
    public static final String CMD_FAIL_MIN_PLOTS = "You cannot unclaim the last plot of your town. Please &a/town delete &fif you wish to get rid of your town";
    public static final String CMD_FAIL_TOWN_SIZE_TOO_SMALL = "The town has reached their maximum plots. You need to recruit before you can claim more land.";
    public static final String CMD_FAIL_TOWN_CREATE_RESIDENT_IN_TOWN = "You cannot start a town while you belong to another";
    public static final String CMD_FAIL_TOWN_KICK_INVALID_PLAYER = "%s is not a member of your town";
    public static final String CMD_FAIL_TOWN_LEAVE_DUKE = "Please promote someone to mayor before leaving your town";
    public static final String CMD_FAIL_TOWN_SET_SPAWN = "The spawn needs to be in an unclaimed town plot";
    public static final String CMD_FAIL_TOWN_WARP = "The warps needs to be on an unclaimed town plot";
    public static final String CMD_FAIL_TOWN_PLOT_NOT_ADJACENT = "New town plots need to be beside another town plot";
    public static final String CMD_FAIL_INVALID_PRICE = "Invalid price. Please use a valid amount";
    public static final String CMD_WARN_INVALID_PRICE = "Invalid price, Using town default instead";
    public static final String CMD_FAIL_CANNOT_PROMOTE = "You cannot promote %s to any higher ranks";
    public static final String CMD_FAIL_CANNOT_DEMOTE = "You cannot demote %s to any lower ranks";
    public static final String CMD_FAIL_KINGDOM_CREATE_NO_TOWN = "You need to own a town before creating a kingdom";
    public static final String CMD_FAIL_KINGDOM_CREATE_ALREADY_IN_KINGDOM = "You are already in a kingdom. Please leave your kingdom before attempting to create another";
    public static final String CMD_FAIL_TOWN_NOT_NOBLE = "You are not a level one Noble";
    public static final String CMD_FAIL_KINGDOM_REQUEST_NOT_KING = "You need to be a king to use this command";
    public static final String CMD_FAIL_KINGDOM_REMOVE_CAPITAL = "You are attempting to remove the capital. Please set another capital before leaving your kingdom";
    public static final String CMD_FAIL_KINGDOM_REMOVE_INVALID = "The town of %s does not belong to your kingdom";
    public static final String CMD_FAIL_UNCLAIM_NON_TOWN_LAND = "You cannot unclaim plots that are not part of town.";
    public static final String CMD_FAIL_UNCLAIM_TOWN_SPAWN = "You cannot unclaim the plot that is the town spawn.";
    
    public static final String CMD_ERROR_TRY_AGAIN = "There was an error executing your command; please try again soon.";
    
    public static final String CMD_KINGDOM_TOO_CLOSE = "%s is %s blocks to the %s. Please move farther away from the kingdom or &c/kingdom request join <kingdom-name>";
    public static final String CMD_TOWN_TOO_CLOSE = "%s is %s blocks to the %s. Please move farther away from them.";
    
    public static final String CMD_REQUEST_KIGDOM_NEW = "A new request was made. &6/kingdom request list &f to check it out";
    public static final String CMD_REQUEST_CONFIRM = "Your request has been submitted";
    public static final String CMD_REQUEST_KINGDOM_JOIN_OUTPUT = "[%s]%s would like to join your kingdom [%s]";
    public static final String CMD_REQUEST_KINGDOM_CREATE_JOIN_OUTPUT = "[%s]%s would like to create a town in your kingdom [%s]";
    public static final String CMD_REQUEST_ACCEPT_CONFIRM = "You accepted %s's request";
    public static final String CMD_REQUEST_ACCEPT_MAIL = "Your request to join %s has been accepted";
    public static final String CMD_REQUEST_DENIED_CONFIRM = "You denied %s's request";
    public static final String CMD_REQUEST_DENIED_MAIL = "Your request to join %s has been denied";
    
    public static final String VERIFING_LOCATION = "Verifying town location...";
    
    public static final String MOVEMENT_WILDERNESS = "&f~&aWilderness";
    public static final String MOVEMENT_TOWN_CLAIMED = "&f~&6%s &7- &e%s &7";
    public static final String MOVEMENT_TOWN_UNCLAIMED_SALE = "&f~&6For Sale! &7(&a%s&7) - &e%s ";
    public static final String MOVEMENT_TOWN_UNCLAIMED = "&f~&e%s";
}
