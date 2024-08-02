package Main.LofiCaptchaDiscord.command;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import Main.LofiCaptchaDiscord.Main;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Players {
  private Main plugin;
  private FileConfiguration yml;
  private File filePlayers;
  public Players(Main plugin) {
    this.plugin = plugin;
    this.filePlayers = new File(this.plugin.getDataFolder() + File.separator + "players.yml");
    this.yml = (FileConfiguration)YamlConfiguration.loadConfiguration(this.filePlayers);
    
  }
  
  	public boolean needVerifed(Player player) {
	  	UUID uuid = player.getUniqueId();
	  	String id = uuid.toString();
    	if(!this.yml.contains(id)) return true;
    	int hours = Main.getInstance().getConfig().getInt("Minecraft.HoursTime");
    	if(hours <= 0) return false;
    	String newIp = player.getAddress().getHostName();
    	String oldIp = this.yml.getString(String.valueOf(id) + ".ip");
    	if(!newIp.equals(oldIp)) return true;
    	Instant oldInst = Instant.parse(this.yml.getString(String.valueOf(id) + ".date"));
    	Instant newInst = (new Date()).toInstant();
    	Duration duration = Duration.between(oldInst, newInst);
    	if (duration.toHours() >= hours) return true;  
    	return false;
  	}
  
  		public boolean setVerifed(Player player) {
	  		UUID uuid = player.getUniqueId();
    		String id = uuid.toString();
    		Date date = new Date();
    		Instant inst = date.toInstant();
    		String newIp = player.getAddress().getHostName();
    		this.yml.set(String.valueOf(id) + ".ip", newIp);
    		this.yml.set(String.valueOf(id) + ".name", player.getName());
    		this.yml.set(String.valueOf(id) + ".date", inst.toString());
    		try {
    		this.yml.save(this.filePlayers);
      		return true;
          } catch (IOException e) {
    		e.printStackTrace();
      		return false;
    			} 
  			}
		}

