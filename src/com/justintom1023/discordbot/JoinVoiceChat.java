package com.justintom1023.discordbot;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinVoiceChat extends ListenerAdapter {

	boolean enough = false;
	String channelId = "{INSERT CHANNEL ID HERE}";
	
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

		try {

			int memberCount = event.getChannelJoined().getMembers().size();
			VoiceChannel joinedChannel = event.getChannelJoined();

			if (!joinedChannel.getId().equals("{AFK CHANNEL ID GOES HERE}")) {

				if (memberCount == 1) {

					oneMemberInVoice(event, joinedChannel.getId());

				}

				else if (memberCount >= 2 && enough == false) {

					event.getGuild().getTextChannelById(channelId)
							.sendMessage("Two buddies are in a voice channel!").queue();
					
					enough = true;

				}

			}

		}

		catch (IllegalArgumentException | IllegalStateException e) {

			// handle this however you want
			
		}

	}
	
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

		try {

			VoiceChannel leftChannel = event.getChannelLeft();

			if (!leftChannel.getId().equals("{AFK CHANNEL ID GOES HERE}")) {
				
				int memberCount = leftChannel.getMembers().size();

				if (memberCount == 0) {

					renameWhenEmpty(event, leftChannel.getId());

				}

				if (memberCount < 2 && enough == true) {

					enough = false;

				}

			}

		}

		catch (IllegalArgumentException | IllegalStateException e) {

			// handle this however you want
		
		}

	}
	
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		
		try {
			
			VoiceChannel leftChannel = event.getChannelLeft();
			int memberCountJoined = event.getChannelJoined().getMembers().size();
			
			if (leftChannel.getMembers().size() == 0) {
				
				renameWhenEmpty(event, leftChannel.getId());
				
			}
			
			if (memberCountJoined == 1) {
				
				oneMemberInVoice(event, event.getChannelJoined().getId());
				
			}
			
		}
		
		catch (IllegalArgumentException | IllegalStateException e) {
			
			// handle this however you want
			
		}
		
	}
	
	private void oneMemberInVoice(GuildVoiceUpdateEvent event, String joinChannelId) {
		
		Map<String, String> channelNameMap = new HashMap<>();
		channelNameMap.put("{VOICE CHANNEL ID 1 GOES HERE}", "1");
		channelNameMap.put("{VOICE CHANNEL ID 2 GOES HERE}", "2");
		channelNameMap.put("{VOICE CHANNEL ID 3 GOES HERE}", "3");
						
		if (channelNameMap.containsKey(joinChannelId)) {
			
			event.getChannelJoined().getManager()
					.setName(channelNameMap.get(joinChannelId) + " - " + event.getMember().getUser().getName()).queue();
			
		}
		
	}
	
	private void renameWhenEmpty(GuildVoiceUpdateEvent event, String leftChannelId) {
		
		Map<String, String> channelNameMap = new HashMap<>();
		channelNameMap.put("{VOICE CHANNEL ID 1 GOES HERE}", "1");
		channelNameMap.put("{VOICE CHANNEL ID 2 GOES HERE}", "2");
		channelNameMap.put("{VOICE CHANNEL ID 3 GOES HERE}", "3");
		
		if (channelNameMap.containsKey(leftChannelId)) {
			
		    event.getChannelLeft().getManager().setName(channelNameMap.get(leftChannelId)).queue();
		    
		}
		
	}
	
}
