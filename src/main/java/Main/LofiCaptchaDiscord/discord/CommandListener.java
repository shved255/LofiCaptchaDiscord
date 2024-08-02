package Main.LofiCaptchaDiscord.discord;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import org.bukkit.entity.Player;

import Main.LofiCaptchaDiscord.Main;

public class CommandListener extends ListenerAdapter {
	private Player player;
	@SuppressWarnings("unused")
	private Main plugin; 
	private TextInput nick1;
	private TextInput code;
	
   @Override
   public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	     if (event.getName().equals("верификация")) {	     
         Button form = Button.danger("open-form", "Пройти верификацию").withStyle(ButtonStyle.PRIMARY).withEmoji(Emoji.fromUnicode("\ud83d\udccb"));
         String embedTitle = Main.getInstance().config().getEmbedTitle();
         String embedDescription =  Main.getInstance().config().getEmbedDescription();
         Color embedColor = Color.decode(Main.getInstance().config().getEmbedColor());
         EmbedBuilder embedBuilder = (new EmbedBuilder()).setTitle(embedTitle).setDescription(embedDescription).setColor(embedColor);
         ((MessageCreateAction)event.getChannel().sendMessageEmbeds(embedBuilder.build(), new MessageEmbed[0]).setComponents(new LayoutComponent[]{ActionRow.of(new ItemComponent[]{form})})).queue();
         event.reply(Main.getInstance().config().getSuccessSendPacket()).setEphemeral(true).queue();
      }

   }
   @Override
   public void onButtonInteraction(ButtonInteractionEvent event) {
      if(event.getComponentId().equals("open-form")) {
    	 boolean goyda = true;
         if (goyda) {
            nick1 = TextInput.create("verif-nick", "Ваш ник", TextInputStyle.SHORT).setMinLength(3).setMaxLength(16).setRequired(true).build();   
            code = TextInput.create("verif-code", "Ваш код", TextInputStyle.SHORT).setMinLength(0).setMaxLength(16).setRequired(true).build();
            Modal modal = Modal.create("verif-modal", "Верификация").addComponents(new LayoutComponent[]{ActionRow.of(new ItemComponent[]{nick1})}).addComponents(new LayoutComponent[]{ActionRow.of(new ItemComponent[]{code})}).build();
            event.replyModal(modal).queue();          
         }
      }
   }
   public Player getPlayer() {
	   return this.player;
   }
}
  
