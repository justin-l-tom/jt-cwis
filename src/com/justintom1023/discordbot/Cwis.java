package com.justintom1023.discordbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Cwis {

	public static JDABuilder builder;
	
	public static String prefix;
	
	public static void main(String[] args) throws LoginException {
		
		String token = ""; // token goes here
		
		prefix = "!";
		
		builder = JDABuilder.createDefault(token);
		
		builder.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
		
		builder.setCompression(Compression.NONE);
		
		builder.setStatus(OnlineStatus.ONLINE);
		
		builder.setActivity(Activity.playing("!cwis"));
				
		registerListeners();
		
		builder.build();
		
	}
	
	public static void registerListeners() {
		
		builder.addEventListeners(new CwisListener());
		builder.addEventListeners(new PingWhenFriendsAreTalking());
		builder.addEventListeners(new WarmingUpTheChat());
		
	}
	
}
