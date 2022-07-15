package justintom1023.discordbot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CwisListener extends ListenerAdapter {

	int counter = 0;

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		String messageSent = event.getMessage().getContentRaw();

		try {

			if (messageSent.contains("youtube") || messageSent.contains("youtu.be")) {
				
				HondaFilter.hondaFilter(event, messageSent);
				
			}

			else if (messageSent.substring(0, 1).equals(Cwis.prefix)) {

				Commands.commands(event, messageSent);

			}

		}

		catch (NullPointerException e) {

			System.out.println("NullPointerException: " + messageSent);

		}

		catch (StringIndexOutOfBoundsException e) {

			String member = event.getMessage().getMember().toString();
			System.out.println(member.substring(member.indexOf(":") + 1, member.indexOf("(")) + ": Image");

		}

	}

}
