package justintom1023.discordbot;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Commands {

	public static void commands(GuildMessageReceivedEvent event, String messageSent) {
		
		if (messageSent.toLowerCase().contains("cwis")) {
	    	
	        event.getChannel().sendMessage("```!countdown - When is it 7:00 PM EST?\r\n"
	        		+ "!fsg - Filtered Seed Glitchless Personal Best\r\n"
	        		+ "!random - Sends back a random number between 0 and 99\r\n"
	        		+ "!rsg - Random Seed Glitchless Personal Best\r\n"
	        		+ "!story - Data analysis of RSG times\r\n```").queue();
	        
	    }
	    
		else if (messageSent.toLowerCase().contains("rsg")) {
	    	
	        event.getChannel().sendMessage("RSG: 16:57 IGT").queue();
	        
	    }
	    
		else if (messageSent.toLowerCase().contains("fsg")) {
	    	
	        event.getChannel().sendMessage("FSG: 6:44 IGT").queue();
	        
	    }
	    
		else if (messageSent.toLowerCase().contains("random")) {
	    	
	    	Random rand = new Random();
	    	int n = rand.nextInt(100);
	    	
	    	if (n <= 50) {
	    		
	    		event.getChannel().sendMessage("Bro, it's " + n + " degrees, give me some takis!").queue();
	    		
	    	}
	    	
	    	else {
	    		
	    		event.getChannel().sendMessage("I like my chat above " + n + " degrees!").queue();
	    		
	    	}
	        
	    }
	    
		else if (messageSent.toLowerCase().contains("story")) {
	    	
	    	event.getChannel().sendMessage("https://public.tableau.com/app/profile/justin4096/viz/Calamity427sRSGTimes/Story1").queue();
	        
	    }
		
		else if (messageSent.toLowerCase().contains("countdown")) {
			
			try {
				
				Countdown.countdown(event);
				
			} catch (ParseException e) {
				
				event.getChannel().sendMessage("Please try again later.").queue();
				
			}
			
		}
		
		else if (messageSent.toLowerCase().contains("move")) {
	    	
			Map<String, String> map = new HashMap<String, String>();
			String voiceChannelId = ""; // voice channel id goes here
			
			map.put("", ""); // a member's name and their id goes here
	    	
			try {
				
				String[] name = messageSent.split(" ");
				
				if (name.length == 2 && map.containsKey(name[1].toLowerCase())) {
					
					event.getGuild().moveVoiceMember(event.getGuild().getMemberById(map.get(name[1])),
							event.getGuild().getVoiceChannelById(voiceChannelId)).queue();
					
					event.getMessage().delete().queue();
					
				}
				
			}
			
			catch (Exception e) {
				
				System.out.println("!move: " + messageSent);
				
			}
	    	
	    }
    	
    }
	
}
