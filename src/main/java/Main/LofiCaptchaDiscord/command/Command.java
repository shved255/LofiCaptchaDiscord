package Main.LofiCaptchaDiscord.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import Main.LofiCaptchaDiscord.Main;
import Main.LofiCaptchaDiscord.listeners.Listeners;

public class Command implements CommandExecutor {
	@SuppressWarnings("unused")
	private Main plugin;
	private Listeners listener;
	private String nickToRemove;
	private String code1;
	
	public Command(Main plugin) {
		this.plugin = plugin;
		listener = plugin.getListener();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {			
	    if (!(sender instanceof ConsoleCommandSender)) {
	        sender.sendMessage("Команда доступна только из консоли!");
	        return true;
	    }
	    if (args.length < 1) {
	        sender.sendMessage("Укажите ник игрока для удаления.");
	        return true;
	    }
	    nickToRemove = args[0];
	    code1 = args[1];
	    if (listener == null) return true;
	    listener.onPlayerSuccess(nickToRemove, code1);
		return true;	  
	}
	   
}