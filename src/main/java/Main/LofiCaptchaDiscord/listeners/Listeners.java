package Main.LofiCaptchaDiscord.listeners;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import Main.LofiCaptchaDiscord.Main;
import Main.LofiCaptchaDiscord.Players;

public class Listeners implements Listener {
	@SuppressWarnings("unused")
	private Main main;
	private Main plugin;
	private String nick;
	private Player player;
	private int time;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
		
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
	player = event.getPlayer();
	nick = player.getName();
    Players base = this.plugin.getBase();
	if(player == null) return;
	if(nick == null) return;
	if(!base.needVerifed(player)) {
	Bukkit.getScheduler().runTaskLater(plugin, () -> { 
	    List<String> commandsPlayer = this.plugin.config().getCommandsPlayer(player);
	    List<String> commandsServer = this.plugin.config().getCommandsServer(player);
	    for (String command : commandsPlayer)
	      Bukkit.dispatchCommand((CommandSender)player, command); 
	    for (String command : commandsServer)
	      Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command); 
		}, 20 * 1); 
	}
	if(player == null) return;
	if(nick == null) return;
	if(base.needVerifed(player)) {
		Boolean Lvl = Main.getInstance().getConfig().getBoolean("Minecraft.LevelEnable");
        if(Lvl) {
        int lvl = Main.getInstance().getConfig().getInt("Minecraft.Time");;
        player.setLevel(lvl + 1);
        }
	time = Main.getInstance().getConfig().getInt("Minecraft.Time");
	plugin.timer.put(nick, time);
    BossBar bar = Bukkit.getServer().createBossBar(plugin.config().getBarInfo(nick), BarColor.RED, BarStyle.SOLID);
    bar.addPlayer(player);
	plugin.onProverka.put(nick, 60);	
	Bukkit.getScheduler().runTaskLater(plugin, () -> { 
	player.kickPlayer(plugin.config().getKickMessage());
	}, time * 20);
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> { 
		if(plugin.onProverka.containsKey(nick)) { 
	    	player.sendMessage(plugin.config().getMessage(nick));
	    	bar.setTitle(plugin.config().getBarInfo(nick));
            plugin.getTimer().put(nick, plugin.getTimer().get(nick) - 1);
			Boolean Lvl1 = Main.getInstance().getConfig().getBoolean("Minecraft.LevelEnable");
			if(Lvl1) {
            int level = player.getLevel();
            if (level >= 0) {
                player.setLevel(level - 1);
                }
			}
		}
    }, 5, 20 * 1); 
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> { 
    		if(plugin.noProverka.containsKey(nick)) {  			
    	    	plugin.noProverka.remove(nick);
    	    	bar.removePlayer(player);
    	    	plugin.timer.remove(nick);
    	        Bukkit.getScheduler().cancelTasks(plugin);
    		}
	    }, 5, 20 * 1);   
	}
}
	
	public void onPlayerSuccess(String nickToRemove) {		
	    if (plugin.onProverka.containsKey(nickToRemove)) {
	        Players base = this.plugin.getBase();
	        base.setVerifed(player);
	        plugin.onProverka.remove(nickToRemove);
	        plugin.noProverka.put(nickToRemove, 60); 
	        String SuccessMessage = plugin.config().getSuccessMessage();
	        player.sendMessage(SuccessMessage);
		    List<String> commandsPlayer = this.plugin.config().getCommandsPlayer(player);
		    List<String> commandsServer = this.plugin.config().getCommandsServer(player);
		    for (String command : commandsPlayer)
		      Bukkit.dispatchCommand((CommandSender)player, command); 
		    for (String command : commandsServer)
		      Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command); 
	        plugin.getServer().getConsoleSender().sendMessage("Проверка пройдена для игрока: " + nickToRemove);
			Boolean Lvl = Main.getInstance().getConfig().getBoolean("Minecraft.LevelEnable");
	        if(Lvl) {
	        player.setLevel(0);
	        }
	    } else {
	        plugin.getServer().getConsoleSender().sendMessage("Игрок с ником " + nickToRemove + " не найден в проверке.");
	        if(nickToRemove == null) return;
	    }
	}	
	   @EventHandler
	   public void onPlayerQuit(PlayerQuitEvent event) {
		 plugin.onProverka.remove(nick);
		 plugin.noProverka.put(nick, 1);
	     plugin.timer.remove(nick);
	     Boolean Lvl = Main.getInstance().getConfig().getBoolean("Minecraft.LevelEnable");
	     if(Lvl) {
	     player.setLevel(0);
	     	}
	    
	     }
	   
	   @EventHandler
	   public void onInventoryOpen(InventoryOpenEvent event) {
	 		if(plugin.onProverka.containsKey(nick)) {
	        event.setCancelled(true); 
	 		}
	   }
	   
	   @EventHandler
	   public void onPlayerMove(PlayerMoveEvent event) {
	 		if(plugin.onProverka.containsKey(nick)) {
		        event.setCancelled(true); 
		 		}
	   }
	   
	   @EventHandler
	   public void onPlayerInteract(PlayerInteractEvent event) {
	 		if(plugin.onProverka.containsKey(nick)) {
		        event.setCancelled(true); 
		 		}
	   }
	   
	   @EventHandler
	   public void onPlayerChat(AsyncPlayerChatEvent event) {
	 		if(plugin.onProverka.containsKey(nick)) {
		        event.setCancelled(true); 
		 		}
	   }
	   
	   @EventHandler
	   public void onInventoryClick(InventoryClickEvent event) {
	     if (event.getWhoClicked() instanceof Player) {
		 		if(plugin.onProverka.containsKey(nick)) {
			        event.setCancelled(true); 
		 		}
	   		} 
	   }
}
