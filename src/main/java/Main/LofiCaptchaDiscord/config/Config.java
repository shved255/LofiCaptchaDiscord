package Main.LofiCaptchaDiscord.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import Main.LofiCaptchaDiscord.Main;

public class Config {
	
	private Main plugin;
	private FileConfiguration cfg;
	private String barInfo;
	private String message;
	private String kickMessage;
	private String successMessage;
	private String barColor;
	private String barStyle;
	private int time;
	private Boolean lvl;
	private String embedTitle;
	private String embedDescription;
	private String embedColor;
	private String successSendPacket;
	private String playerIsNotOnline;
	private String playerInBase;
	private String playerSuccessDiscord;
	private String wrongMessage;
	private List<String> commandsPlayer;
	private List<String> commandsServer;
	
	public Config(Main plugin) {
		
		this.plugin = plugin;
		File file = new File(plugin.getDataFolder() + File.separator, "config.yml"); 
	    this.cfg = YamlConfiguration.loadConfiguration(file);
	    kickMessage = cfg.getString("Minecraft.KickMessage");
	    message = cfg.getString("Minecraft.Message");	
	    successMessage = cfg.getString("Minecraft.SuccessMessage");
	    commandsPlayer = cfg.getStringList("Minecraft.PlayerCommands");
	    commandsServer = cfg.getStringList("Minecraft.ServerCommands");
	    barInfo = cfg.getString("BossBar.BossBarInfo");
	    barColor = cfg.getString("BossBar.Color");
	    barStyle = cfg.getString("BossBar.Style");
	    time = cfg.getInt("Minecraft.Time");
	    lvl = cfg.getBoolean("Minecraft.LevelEnable");
        embedTitle = cfg.getString("Discord.Title");
        embedDescription = cfg.getString("Discord.Description");
        embedColor = cfg.getString("Discord.Color");
        successSendPacket = cfg.getString("Discord.SuccessSendPacket");
        playerIsNotOnline = cfg.getString("Discord.PlayerNotOnline");
        playerInBase = cfg.getString("Discord.PlayerInBase");
        playerSuccessDiscord = cfg.getString("Discord.PlayerSuccessDiscord");
        wrongMessage = cfg.getString("Discord.WrongMessage");
        
	}
	
	public static String ChatColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public FileConfiguration getConfig() {
		return this.cfg;
	}
	
	public String getBarInfo(String nick, Player player) {
	    String barInfoWithTime = this.barInfo.replace("{TIME}", String.valueOf(plugin.getTimer1(nick)));
	    return ChatColor(barInfoWithTime.replace("{CODE}", String.valueOf(plugin.getCode(player))));
	}
	
	public String getMessage(String nick, Player player) {
		String messageWithTime = this.message.replace("{TIME}", String.valueOf(plugin.getTimer1(nick)));
		return ChatColor(messageWithTime.replace("{CODE}", String.valueOf(plugin.getCode(player))));
	}
	
	public String getKickMessage() {
		return ChatColor(this.kickMessage);
	}
	
	public String getEmbedTitle() {
		return this.embedTitle;
	}
	
	public String getEmbedDescription() {
		return this.embedDescription;
	}
	
	public String getEmbedColor() {
		return this.embedColor;
	}
	
	public String getSuccessSendPacket() {
		return this.successSendPacket;
	}
	
	public String getPlayerIsNotOnline() {
		return this.playerIsNotOnline;
	}
	
	public String getPlayerInBase() {
		return this.playerInBase;
	}
	
	public String getPlayerSuccessDiscord() {
		return this.playerSuccessDiscord;
	}
	
	public String getWrongMessage() {
		return this.wrongMessage;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public Boolean getLvl() {
		return this.lvl;
	}
	
	public String getSuccessMessage() {
		return ChatColor(this.successMessage);
	}
	
	public String getBarColor() {
		return this.barColor;
	}
	
	public String getBarStyle() {
		return this.barStyle;
	}
	public Main getPlugin() {
		return plugin;
	}
	
	public List<String> getCommandsPlayer(Player player) {
		List<String> result = new ArrayList<>(this.commandsPlayer);
		return result;
	}
		  
	public List<String> getCommandsServer(Player player) {
	    List<String> result = new ArrayList<>();
	    for (String command : this.commandsServer) {
	        result.add(command.replace("%PLAYER%", player.getName()));
	    }
	    return result;
	}
}
