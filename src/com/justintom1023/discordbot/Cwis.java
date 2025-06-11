package com.justintom1023.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Cwis {

	public static JDABuilder builder;

	public static void main(String[] args) {

		try {
			
			String token = "<TOKEN>";

			JDA jda = JDABuilder.createDefault(token)
					.setChunkingFilter(ChunkingFilter.ALL)
					.setMemberCachePolicy(MemberCachePolicy.ALL)
					.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
				    
				    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
				    
				    .setCompression(Compression.NONE)
				    .setStatus(OnlineStatus.ONLINE)
				    .setActivity(Activity.playing("!cwis"))
				    
				    .addEventListeners(new CwisListener())
				    .addEventListeners(new JoinVoiceChat())
				    .build();

			jda.awaitReady();
			
//			GuildChannel channel = jda.getGuildChannelById("<TEXT_CHANNEL_ID>");
//
//	        if (channel instanceof ThreadChannel) {
//	        	
//	        	System.out.println("THREAD CHANNEL");
//	            ThreadChannel threadChannel = (ThreadChannel) channel;
//	            new ImageDownloader().downloadImagesFromHistory(threadChannel);
//
//	        }
//	        
//	        else if (channel instanceof MessageChannel) {
//
//	        	System.out.println("REGULAR TEXT CHANNEL");
//	        	MessageChannel messageChannel = (MessageChannel) channel;
//	        	new ImageDownloader().downloadImagesFromHistory(messageChannel);
//	        	
//	        }
			
		}
		
		catch (Exception e) {			
			e.printStackTrace();			
		}

	}

}
