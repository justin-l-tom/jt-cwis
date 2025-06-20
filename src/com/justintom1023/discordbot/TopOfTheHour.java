package com.justintom1023.discordbot;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TopOfTheHour extends ListenerAdapter {

	String serverId = "<SERVER_ID>";
	String textChannelId = "<TEXT_CHANNEL_ID>";

	public void onReady(@NotNull ReadyEvent event) {

		Timer timer = new Timer();
		long currentTime = System.currentTimeMillis();
		long delay = 60 * 60 * 1000 - (currentTime % (60 * 60 * 1000));
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				String message = "Start rolling!";

				Guild guild = event.getJDA().getGuildById(serverId);
				guild.getTextChannelById(textChannelId).sendMessage(message).queue();

			}

		}, new Date(System.currentTimeMillis() + delay), 60 * 60 * 1000);

	}

}
