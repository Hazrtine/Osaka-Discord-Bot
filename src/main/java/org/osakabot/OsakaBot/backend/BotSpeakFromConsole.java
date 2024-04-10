package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Scanner;

public class BotSpeakFromConsole {
    public static void main(String[] args) {

    }

    public static void speakFromConsole(TextChannel channel) {
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
