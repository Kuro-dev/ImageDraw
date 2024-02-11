package org.kurodev.graph;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraphTests {
    @Test
    public void test() throws IOException {
        KGraph graph = new KGraph();
        Files.write(Path.of("./img.png"), graph.createGraph().encode());
    }
}
