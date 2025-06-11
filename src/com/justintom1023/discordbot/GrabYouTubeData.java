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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GrabYouTubeData {
	
	static String botDeployerUserId = "<USER_ID>";

	public static void grabYouTubeData(MessageReceivedEvent event, String messageSent) {

		try {

			String videoId;

			if (messageSent.contains("youtube.com/shorts")) {
				videoId = getVideoId(messageSent, "/", 4);
			}

			else if (messageSent.contains("youtube")) {
				videoId = getVideoId(messageSent, "=", 1);
			}

			else {
				videoId = getVideoId(messageSent, "/", 3);
			}
			
			HttpClient httpClient = HttpClient.newHttpClient();

			String key = "<KEY>";
			HttpRequest getRequest = HttpRequest.newBuilder()
					.uri(new URI("https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId
							+ "&key=" + key))
					.build();

			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			int responseCode = getResponse.statusCode();

			if (responseCode != 200) {

				event.getJDA().getUserById(botDeployerUserId).openPrivateChannel()
						.flatMap(channel -> channel.sendMessage("grabYouTubeData: " + responseCode)).queue();

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
				
				String[] keywords = {"example", "tags"};
				
				for (int i = 0; i < keywords.length; i++) {
					
					if (titleDescTags.matches("(?s).*\\b" + keywords[i] + "\\b.*")) {
						
						String message = "I just watched **{video}**. It was great!";				
						event.getChannel().sendMessage(message.replace("{video}", title)).queue();
						break;
						
					}
					
				}

			}

		}
		
		catch (Exception e) {
			event.getJDA().getUserById(botDeployerUserId).openPrivateChannel()
					.flatMap(channel -> channel.sendMessage("grabYouTubeData: " + messageSent + " " + e)).queue();			
		}

	}
	
	private static String getVideoId(String messageSent, String splitter, int i) {
		
		String[] videoURL = messageSent.split(splitter);
		return videoURL[i].substring(0, 11);
		
	}

}
