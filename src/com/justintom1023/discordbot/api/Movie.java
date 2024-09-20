package com.justintom1023.discordbot.api;

public class Movie {

	private int movieId;
	private String name;
	private int thumbsUp;
	private int thumbsDown;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getThumbsUp() {
		return thumbsUp;
	}
	
	public void setThumbsUp(int thumbsUp) {
		this.thumbsUp = thumbsUp;
	}
	
	public int getThumbsDown() {
		return thumbsDown;
	}
	
	public void setThumbsDown(int thumbsDown) {
		this.thumbsDown = thumbsDown;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
}
