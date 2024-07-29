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
	private List<String> commandsPlayer;
	private List<String> commandsServer;
	
	public Config(Main plugin) {
		
		this.plugin = plugin;
		File file = new File(plugin.getDataFolder() + File.separator, "config.yml"); 
	    this.cfg = YamlConfiguration.loadConfiguration(file);
	    kickMessage = cfg.getString("Minecraft.KickMessage");
	    barInfo = cfg.getString("Minecraft.BossBarInfo");
	    message = cfg.getString("Minecraft.Message");	
	    successMessage = cfg.getString("Minecraft.SuccessMessage");
	    commandsPlayer = cfg.getStringList("Minecraft.PlayerCommands");
	    commandsServer = cfg.getStringList("Minecraft.ServerCommands");
	}
	
	public static String ChatColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public FileConfiguration getConfig() {
		return this.cfg;
	}
	
	public String getBarInfo(String nick) {	
		return ChatColor(this.barInfo.replace("{TIME}", String.valueOf(plugin.getTimer1(nick))));
	}
	
	public String getMessage(String nick) {
		return ChatColor(this.message.replace("{TIME}", String.valueOf(plugin.getTimer1(nick))));
	}
	
	public String getKickMessage() {
		return ChatColor(this.kickMessage);
	}
	
	public String getSuccessMessage() {
		return ChatColor(this.successMessage);
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
