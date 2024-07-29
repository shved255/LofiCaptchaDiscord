package Main.LofiCaptchaDiscord;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LogHandler extends ListenerAdapter {
	
   @SuppressWarnings("unused")
private Main plugin;
   private String nick;
   boolean isonline; 
   private Player player;
   private Players base;
   private String PlayerNotOnline;
   private String PlayerSuccessDiscord;
   private String PlayerInBase;
   
   public void checkPlayerOnline(String nick) {
	    this.base = Main.getInstance().getBase();
		isonline = false;
	    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
	        if (player.getName().equalsIgnoreCase(nick)) {
	            isonline = true;
	            break; 
	        }  
	    }
  }
   
   @Override	
   public void onModalInteraction(ModalInteractionEvent event) {
	  this.base = Main.getInstance().getBase();
      if (event.getModalId().equals("verif-modal")) {
             	nick = event.getValue("verif-nick").getAsString(); 
                player = Bukkit.getPlayer(nick);
                if(nick == null) {   
                PlayerNotOnline	= Main.getInstance().getConfig().getString("Discord.PlayerNotOnline");
                event.reply(PlayerNotOnline).setEphemeral(true).queue();
                return;
                }
             	if(player == null) {
                PlayerNotOnline	= Main.getInstance().getConfig().getString("Discord.PlayerNotOnline");
                event.reply(PlayerNotOnline).setEphemeral(true).queue();
             	return;
             	}
                if(player == null) return;    
                if(nick == null) return;     
      			if(!base.needVerifed(player)) {
      			PlayerInBase = Main.getInstance().getConfig().getString("Discord.PlayerInBase");
      			event.reply(PlayerInBase).setEphemeral(true).queue();
      			return;
      				}
      			}
                if(player == null) return;  
                if(nick == null) return;     
                if(player.isOnline()) {
                if(base.needVerifed(player)) {
                PlayerSuccessDiscord = Main.getInstance().getConfig().getString("Discord.PlayerSuccessDiscord");
                event.reply(PlayerSuccessDiscord).setEphemeral(true).queue(); 
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "loficaptcha " + nick);	
                			});
                		}
                	} 
                }            
   public String getNick() {
	   return this.nick;
   }
}
