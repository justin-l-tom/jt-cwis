package justintom1023.discordbot;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingWhenFriendsAreTalking extends ListenerAdapter {
	
	boolean enough = false;
	
	public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
		
		String afkId = ""; // afk channel id goes here
		String textChannelId = ""; // text channel id goes here
				
		if ((event.toString().contains("null->") || event.toString().contains("VC:AFK(" + afkId + ")->VC")) && !event.toString().contains("->VC:AFK")) {
			
			int memberCount = event.getChannelJoined().getMembers().size();
			
			if (memberCount >= 2 && enough == false) {
				
				event.getGuild().getTextChannelById(textChannelId).sendMessage("@here Friends are chatting in a voice channel!").queue();
				enough = true;
				
			}
			
		}
		
		else if ((event.toString().contains("->null") || event.toString().contains("->VC:AFK"))
				&& !event.toString().contains("null->VC:AFK")
				&& !event.toString().contains("VC:AFK(" + afkId + ")->null")) {
			
			int memberCount = event.getChannelLeft().getMembers().size();
			
			if (memberCount < 2 && enough == true) {
				
				enough = false;
				
			}
			
		}
		
		else if (!event.toString().contains("AFK")) {
			
			int memberCount1 = event.getChannelLeft().getMembers().size();
			int memberCount2 = event.getChannelJoined().getMembers().size();

			if (memberCount1 < 2 && enough == true) {
				
				enough = false;
				
			}
			
			if (memberCount2 >= 2 && enough == false) {
				
				event.getGuild().getTextChannelById(textChannelId).sendMessage("@here Friends are chatting in a voice channel!").queue();
				enough = true;
				
			}
			
		}

	}

}
