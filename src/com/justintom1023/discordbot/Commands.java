package com.justintom1023.discordbot;

import java.awt.Color;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Commands {

	public static void commands(GuildMessageReceivedEvent event, String messageSent) {

		if (messageSent.toLowerCase().startsWith("!cwis")) {
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Cwis");
			eb.setColor(new Color(130, 144, 149));
			eb.addField("!hello", "Says hello", false);
			eb.addField("!days", "Calculates the number of days between a specified date and the current date", false);
			eb.addField("!random", "Prints out a random number from 0 to 100", false);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			eb.clearFields();
			
			eb.setTitle("#movie-vote #movie-discussion only");
			eb.setColor(new Color(150, 214, 112));
			eb.setDescription("These commands can only be used in the movie night channels.");
			eb.addField("!suggest [MOVIE TITLE GOES HERE]", "Adds a movie to the suggestions list (e.g. !suggest Inception).", false);
			eb.addField("!random", "A random movie from the suggestions list is chosen.", false);
			eb.addField("!vote", "Lists all movies from the suggestions list. Buddies can vote by reacting to it with an emoji.", false);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();

		}

		else if (messageSent.toLowerCase().startsWith("!hello")) {

			event.getChannel().sendMessage("Hello!").queue();

		}

		else if (messageSent.toLowerCase().startsWith("!random")) {

			Random rand = new Random();
			int n = rand.nextInt(101);

			if (n <= 50) {

				event.getChannel().sendMessage("Bro, it's " + n + " degrees. Give me some takis!").queue();

			}

			else {

				event.getChannel().sendMessage("I like my chat above " + n + " degrees!").queue();

			}

		}
		
		else if (messageSent.toLowerCase().startsWith("!days")) {
			
			LocalDate startDate = LocalDate.of(2023, 7, 24);
			LocalDate currentDate = LocalDate.now();
			
			long daysSince = ChronoUnit.DAYS.between(startDate, currentDate);
			
			event.getChannel().sendMessage(daysSince + "").queue();
			
		}
		
		// commands that only work in specific text channels
		else if (event.getMessage().getTextChannel() == event.getGuild().getTextChannelById("{TEXT CHANNEL ID GOES HERE}")) {
		
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
