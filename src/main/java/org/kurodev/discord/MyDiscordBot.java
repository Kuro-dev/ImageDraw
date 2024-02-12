package org.kurodev.discord;

import kurodev.reader.IniInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.kurodev.graph.DrawMode;
import org.kurodev.graph.KGraph;
import org.kurodev.kimage.draw.DrawableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

public class MyDiscordBot extends ListenerAdapter implements Runnable {
    private static final String MY_COMMAND_NAME = "graph";
    private static final String OPTION_KEY = "equation";
    private static final Logger logger = LoggerFactory.getLogger(MyDiscordBot.class);
    private static JDA bot;
    private final IniInstance settings;

    public MyDiscordBot(IniInstance settings) {

        this.settings = settings;
    }

    @Override
    public void run() {
        Optional<String> key = settings.get("discord.apiKey");
        if (key.isEmpty()) {
            throw new RuntimeException("discord.apiKey not set in settings.ini");
        }
        logger.info("starting discord bot");
        bot = JDABuilder.createDefault(key.get()).build();
        bot.addEventListener(this);
        logger.info("Registering graph command");
        bot.upsertCommand(createCommand()).queue();
    }

    private CommandData createCommand() {
        return Commands.slash(MY_COMMAND_NAME, "draws a graph from a given equation")
                .addOption(OptionType.STRING, OPTION_KEY, "The equation", true)
                .addOption(OptionType.INTEGER, "size", "size of the image. default is 500px")
                .addOption(OptionType.STRING, "graphmode",
                        "How the graph drawing should behave. Default: lines and points", false, true)
                ;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        event.getGuild().retrieveCommands().queue(commands -> {
            if (commands.stream().noneMatch(command -> MY_COMMAND_NAME.equals(command.getName()))) {
                logger.info("Registering graph command for guild {}", event.getGuild().getName());
                event.getGuild().upsertCommand(createCommand()).queue();
            } else {
                logger.info("Graph command exists");
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (MY_COMMAND_NAME.equals(event.getName())) {
            OptionMapping option = event.getInteraction().getOption(OPTION_KEY);
            if (option != null) {
                String equation = option.getAsString();
                KGraph graph = new KGraph();
                DrawableImage img = graph.createGraph(equation);
                event.getInteraction().reply("Graph for " + equation)
                        .addFiles(FileUpload.fromData(img.encode(), "graph.png"))
                        .queue();
            } else {
                event.getInteraction().reply("Equation missing").queue();
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        logger.info("{}", event);
        if ("graphmode".equals(event.getInteraction().getFocusedOption().getName())) {
            var choices = Arrays.stream(DrawMode.values()).map(Enum::name).toList();
            event.replyChoiceStrings(choices).queue();
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
}
