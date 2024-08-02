package Main.LofiCaptchaDiscord.discord;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Main.LofiCaptchaDiscord.Main;
import Main.LofiCaptchaDiscord.command.Players;

public class LogHandler extends ListenerAdapter {
	
   private String nick;
   private String code;
   boolean isonline; 
   private Player player;
   private Players base;
   private String playerNotOnline;
   private String playerSuccessDiscord;
   private String playerInBase;
   private String wrongMessage;
	
   @Override	
   public void onModalInteraction(ModalInteractionEvent event) {
	  this.base = Main.getInstance().getBase();
      if (event.getModalId().equals("verif-modal")) {
             	nick = event.getValue("verif-nick").getAsString(); 
             	code = event.getValue("verif-code").getAsString();
                player = Bukkit.getPlayer(nick);
                if(nick == null) {   
                playerNotOnline	= Main.getInstance().config().getPlayerIsNotOnline();
                event.reply(playerNotOnline).setEphemeral(true).queue();
                return;
                }
             	if(player == null) {
                playerNotOnline	= Main.getInstance().config().getPlayerIsNotOnline();
                event.reply(playerNotOnline).setEphemeral(true).queue();
             	return;
             	}
                if(player == null) return;    
                if(nick == null) return;     
      			if(!base.needVerifed(player)) {
      			playerInBase = Main.getInstance().config().getPlayerInBase();
      			event.reply(playerInBase).setEphemeral(true).queue();
      			return;
      				}
      			}
      			Player player1 = Main.getInstance().getListener().getPlayer();
				if(!code.equals(Main.getInstance().codeMap.get(player1))) {
				wrongMessage = Main.getInstance().config().getWrongMessage();
				event.reply(wrongMessage).setEphemeral(true).queue();
				return;
				}
                if(player == null) return;  
                if(nick == null) return; 
                if(player.isOnline()) {
                if(base.needVerifed(player)) {
                playerSuccessDiscord = Main.getInstance().config().getPlayerSuccessDiscord();
                event.reply(playerSuccessDiscord).setEphemeral(true).queue(); 
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "loficaptcha " + nick + " " + code);
                			});
                		}
                	}
                } 
   
   public String getNick() {
	   return this.nick;
   }
}
