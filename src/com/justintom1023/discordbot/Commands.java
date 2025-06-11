package com.justintom1023.discordbot;

import java.awt.Color;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Commands {

	static String movieNightChannelId = "<CHANNEL_ID>";
	static String textChannelURL = "<CHANNEL_URL>";
	
	public static void commands(MessageReceivedEvent event, String messageSent) {

		if (messageSent.toLowerCase().startsWith("!cwis")) {
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Cwis");
			eb.setColor(new Color(130, 144, 149));
			eb.addField("!hello", "Says hello", false);
			eb.addField("!days", "Calculates the number of days between a specified date and the current date", false);
			eb.addField("!random", "Prints out a random number from 0 to 100", false);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			eb.clearFields();
			
			eb.setTitle("#movie-vote #movie-discussion only", textChannelURL);
			eb.setColor(new Color(150, 214, 112));
			eb.setDescription("These commands can only be used in the #movie-night chat.");
			eb.addField("!suggest [MOVIE TITLE GOES HERE]", "Adds a movie to the suggestions list (e.g. !suggest Inception).", false);
			eb.addField("!random", "A random movie from the suggestions list is chosen.", false);
			eb.addField("!vote", "Lists all movies from the suggestions list. Buddies can vote by reacting to the message with the correct emoji.", false);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();

		}

		else if (messageSent.toLowerCase().startsWith("!hello")) {
			event.getChannel().sendMessage("Hello!").queue();
		}

		else if (messageSent.toLowerCase().startsWith("!random")) {

			Random rand = new Random();
			int n = rand.nextInt(101);

			if (n <= 50) {
				event.getChannel().sendMessage("It's " + n + " degrees. Give me some takis!").queue();
			}

			else {
				event.getChannel().sendMessage("I like my chat above " + n + " degrees!").queue();
			}

		}

		else if (messageSent.toLowerCase().startsWith("!days")) {
			
			LocalDate startDate = LocalDate.of(2023, 7, 24);
			LocalDate currentDate = LocalDate.now();
			
			long daysSince = ChronoUnit.DAYS.between(startDate, currentDate);
			
			event.getChannel().sendMessage(Long.toString(daysSince)).queue();
			
		}
		
		else if (event.getMessage().getGuildChannel() == event.getGuild().getTextChannelById(movieNightChannelId)) {
		
			if (messageSent.toLowerCase().startsWith("!suggest")) {				
				MovieNight.suggest(event, messageSent);				
			}
			
			else if (messageSent.toLowerCase().startsWith("!vote")) {				
				MovieNight.voteMovie(event, messageSent);				
			}
			
			else if (messageSent.toLowerCase().startsWith("!random")) {				
				MovieNight.randomMovie(event, messageSent);				
			}

		}

	}

}
