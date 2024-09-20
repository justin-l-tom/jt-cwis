package com.justintom1023.discordbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Cwis {

	public static JDABuilder builder;

	public static void main(String[] args) throws LoginException {

		String token = "{TOKEN GOES HERE}"; // I use an environment variable

		builder = JDABuilder.createDefault(token).setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);

		builder.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);

		builder.setCompression(Compression.NONE);

		builder.setStatus(OnlineStatus.ONLINE);

		builder.setActivity(Activity.playing("!cwis"));

		registerListeners();

		builder.build();

	}

	public static void registerListeners() {

		builder.addEventListeners(new CwisListener());
		builder.addEventListeners(new JoinVoiceChat());

	}

}
