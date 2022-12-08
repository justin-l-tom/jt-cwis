package com.justintom1023.discordbot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Countdown {

	public static void countdown(GuildMessageReceivedEvent event) throws ParseException {
				
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = new Date();  
    	
    	final String formattedDate = formatter.format(date) + "T19:00:00Z";
		final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		
		apiFormat.setTimeZone(TimeZone.getTimeZone("GMT-5"));
		
		final Date theDate = apiFormat.parse(formattedDate);
		final long millis = theDate.getTime() - System.currentTimeMillis();

		final String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
		                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		
		
		String[] split_hms = hms.split(":");
		int hourFix = Integer.parseInt(split_hms[0]) - 1;
		
		if (hourFix < 0 || split_hms[1].startsWith("-") || split_hms[2].startsWith("-")) {
			
			int a = 23 + hourFix;
			int b = 60 + Integer.parseInt(split_hms[1]);
			int c = 60 + Integer.parseInt(split_hms[2]);
			
			event.getChannel().sendMessage(a + " hours, " + b + " minutes, and " + c + " seconds until it is 7:00 PM the next day.").queue();
			
		}

		else {
		
			event.getChannel().sendMessage(hourFix + " hours, " + split_hms[1] + " minutes, and " + split_hms[2] + " seconds until it is 7:00 PM sharp.").queue();
			
		}
    	
    }
	
}
