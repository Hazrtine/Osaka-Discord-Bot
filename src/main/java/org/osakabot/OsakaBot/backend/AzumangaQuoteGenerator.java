package org.osakabot.OsakaBot.backend;

import org.osakabot.OsakaBot.Osaka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class AzumangaQuoteGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    public static String generate() {
        String filePath = "C:\\Users\\Hazrtine\\IdeaProjects\\OsakaBot\\src\\main\\resources\\AzumangaDaiohEntireScript.txt";
        String regex = "Osaka:(.*?)\n.*";
        Pattern pattern = Pattern.compile(regex);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Scanner scanner = new Scanner(System.in)) {

        StringBuilder fileContent = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            fileContent.append(line).append("\n");
        }
            List<String> matches = new ArrayList<>();
            Matcher matcher = pattern.matcher(fileContent.toString());

            while (matcher.find()) {
                matches.add(matcher.group(0));
            }

            if (matches.isEmpty()) {
                System.out.println("No matches found.");
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(Math.min(matches.size(), 889));
                System.out.println("Randomly selected match: " + matches.get(randomIndex));
                return matches.get(randomIndex);
            }

        } catch (IOException e) {
            LOGGER.error("AzumangaQuoteGenerator Failed: ", e);
        }
        return null;
    }
}
