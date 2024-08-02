package Main.LofiCaptchaDiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Main.LofiCaptchaDiscord.config.Config;
import Main.LofiCaptchaDiscord.discord.CommandListener;
import Main.LofiCaptchaDiscord.discord.LogHandler;
import Main.LofiCaptchaDiscord.listeners.Listeners;
import Main.LofiCaptchaDiscord.command.Command;
import Main.LofiCaptchaDiscord.command.Players;

@SuppressWarnings("unused")
public final class Main extends JavaPlugin {
	
	public Map<String, Integer> onProverka = new ConcurrentHashMap<>();
	public Map<String, Integer> noProverka = new ConcurrentHashMap<>();
	public Map<Player, String> codeMap = new ConcurrentHashMap<>();
	public Map<String, Integer> timer = new ConcurrentHashMap<>();
  
	private static Main inst;
	private Listeners listener;
	private JDA jda; 
	private Config config;
	private Command Command;
	private Players base;
	
	@Override
	public void onLoad() {
    	(inst = this).saveDefaultConfig();
	}	
   @Override	
   public void onEnable() {
      System.out.println("|------------------------------------------------------------------|");
      System.out.println("|                                                                  |");
      System.out.println("|      LofiAntiBot: Плагин был включен! :)                         |");
      System.out.println("|      Плагин был сделан Shved255 | Discord: Shved255              |");
      System.out.println("|------------------------------------------------------------------|");
	  inst = this;
  	  config = new Config(this);
      this.base = new Players(this);
  	File cfg = new File(getDataFolder() + File.separator + "config.yml"); {
  	if(!cfg.exists()) {
  		saveDefaultConfig();
  	}
  	File playersFile = new File(getDataFolder() + File.separator + "players.yml"); {
  	    if (!playersFile.exists())
  	      saveResource("players.yml", false); 
  		}
  	}
      this.listener = new Listeners(this);
      getServer().getPluginManager().registerEvents(listener, this);
      String token = getConfig().getString("Discord.Token");
	    getCommand("loficaptcha").setExecutor(new Command(this));
      try {
         this.jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MESSAGES, new GatewayIntent[]{GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT}).addEventListeners(new Object[]{new LogHandler(), new CommandListener()}).build();
      } catch (Exception var3) {
         this.getLogger().severe("§4Неверный токен, введите его в конфиге!");
         this.getServer().getPluginManager().disablePlugin(this);
      }

      this.jda.updateCommands().addCommands(new CommandData[]{Commands.slash("верификация", "Создать сообщение для верификации").setDefaultPermissions(DefaultMemberPermissions.enabledFor(new Permission[]{Permission.ADMINISTRATOR}))}).queue();
   }
   @Override
   public void onDisable() {
      System.out.println("|------------------------------------------------------------------|");
      System.out.println("|                                                                  |");
      System.out.println("|      LofiAntiBot: Плагин был выключен! :(                        |");
      System.out.println("|                                                                  |");
      System.out.println("|------------------------------------------------------------------|");
   }

   public static Main getInstance() {
      return inst;
   }
   
   public Map<String, Integer> getNoProverka() {
	   return this.noProverka;
   }
   
   public Map<String, Integer> getOnProverka() {
	   return this.onProverka;
   }
   
   public Map<String, Integer> getTimer() {
	   return this.timer;
   }
   
   public Map<Player, String> getCodeMap() {
	   return this.codeMap;
   }
   
   public Integer getTimer1(String nick) {
   	if(timer.containsKey(nick)) {
   		return timer.get(nick);
   	} else throw new IllegalStateException("nick not in map.");
   }
   
   public String getCode(Player player) {
	   	if(codeMap.containsKey(player)) {
	   		return codeMap.get(player);
	   	} else throw new IllegalStateException("nick not in map.");
	   }
   
   public Listeners getListener() {
	   return this.listener;
   }
   
   public Command getCommand() {
	   return this.Command;
   }
   
   public Config config() {
   	return this.config;
   }
   
   public Players getBase() {
     return this.base;
   }
}
