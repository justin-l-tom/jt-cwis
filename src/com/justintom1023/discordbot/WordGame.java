package com.justintom1023.discordbot;

import java.awt.Color;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WordGame {

	static int start = -1;
	static String word = "FRIEND"; // hard-coded word for this demo
	static StringBuilder squares = new StringBuilder();
	static StringBuilder letters = new StringBuilder();
	static HttpClient httpClient = HttpClient.newHttpClient();
	static String KEY = "<API_KEY>";
	static String wordleTextChannelId = "<TEXT_CHANNEL_ID>";
	
	public static void wordle(MessageReceivedEvent event) {
		
		try {
			
			Map<Character, Integer> map = characterCount(word);
			TextChannel textChannel = event.getGuild().getTextChannelById(wordleTextChannelId);
	    	String messageSent = event.getMessage().getContentRaw().toUpperCase();
	    	
			boolean rightChannel = event.getMessage().getGuildChannel() == textChannel &&
					!event.getAuthor().getName().equals("Cwis");
	    	
			if (start != 200  && rightChannel) {				
				event.getMessage().delete().queue();				
			}
			
			if (rightChannel && start == -1) {

				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Word Game");
				
				eb.setDescription("This game is like Wordle. Today's word is " + word.length() + " letters long. "
						+ "Buddies work together to guess the same word, but attempts are shared.\n\n"
						+ "Enter a " + word.length() + " letter word!");
				
				eb.setColor(new Color(214, 175, 18));
				event.getChannel().sendMessageEmbeds(eb.build()).queue();
				start++;

			}
	    	
	    	else if (rightChannel && start < 6) {
	    		
	    		if (messageSent.length() == word.length() && !messageSent.contains(" ")) {
	    			
	    			if (messageSent.equals(word)) {
	    				
	    				String attemptByMember = "Attempt {i} / 6 by {member}"
	    						.replace("{i}", Integer.toString(start + 1))
	    						.replace("{member}", event.getMember().getAsMention());
	    				
	    				for (int i = 0; i < word.length(); i++) {	    					
	    					letters.append(":regional_indicator_" + messageSent.toLowerCase().charAt(i) + ": ");	    					
	    				}
	    				
	    				textChannel.sendMessage(attemptByMember + "\n" + letters + "\n" + ":green_square: ".repeat(word.length()) + "\n-"
	    						+ "\nCongratulations! " + word + " was the word of the day! ").queue();
	    				
	    				start = 200;
	    				
	    			}
	    			
	    			// word != guess
	    			else {
	    				
	    				HttpRequest getRequest = HttpRequest.newBuilder()
								.uri(new URI("https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
										+ messageSent + "?key=" + KEY))
								.build();
						
						HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
						
						// Checks if the word is valid.
						if (getResponse.body().contains("\"id\":")) {
	    				
							String[] evaluate = new String[word.length()];
														
							// Green Matches
							for (int i = 0; i < word.length(); i++) {
								
								char guessedLetter = messageSent.charAt(i);
								
								if (word.charAt(i) == guessedLetter) {
									
									evaluate[i] = "Green";
									map.put(guessedLetter, map.get(guessedLetter) - 1);
								
								}
								
							}
							
							for (int i = 0; i < word.length(); i++) {
								
								char guessedLetter = messageSent.charAt(i);
								
								if (evaluate[i] == null) {
																	
									if (map.containsKey(guessedLetter) && map.get(guessedLetter) > 0) {
										
										evaluate[i] = "Yellow";
										map.put(guessedLetter, map.get(guessedLetter) - 1);
										
									}
									
									else evaluate[i] = "Red";
									
								}
								
							}
							
		    				for (int i = 0; i < word.length(); i++) {
		    					
		    					letters.append(":regional_indicator_" + messageSent.toLowerCase().charAt(i) + ": ");
		    					
								if (evaluate[i].equals("Green")) {
									squares.append(":green_square: ");
								}

								else if (evaluate[i].equals("Yellow")) {
									squares.append(":yellow_square: ");
								}

								if (evaluate[i].equals("Red")) {
									squares.append(":red_square: ");
								}
		    					
		    				}
		    				
		    				String squaresConverted = squares.toString();
		    				start++;
		    				
		    				String attemptByMember = "Attempt {i} / 6 by {member}"
		    						.replace("{i}", Integer.toString(start))
		    						.replace("{member}", event.getMember().getAsMention());
		    				
		    				textChannel.sendMessage(attemptByMember + "\n" + letters + "\n" + squaresConverted + "\n-").queue();
		    				
		    				squares = new StringBuilder();
		    				letters = new StringBuilder();
		    				
		    				if (start == 6) {
		    					
		    					textChannel.sendMessage("All attempts have been used. " + word + " was the word of the day. ").queue();		    					
		    					start = 200;
		    					
		    				}
		    				
	    				}
						
						else {
							
							textChannel.sendMessage("{member} {word} is not a valid word. Please try again."
			    					.replace("{member}", event.getMember().getAsMention())
			    					.replace("{word}", messageSent)).queue();
							
						}
	    				
	    			}
	    			
	    		}
	    		
	    		else if (messageSent.length() != word.length() && !messageSent.contains(" ")) {
	    			
	    			textChannel.sendMessage("{member} {word} is not {n} letters long. Please try again."
	    					.replace("{member}", event.getMember().getAsMention())
	    					.replace("{word}", messageSent)
	    					.replace("{n}", Integer.toString(word.length()))).queue();
	    			
	    		}
	    		
	    	}
			
		}
		
		catch (Exception e) {
			
			// handle this however you want
			event.getJDA().getUserById("<USER_ID>").openPrivateChannel()
					.flatMap(channel -> channel.sendMessage("Word Game: " + e)).queue();
			
		}
    	
    }
	
	private static Map<Character, Integer> characterCount(String word) {
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		
		for (char c : word.toCharArray()) {
			
			if (map.containsKey(c)) {
				map.put(c, map.get(c) + 1);
			}
			
			else {
				map.put(c, 1);
			}
			
		}
		
		return map;
		
	}
	
}
