package com.justintom1023.discordbot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.justintom1023.discordbot.api.Item;
import com.justintom1023.discordbot.api.Json;
import com.justintom1023.discordbot.api.Snippet;
import com.justintom1023.discordbot.api.Video;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HondaFilter {
	
	public static void hondaFilter(GuildMessageReceivedEvent event, String messageSent) {

		try {

			String[] videoURL;
			String videoId;

			if (messageSent.contains("youtube")) {

				videoURL = messageSent.split("=");
				videoId = videoURL[1].substring(0, 11);

			}

			else {

				videoURL = messageSent.split("/");
				videoId = videoURL[3].substring(0, 11);

			}
			
			String key = ""; // key goes here

			URL url = new URL("https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId
					+ "&key=" + key);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) {

				System.out.println("HttpResponseCode: " + responseCode);

			}

			else {

				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(url.openStream());

				while (scanner.hasNext()) {

					informationString.append(scanner.nextLine());

				}

				scanner.close();
				
				JsonNode node = Json.parse(informationString.toString());
				Video video = Json.fromJson(node, Video.class);
				List<Item> items = video.getItems();
				Snippet snippet = items.get(0).getSnippet();

				String title = snippet.getTitle();
				String titleDescTags = "";

				if (snippet.getTags() != null) {

					for (String tags : snippet.getTags()) {

						titleDescTags += tags.toLowerCase();

					}

				}

				titleDescTags += title.toLowerCase() + snippet.getDescription().toLowerCase();

				if (titleDescTags.toLowerCase().contains("honda")) {

					String honda = "[member] shared **[video]**, a video about Honda!";
					event.getChannel().sendMessage(honda.replace("[video]", title).replace("[member]", event.getMember().getAsMention())).queue();

				}

			}

		} catch (Exception e) {

			System.out.println("YouTube Exception: " + messageSent);

		}

	}

}
