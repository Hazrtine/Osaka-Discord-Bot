package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Scanner;

public class BotTextFromConsole extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("saata andagi hours rn"))
            speakFromConsole(event.getChannel().asTextChannel());
    }

    private void speakFromConsole(TextChannel channel) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Scanner Start!");
        String message = "";
        while (!message.equals("KILLBOTCONSOLE")) {
            //now sure, i should really not be just straight up using my guild id but this is for like one server i do NOT care
            message = sc.nextLine();
            System.out.println(message);
            channel.sendMessage(message).queue();
        }
    }
}