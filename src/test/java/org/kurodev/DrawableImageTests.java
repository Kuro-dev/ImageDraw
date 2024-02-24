package org.kurodev;

import org.junit.jupiter.api.Test;
import org.kurodev.graph.kimage.kimage.draw.DrawableImage;
import org.kurodev.graph.kimage.kimage.img.PixelColor;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrawableImageTests {

    @Test
    public void testIfImageCanBeSerialisedAndDeserialised() {
        DrawableImage img = new DrawableImage(1920, 1080);
        img.fillRect(300, 300, 400, 550, PixelColor.BLACK);
        img.drawRect(350, 350, 300, 450, PixelColor.WHITE);
        img.drawCircle(400, 400, 25, PixelColor.WHITE);

        DrawableImage img2 = DrawableImage.ofBytes(img.encode());
        assertEquals(img.getPng(), img2.getPng());
    }

    @Test
    public void testIfImageMetaDataIsPersisted() {
        DrawableImage img = new DrawableImage(1920, 1080);
        img.fillRect(300, 300, 400, 550, PixelColor.BLACK);
        img.addCustomChunk("TEST", "THIS IS A TEST");
        DrawableImage img2 = DrawableImage.ofBytes(img.encode());
        assertArrayEquals(img.getChunk("TEST"), img2.getChunk("TEST"));
    }
}
