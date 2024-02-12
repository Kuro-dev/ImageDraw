package org.kurodev;

import kurodev.reader.IniInstance;
import org.kurodev.discord.MyDiscordBot;
import org.kurodev.telegram.MyTelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final IniInstance settings;

    static {
        Path path = Path.of("./settings.ini");
        logger.info("Looking for settings file");
        if (Files.exists(path)) {
            logger.info("Found settings file!");
        } else {
            logger.error("Could not find {} file in current directory", path.toAbsolutePath());
            System.exit(-1);
        }
        try {
            settings = IniInstance.createNew(Files.newInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Thread(new MyDiscordBot(settings), "Discord-Thread ").start();
        new Thread(new MyTelegramBot(settings), "Telegram-Thread").start();
    }
}
