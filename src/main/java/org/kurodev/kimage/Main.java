package org.kurodev.kimage;

import org.kurodev.kimage.draw.DrawableImage;
import org.kurodev.kimage.img.PixelColor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        DrawableImage img = new DrawableImage(100, 100);
        img.drawCircle(50, 50, 25, PixelColor.BLACK);
        Files.write(Path.of("./img.png"), img.encode());
    }
}
