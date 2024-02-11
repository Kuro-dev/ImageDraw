package org.kurodev.kimage.img;

public class PixelColor {
    public static final PixelColor BLACK = new PixelColor(0, 0, 0);
    public static final PixelColor WHITE = new PixelColor(255, 255, 255);
    public static final PixelColor TRANSPARENT = new PixelColor(0, 0, 0, 0);
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    private PixelColor(int r, int g, int b) {
        this.r = r & 0xff;
        this.g = g & 0xff;
        this.b = b & 0xff;
        this.a = 255;
    }

    private PixelColor(int r, int g, int b, int a) {
        this.r = r & 0xff;
        this.g = g & 0xff;
        this.b = b & 0xff;
        this.a = a & 0xff;
    }

    private PixelColor(int rgba) {
        this.r = rgba & 0xff;
        this.g = (rgba >> 8) & 0xff;
        this.b = (rgba >> 16) & 0xff;
        this.a = (rgba >> 24) & 0xff;
    }

    public static PixelColor of(int r, int g, int b) {
        return new PixelColor(r, g, b);
    }

    public static PixelColor of(int r, int g, int b, int a) {
        return new PixelColor(r, g, b, a);
    }

    public static PixelColor of(int rgba) {
        return new PixelColor(rgba);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }
}
