package com.justintom1023.discordbot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class ImageDownloader {
	
	static String messageChannelMessageId = "<MESSAGE_CHANNEL_MESSAGE_ID>";
	static String threadChannelMessageId = "<THREAD_CHANNEL_MESSAGE_ID>";
	static String path = "<PATH>";
	
	public void downloadImagesFromHistory(MessageChannel channel) {

		channel.getHistoryAfter(messageChannelMessageId, 1).queue(history -> {
			
			List<Message> messages = history.getRetrievedHistory();
			
			int i = 99;
			
            for (Message message : messages) {

            	int j = 1;
            	
            	for (Attachment attachment : message.getAttachments()) {

                    if (attachment.isImage()) {

                    	Path filePath = Paths.get(path, i + " " + j + "." + attachment.getFileExtension());

						attachment.getProxy().downloadToPath(filePath).thenAccept(downloadedPath -> {

							System.out.println("Image saved to: " + downloadedPath.toAbsolutePath());  
                             
                         }).exceptionally(ex -> {
                        	 
                             System.err.println("Failed to download image: " + ex.getMessage());
                             return null;
                             
                         });
                    }
                    
                    j++;
                    
                }
            	
            	i--;
                
            }
            
        }, failure -> {        	
            System.err.println("Failed to retrieve messages: " + failure.getMessage());            
        });
        
    }
	
	public void downloadImagesFromHistory(ThreadChannel channel) {

		channel.getHistoryAfter(threadChannelMessageId, 1).queue(history -> {
			
			List<Message> messages = history.getRetrievedHistory();
			
			int i = 99;
			
            for (Message message : messages) {

            	int j = 1;
            	
            	for (Attachment attachment : message.getAttachments()) {

                    if (attachment.isImage()) {

                    	Path filePath = Paths.get(path, i + " " + j + "." + attachment.getFileExtension());

                    	attachment.getProxy().downloadToPath(filePath).thenAccept(downloadedPath -> {
                    		
                             System.out.println("Image saved to: " + downloadedPath.toAbsolutePath());
                             
                         }).exceptionally(ex -> {
                        	 
                             System.err.println("Failed to download image: " + ex.getMessage());
                             return null;
                             
                         });
                    }
                    
                    j++;
                    
                }
            	
            	i--;
                
            }
            
        }, failure -> {        	
            System.err.println("Failed to retrieve messages: " + failure.getMessage());            
        });
        
    }
	
}