package com.justintom1023.discordbot.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Snippet {

	private String title;
	
	private String description;
	
	private List<String> tags;
	
	@JsonProperty("resourceId")
	private ResourceId resourceId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public ResourceId getResourceId() {
		return resourceId;
	}

	public void setResourceId(ResourceId resourceId) {
		this.resourceId = resourceId;
	}
	
}
