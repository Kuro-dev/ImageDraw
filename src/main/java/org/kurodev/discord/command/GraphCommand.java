package org.kurodev.discord.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.kurodev.discord.DiscordCommand;
import org.kurodev.graph.DrawMode;
import org.kurodev.graph.KGraph;
import org.kurodev.kimage.draw.DrawableImage;

import java.util.Arrays;

@DiscordCommand
public class GraphCommand extends AbstractDiscordCommand {
    private static final String EQUATION = "equation";

    public GraphCommand() {
        super("graph");
    }

    @Override
    public CommandData getCommand() {
        return Commands.slash(commandName, "draws a graph from a given equation")
                .addOption(OptionType.STRING, EQUATION, "The equation", true)
                .addOption(OptionType.INTEGER, "size", "size of the image. default is 500px")
                .addOption(OptionType.STRING, "graph-mode",
                        "How the graph drawing should behave. Default: lines and points", false, true)
                .addOption(OptionType.NUMBER, "step-size", "The number of steps, more steps means more points being calculated. Must be between 0.05 and 1")
                ;
    }

    @Override
    public void onInvoke(SlashCommandInteractionEvent event) {
        OptionMapping reqEquation = event.getInteraction().getOption(EQUATION);
        if (reqEquation != null) {
            String equation = reqEquation.getAsString();
            KGraph graph = new KGraph();
            DrawMode drawMode = event.getInteraction().getOption("graph-mode",
                    () -> DrawMode.POINTS_AND_LINES,
                    optionMapping -> DrawMode.valueOf(optionMapping.getAsString().toUpperCase().replace(" ", "_")));
            graph.getOptions().setMode(drawMode);
            int size = event.getInteraction().getOption("size", () -> 500, OptionMapping::getAsInt);
            graph.getOptions().setImageWidth(size);
            graph.getOptions().setImageHeight(size);
            DrawableImage img = graph.createGraph(equation);
            logger.info("Drawing {}x{} image using: {}", size, size, drawMode);
            event.getInteraction().reply("Graph for " + equation)
                    .addFiles(FileUpload.fromData(img.encode(), "graph.png"))
                    .queue();
        } else {
            event.getInteraction().reply("Equation missing").queue();
        }
    }

    @Override
    public void onAutocomplete(CommandAutoCompleteInteractionEvent event) {
        var choices = Arrays.stream(DrawMode.values())
                .map(Enum::toString)
                .filter(s -> s.contains(event.getFocusedOption().getValue().toLowerCase()))
                .toList();
        event.replyChoiceStrings(choices).queue();
    }
}
