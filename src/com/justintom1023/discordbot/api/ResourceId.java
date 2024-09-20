package com.justintom1023.discordbot.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceId {
	
	@JsonProperty("videoId")
	private String videoId;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
}
