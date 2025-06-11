package com.justintom1023.discordbot;

import java.awt.Color;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.justintom1023.discordbot.api.Movie;
import com.justintom1023.discordbot.api.MovieSearch;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MovieNight extends ListenerAdapter {

	static HttpClient httpClient = HttpClient.newHttpClient();
	static Gson gson = new Gson();
	
	public static void suggest(MessageReceivedEvent event, String messageSent) {

		try {
									
			String movieName = messageSent.split(" ", 2)[1].toUpperCase();
			
			HttpRequest getRequest = HttpRequest.newBuilder()
					.uri(new URI("http://localhost:8080/api-movies/search/findByName?value="
							+ movieName.replace(" ", "%20")))
					.build();
			
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());

			MovieSearch movieSearch = gson.fromJson(getResponse.body(), MovieSearch.class);
			
			// Search if the movie exists.
			if (movieSearch != null) {		

				String retrievedMovieUrl = movieSearch.get_links().getMovie().getHref().replace("api-movies",
						"app-api/movies");
				
				// Get the movie.
				getRequest = HttpRequest.newBuilder().uri(new URI(retrievedMovieUrl)).build();
				getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
				
				Movie movie = gson.fromJson(getResponse.body(), Movie.class);
				
				event.getChannel().sendMessage(movie.getName() + " has already been suggested.").queue();
				
			}
			
			// Create the movie if it doesn't exist.
			else {
											
				addMovie(movieName);
				event.getChannel().sendMessage(movieName + " has been added to the suggestions list.").queue();
				
			}

		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			event.getChannel().sendMessage("Not a valid movie.").queue();
		}

		catch (URISyntaxException | IOException | InterruptedException e) {
			// handle this however you want
		}
		
	}
	
	public static void voteMovie(MessageReceivedEvent event, String messageSent) {
		
		try {
		
			HttpRequest getRequest = HttpRequest.newBuilder()
					.uri(new URI("http://localhost:8080/app-api/movies"))
					.build();
			
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			
			Movie[] moviesArr = gson.fromJson(getResponse.body(), Movie[].class);
			List<Movie> movies = Arrays.asList(moviesArr);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Vote");
			eb.setColor(new Color(93, 181, 128));
			
			for (int i = 0; i < movies.size(); i++) {
				
				String movieName = movies.get(i).getName();
				eb.addField(Util.emojis[i] + " " + movieName, "", false);
	
			}
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue(message -> {
				
				for (int i = 0; i < movies.size(); i++) {					
					message.addReaction(Emoji.fromUnicode(Util.emojis[i])).queue();					
				}
				
			});
			
		}
				
		catch (URISyntaxException | IOException | InterruptedException e) {
			// handle this however you want
		}
		
	}
	
	public static void randomMovie(MessageReceivedEvent event, String messageSent) {
		
		try {
			
			HttpRequest getRequest = HttpRequest.newBuilder()
					.uri(new URI("http://localhost:8080/app-api/movies"))
					.build();
			
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			
			Movie[] moviesArr = gson.fromJson(getResponse.body(), Movie[].class);
			List<Movie> movies = Arrays.asList(moviesArr);
			
			if (movies.size() == 0) {
				
				event.getChannel().sendMessage(
						"The suggestions list is currently empty. Use the !suggest command to add movies to the suggestions list.")
						.queue();
				
			}
			
			else {
				
				Random rand = new Random();
				int n = rand.nextInt(movies.size());
				
				String movieName = movies.get(n).getName();
				event.getChannel().sendMessage(movieName).queue();
				
			}
			
		}
				
		catch (URISyntaxException | IOException | InterruptedException e) {
			// handle this however you want
		}
		
	}

	private static String[] getHeaders() throws URISyntaxException, IOException, InterruptedException {

		String encoding = Base64.getEncoder().encodeToString(("<BACKEND_APP_USERNAME>:<BACKEND_APP_PASSWORD>").getBytes());

		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:8080/login"))
				.build();

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());

		String csrfToken = StringUtils.substringBetween(getResponse.body(), "<input type=\"hidden\" name=\"_csrf\" value=\"", "\"/>");
		
		System.out.println(csrfToken);

		String cookie = StringUtils.substringBefore(getResponse.headers().allValues("set-cookie").get(0), ";");

		String[] headers = { csrfToken, encoding, cookie };
		
		return headers;

	}
	
	private static void addMovie(String movieName) throws URISyntaxException, IOException, InterruptedException {
		
		String[] headers = getHeaders();

		Gson gson = new Gson();
		Movie movie = new Movie();
		movie.setName(movieName);
		String jsonRequest = gson.toJson(movie);

		HttpRequest postRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:8080/app-api/movies"))
				.header("Cookie", headers[2])
				.header("Content-Type", "application/json")
				.header("Authorization", "Basic " + headers[1])
				.header("x-csrf-token", headers[0])
				.POST(BodyPublishers.ofString(jsonRequest)).build();

		HttpClient httpClient = HttpClient.newHttpClient();
		httpClient.send(postRequest, BodyHandlers.ofString());
		
	}
	
	public static HttpRequest getPatchRequest(String url, String[] headers, String jsonRequest) throws URISyntaxException {
		
		HttpRequest patchRequest = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Cookie", headers[2])
				.header("Content-Type", "application/json")
				.header("Authorization", "Basic " + headers[1])
				.header("x-csrf-token", headers[0])
				.method("PATCH", BodyPublishers.ofString(jsonRequest))
				.build();
		
		return patchRequest;
		
	}

}
