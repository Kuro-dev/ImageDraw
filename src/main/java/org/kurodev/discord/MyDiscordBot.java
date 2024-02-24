package org.kurodev.discord;

import kurodev.reader.IniInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kurodev.discord.command.AbstractDiscordCommand;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MyDiscordBot extends ListenerAdapter implements Runnable {

    private static final String MY_COMMAND_NAME = "graph";
    private static final String OPTION_KEY = "equation";
    private static final Logger logger = LoggerFactory.getLogger(MyDiscordBot.class);
    private static JDA bot;
    private final IniInstance settings;
    private final List<AbstractDiscordCommand> commands = new ArrayList<>();

    public MyDiscordBot(IniInstance settings) {

        this.settings = settings;
    }

    private static List<AbstractDiscordCommand> findCommands() {
        logger.info("Finding Discord commands");
        return new Reflections("org.kurodev").getTypesAnnotatedWith(DiscordCommand.class).stream()
                .filter(AbstractDiscordCommand.class::isAssignableFrom)
                .map(aClass -> {
                    try {
                        var out = (AbstractDiscordCommand) aClass.getConstructor().newInstance();
                        logger.info("Registered discord command: {}", out.getCommandName());
                        return out;
                    } catch (Exception e) {
                        logger.error("Failed instantiating {}", aClass.getSimpleName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        for (AbstractDiscordCommand command : commands) {
            event.getGuild().upsertCommand(command.getCommand()).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (AbstractDiscordCommand command : commands) {
            if (command.getCommandName().equals(event.getName())){
                command.onInvoke(event);
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        logger.info("{}", event);
        for (AbstractDiscordCommand command : commands) {
            if (command.getCommandName().equals(event.getFocusedOption().getName())){
                command.onAutocomplete(event);
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info("Discord bot successfully started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down");
            bot.shutdown();
        }));
    }

    @Override
    public void run() {
        commands.addAll(findCommands());
        logger.info("Found {} commands", commands.size());

        Optional<String> key = settings.get("discord.apiKey");
        if (key.isEmpty()) {
            throw new RuntimeException("discord.apiKey not set in settings.ini");
        }
        logger.info("starting discord bot");
        bot = JDABuilder.createDefault(key.get()).build();
        bot.addEventListener(this);
        registerCommands();
    }

    private void registerCommands() {
        for (AbstractDiscordCommand command : commands) {
            logger.info("Registering command: {}", command.getCommandName());
            bot.upsertCommand(command.getCommand()).queue();
            bot.getGuilds().forEach(guild -> guild.upsertCommand(command.getCommand()).queue());
        }

    }
}
