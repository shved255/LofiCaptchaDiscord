package Main.LofiCaptchaDiscord.listeners;

import java.util.List;
import java.util.Random;

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
import Main.LofiCaptchaDiscord.command.Players;

public class Listeners implements Listener {
    private Main plugin;
    private String code;
    private Player player;

    public Listeners(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        player = event.getPlayer();
        String nick = player.getName();
        Players base = this.plugin.getBase();
        code = generateCode();
        if (player == null || nick == null) return;
        if (!base.needVerifed(player)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> { 
                List<String> commandsPlayer = this.plugin.config().getCommandsPlayer(player);
                List<String> commandsServer = this.plugin.config().getCommandsServer(player);
                commandsPlayer.forEach(command -> Bukkit.dispatchCommand((CommandSender) player, command)); 
                commandsServer.forEach(command -> Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), command));
            }, 20 * 1); 
        } 
        
        if (base.needVerifed(player)) {
            plugin.codeMap.put(player, code);
            int time = plugin.config().getTime();
            plugin.timer.put(nick, time);
            Bukkit.getScheduler().runTaskLater(plugin, () -> { 
            player.kickPlayer(plugin.config().getKickMessage());
            }, time * 20);
            Boolean Lvl = plugin.config().getLvl();
            if (Lvl) {
                player.setLevel(plugin.config().getTime() + 1);
            }
            String styleString = plugin.config().getBarStyle();
            String colorString = plugin.config().getBarColor();
            BarColor barColor = BarColor.valueOf(colorString.toUpperCase());
            BarStyle barStyle = BarStyle.valueOf(styleString.toUpperCase());
            BossBar bar = Bukkit.getServer().createBossBar(plugin.config().getBarInfo(nick, player), barColor, barStyle);
            bar.addPlayer(player);
            plugin.onProverka.put(nick, 60);         
            
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (plugin.onProverka.containsKey(nick)) { 
                    player.sendMessage(plugin.config().getMessage(nick, player));
                    bar.setTitle(plugin.config().getBarInfo(nick, player));
                    bar.setProgress((double)plugin.timer.get(nick) / time);
                    plugin.timer.put(nick, plugin.timer.get(nick) - 1);
                    if (Lvl) {
                        player.setLevel(Math.max(0, player.getLevel() - 1)); 
                    }
                }
            }, 5, 20 * 1); 
            
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (plugin.noProverka.containsKey(nick)) {  
                    plugin.noProverka.remove(nick);
                    bar.removePlayer(player);
                    plugin.timer.remove(nick);
                    Bukkit.getScheduler().cancelTasks(plugin);
                }
            }, 5, 20 * 1);   
        }
    }
    
    private String generateCode() {
        int code = (new Random()).nextInt(999999);
        return String.format("%06d", new Object[] { Integer.valueOf(code) });
      }
    
    public void onPlayerSuccess(String nickToRemove, String code1) {  
    	Player player = Bukkit.getPlayer(nickToRemove);
    	if((plugin.codeMap.get(player)).equals(code1)) {
        if (plugin.onProverka.containsKey(nickToRemove)) {
            Players base = this.plugin.getBase();
            base.setVerifed(plugin.getServer().getPlayer(nickToRemove));
            plugin.onProverka.remove(nickToRemove);
            plugin.noProverka.put(nickToRemove, 60); 
            String SuccessMessage = plugin.config().getSuccessMessage();
            if (player == null) return;
                player.sendMessage(SuccessMessage);
                List<String> commandsPlayer = this.plugin.config().getCommandsPlayer(player);
                List<String> commandsServer = this.plugin.config().getCommandsServer(player);
                commandsPlayer.forEach(command -> Bukkit.dispatchCommand((CommandSender) player, command)); 
                commandsServer.forEach(command -> Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), command)); 
                plugin.getServer().getConsoleSender().sendMessage("Проверка пройдена для игрока: " + nickToRemove);
                Boolean Lvl = plugin.config().getLvl();
                if (Lvl) {
                    player.setLevel(0);
                }
            } else {
                plugin.getServer().getConsoleSender().sendMessage("Игрок с ником " + nickToRemove + " не найден в проверке.");
            }
    	} 
    }   

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            String nick = player.getName();
            plugin.onProverka.remove(nick);
            plugin.noProverka.put(nick, 1);
            plugin.timer.remove(nick);
            Boolean Lvl = plugin.config().getLvl();
            if (Lvl) {
                player.setLevel(0);
            }
        }
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (plugin.onProverka.containsKey(player.getName())) {
                event.setCancelled(true); 
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (plugin.onProverka.containsKey(player.getName())) {
                event.setCancelled(true); 
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (plugin.onProverka.containsKey(player.getName())) {
                event.setCancelled(true); 
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (plugin.onProverka.containsKey(player.getName())) {
                event.setCancelled(true); 
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (plugin.onProverka.containsKey(player.getName())) {
                event.setCancelled(true); 
            }
        }
    }
    public String getCode() {
    	return this.code;
    }
    
    public Player getPlayer() {
    	return this.player;
    }
}
