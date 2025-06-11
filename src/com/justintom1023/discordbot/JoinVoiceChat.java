package com.justintom1023.discordbot;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinVoiceChat extends ListenerAdapter {

	boolean enough = false;
	String textChannelId = "<TEXT_CHANNEL_ID>";
	static String botDeployerUserId = "<USER_ID>";
	static String afkChannelId = "<AFK_CHANNEL_ID>";
	
	public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

		try {
			
			if (event.getChannelJoined() != null && event.getChannelLeft() != null) {
				
				AudioChannel leftChannel = event.getChannelLeft();
				int memberCountJoined = event.getChannelJoined().getMembers().size();
				
				if (leftChannel.getMembers().size() == 0) {					
					renameWhenEmpty(event, leftChannel.getId());					
				}
				
				if (memberCountJoined == 1) {					
					oneMemberInVoice(event, event.getChannelJoined().getId());					
				}
				
			}
			
			if (event.getChannelJoined() != null) {
				
				int memberCount = event.getChannelJoined().getMembers().size();
				AudioChannel joinedChannel = event.getChannelJoined();

				if (!joinedChannel.getId().equals(afkChannelId)) {

				
					if (memberCount == 1) {
						oneMemberInVoice(event, joinedChannel.getId());
					}

					else if (memberCount >= 2 && enough == false) {

						event.getGuild().getTextChannelById(textChannelId).sendMessage("Two buddies are in a voice channel!")
								.queue();						
						enough = true;

					}

				}
				
			}
			
			else if (event.getChannelLeft() != null) {
				
				AudioChannel leftChannel = event.getChannelLeft();

				if (!leftChannel.getId().equals(afkChannelId)) {
					
					int memberCount = leftChannel.getMembers().size();

					if (memberCount == 0) {
						renameWhenEmpty(event, leftChannel.getId());
					}

					if (memberCount < 2 && enough == true) {
						enough = false;
					}

				}
				
			}

		}

		catch (IllegalArgumentException | IllegalStateException e) {
			event.getJDA().getUserById(botDeployerUserId).openPrivateChannel()
					.flatMap(channel -> channel.sendMessage("onGuildVoiceUpdate: " + e)).queue();			
		}

	}
	
	private void oneMemberInVoice(GuildVoiceUpdateEvent event, String joinChannelId) {
		
		Map<String, String> channelNameMap = new HashMap<>();
		channelNameMap.put("<VOICE_CHANNEL_ID>", "1");
		channelNameMap.put("<VOICE_CHANNEL_ID>", "2");
		channelNameMap.put("<VOICE_CHANNEL_ID>", "3");
						
		if (channelNameMap.containsKey(joinChannelId)) {			
			event.getChannelJoined().getManager()
					.setName(channelNameMap.get(joinChannelId) + " - " + event.getMember().getUser().getName()).queue();			
		}
		
	}
	
	private void renameWhenEmpty(GuildVoiceUpdateEvent event, String leftChannelId) {
		
		Map<String, String> channelNameMap = new HashMap<>();
		channelNameMap.put("<VOICE_CHANNEL_ID>", "1");
		channelNameMap.put("<VOICE_CHANNEL_ID>", "2");
		channelNameMap.put("<VOICE_CHANNEL_ID>", "3");
		
		if (channelNameMap.containsKey(leftChannelId)) {			
		    event.getChannelLeft().getManager().setName(channelNameMap.get(leftChannelId)).queue();		    
		}
		
	}
	
}
