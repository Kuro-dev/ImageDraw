package org.kurodev.discord.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.DiscordCommand;
import org.kurodev.discord.MarkDown;
import org.kurodev.graph.DrawMode;
import org.kurodev.graph.GraphPoint;
import org.kurodev.graph.KGraph;
import org.kurodev.graph.kimage.kimage.draw.DrawableImage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DiscordCommand
public class GraphCommand extends AbstractDiscordCommand {
    private static final String EQUATION = "equation";

    public GraphCommand() {
        super("graph");
    }

    @NotNull
    private static String createValueString(boolean printValues, List<GraphPoint> points) {
        StringBuilder msg = new StringBuilder();
        if (printValues) {
            msg.append("Points:\n");
            // Determine the maximum lengths of the formatted x and y values
            int maxXLength = 0;
            int maxYLength = 0;
            for (GraphPoint point : points) {
                String xFormatted = String.format("%.2f", point.x());
                String yFormatted = String.format("%.2f", point.y());
                maxXLength = Math.max(maxXLength, xFormatted.length());
                maxYLength = Math.max(maxYLength, yFormatted.length());
            }
            msg.append(" ".repeat(maxXLength / 2)).append("X");
            msg.append(" ".repeat(maxXLength + (maxYLength / 2))).append("Y").append("\n");
            msg.append("-".repeat(maxXLength + maxYLength + 3)).append("\n");
            // Append each point, right-aligning both x and y values
            for (GraphPoint point : points) {
                String xPadded = String.format("%" + maxXLength + ".2f", point.x()); // Right-align x value
                String yPadded = String.format("%" + maxYLength + ".2f", point.y()); // Right-align y value
                msg.append(String.format("%s | %s%n", xPadded, yPadded));
            }
        }
        return msg.toString();
    }

    @Override
    public CommandData getCommand() {
        return Commands.slash(commandName, "draws a graph from a given equation")
                .addOption(OptionType.STRING, EQUATION, "The equation", true)
                .addOption(OptionType.INTEGER, "size", "size of the image. default is 500px")
                .addOption(OptionType.STRING, "graph-mode",
                        "How the graph drawing should behave. Default: lines and points", false, true)
                .addOption(OptionType.NUMBER, "step-size", "The number of steps, more steps means more points being calculated. Must be between 0.05 and 1")
                .addOption(OptionType.BOOLEAN, "print-values", "When set it prints all points, default false");
    }

    @Override
    public void onInvoke(SlashCommandInteractionEvent event) {
        KGraph graph = new KGraph();
        DrawMode drawMode = event.getInteraction().getOption("graph-mode",
                () -> DrawMode.POINTS_AND_LINES,
                optionMapping -> DrawMode.valueOf(optionMapping.getAsString().toUpperCase().replace(" ", "_")));
        graph.getOptions().setMode(drawMode);

        int size = event.getInteraction().getOption("size", () -> 500, OptionMapping::getAsInt);
        graph.getOptions().setImageWidth(size);
        graph.getOptions().setImageHeight(size);

        boolean printValues = event.getInteraction().getOption("print-values", () -> false, OptionMapping::getAsBoolean);

        Optional.ofNullable(event.getInteraction().getOption("step-size"))
                .ifPresent(option -> graph.getOptions().setGraphSteps(option.getAsDouble()));

        OptionMapping reqEquation = event.getInteraction().getOption(EQUATION);
        if (reqEquation != null) {
            String equation = reqEquation.getAsString();
            try {
                List<GraphPoint> points = graph.calculatePoints(equation);
                String msg = createValueString(printValues, points);
                DrawableImage img = graph.createGraph(points);
                logger.info("Drawing {}x{} image using: {}", size, size, drawMode);
                event.getInteraction().reply("Graph for " + MarkDown.CODE_BLOCK.wrap(equation))
                        .addContent(MarkDown.CODE_BLOCK.wrap(msg))
                        .addFiles(FileUpload.fromData(img.encode(), "graph.png"))
                        .queue();
            } catch (Exception e) {
                event.getInteraction().reply("Failed to calculate graph for " + MarkDown.CODE.wrap(equation) + ": " + e.getMessage()).queue();
            }

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
