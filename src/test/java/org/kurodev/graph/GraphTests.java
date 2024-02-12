package org.kurodev.graph;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GraphTests {
    @Test
    public void test() throws IOException {
        KGraph graph = new KGraph();
        Files.write(Path.of("./img.png"), graph.createGraph().encode());
    }

    @Test
    public void test2() throws IOException {
        KGraph graph = new KGraph();
        List<GraphPoint> points = new ArrayList<>();
        points.add(new GraphPoint(1, 1));
        points.add(new GraphPoint(-1, -1));
        Files.write(Path.of("./img.png"), graph.createGraph(points).encode());
    }

    @Test
    public void test3() throws IOException {
        KGraph graph = new KGraph();
        List<GraphPoint> points = new ArrayList<>();
        points.add(new GraphPoint(1, 1));
        points.add(new GraphPoint(-1, -1));
        graph.getOptions()
                .setImageWidth(1920)
                .setImageHeight(1080)
                .setGraphSteps(0.2)
                .setMode(DrawMode.LINES);
        Files.write(Path.of("./img.png"), graph.createGraph("(0.5x^2)-5").encode());
    }
}
