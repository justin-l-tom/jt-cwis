package com.justintom1023.discordbot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import com.google.gson.Gson;
import com.justintom1023.discordbot.api.Item;
import com.justintom1023.discordbot.api.Snippet;
import com.justintom1023.discordbot.api.Video;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GrabYouTubeData {

	public static void grabYouTubeData(GuildMessageReceivedEvent event, String messageSent) {

		try {

			String[] videoURL;
			String videoId;

			if (messageSent.contains("youtube.com/shorts")) {

				videoURL = messageSent.split("/");
				videoId = videoURL[4].substring(0, 11);

			}
			
			else if (messageSent.contains("youtube")) {

				videoURL = messageSent.split("=");
				videoId = videoURL[1].substring(0, 11);

			}

			else {

				videoURL = messageSent.split("/");
				videoId = videoURL[3].substring(0, 11);

			}
			
			HttpClient httpClient = HttpClient.newHttpClient();

			HttpRequest getRequest = HttpRequest.newBuilder()
					.uri(new URI("https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId
							+ "&key={KEY GOES HERE}")) // I use an environment variable
					.build();

			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			int responseCode = getResponse.statusCode();

			if (responseCode != 200) {

				// handle this however you want

			}

			else {

				Gson gson = new Gson();
				
				Video video = gson.fromJson(getResponse.body(), Video.class);
				List<Item> items = video.getItems();
				Snippet snippet = items.get(0).getSnippet();
				String title = snippet.getTitle();
				
				String titleDescTags = title.toLowerCase() + snippet.getDescription().toLowerCase();

				if (snippet.getTags() != null) {

					for (String tags : snippet.getTags()) {

						titleDescTags += tags.toLowerCase();

					}

				}
				
				String[] keywords = {"honda", "toyota"};
				
				// This is just an example of what you can do. Get creative!
				for (int i = 0; i < keywords.length; i++) {
					
					if (titleDescTags.matches("(?s).*\\b" + keywords[i] + "\\b.*")) {
						
						String botMessage = "I just watched **{video}**. What a great video!";				
						event.getChannel().sendMessage(botMessage.replace("{video}", title)).queue();
						break;
						
					}
					
				}

			}

		}
		
		catch (Exception e) {

			// handle this however you want
			
		}

	}

}
