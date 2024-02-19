package org.kurodev.discord.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDiscordCommand {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final String commandName;

    public AbstractDiscordCommand(String name) {
        this.commandName = name;
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public String toString() {
        return commandName;
    }

    public abstract CommandData getCommand();

    public abstract void onInvoke(SlashCommandInteractionEvent event);

    public abstract void onAutocomplete(CommandAutoCompleteInteractionEvent event);
}
