package com.justintom1023.discordbot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CwisListener extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		String messageSent = event.getMessage().getContentRaw();
						
		try {
			
			if (messageSent.contains("https://twitter.com/")) {

				messageSent = messageSent.replace("twitter.com", "vxtwitter.com");
				event.getChannel().sendMessage(messageSent).queue();
				

			}
			
			else if (messageSent.contains("https://x.com/")) {

				messageSent = messageSent.replace("x.com", "vxtwitter.com");
				event.getChannel().sendMessage(messageSent).queue();

			}
			
			if (messageSent.contains("youtube") || messageSent.contains("youtu.be")) {

				GrabYouTubeData.grabYouTubeData(event, messageSent);

			}

			else if (messageSent.startsWith("!")) {

				Commands.commands(event, messageSent);

			}

		}

		catch (StringIndexOutOfBoundsException e) {
			
			// handle this however you want (this usually occurs when an image is posted)
			
		}

	}

}
