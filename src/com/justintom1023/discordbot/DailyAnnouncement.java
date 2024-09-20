package com.justintom1023.discordbot;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DailyAnnouncement extends ListenerAdapter {

	public void onReady(@NotNull ReadyEvent event) {

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));

		ZonedDateTime nextEvent = now.withHour(20).withMinute(00).withSecond(0);

		if (now.compareTo(nextEvent) > 0) {
			
			nextEvent = nextEvent.plusDays(1);
			
		}

		Duration durationUntilEvent = Duration.between(now, nextEvent);

		long initialDelayEvent = durationUntilEvent.getSeconds();

		ScheduledExecutorService schedulerEvent = Executors.newScheduledThreadPool(1);
		
		schedulerEvent.scheduleAtFixedRate(() -> {

			String message = "Daily message!";
			JDA jda = event.getJDA();
			
			for (Guild guild : jda.getGuilds()) {
				
				guild.getDefaultChannel().sendMessage(message).queue();
				
			}

		}, initialDelayEvent, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

	}

}
