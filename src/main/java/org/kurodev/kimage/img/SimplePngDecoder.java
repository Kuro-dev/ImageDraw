package org.kurodev.kimage.img;

import org.kurodev.kimage.draw.DrawableImage;
import org.kurodev.kimage.util.DeflateCompression;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SimplePngDecoder {
    private final Map<String, byte[]> otherChunkData = new HashMap<>();

    private final Map<String, ChunkHandler> chunkHandlers = new HashMap<>();
    private SimplePng pngHandler;

    public SimplePngDecoder() {
        registerChunkHandler("IHDR", this::handleIHDR);
        registerChunkHandler("IDAT", this::handleIDAT);
        registerChunkHandler("IEND", data -> {
        });
        registerChunkHandler("tRNS", this::handleTRNS); // Added handler for tRNS chunk
    }

    public void addChunkHandler(String chunkType, ChunkHandler handler) {
        chunkHandlers.put(chunkType, handler);
    }

    public void decodePng(byte[] pngData) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(pngData);

        // Skip PNG signature (8 bytes)
        stream.skip(8);

        while (stream.available() > 0) {
            // Read chunk length (4 bytes)
            byte[] lengthBytes = new byte[4];
            stream.read(lengthBytes);
            int length = ByteBuffer.wrap(lengthBytes).getInt();

            // Read chunk type (4 bytes)
            byte[] typeBytes = new byte[4];
            stream.read(typeBytes);
            String type = new String(typeBytes);

            // Read chunk data
            byte[] data = new byte[length];
            stream.read(data);

            // Skip CRC (4 bytes)
            stream.skip(4);

            // Handle the chunk
            ChunkHandler handler = chunkHandlers.get(type);
            if (handler != null) {
                handler.handleChunk(data);
            } else {
                otherChunkData.put(type, data);
            }
        }
    }

    private void handleIHDR(byte[] data) {
        // Handle IHDR chunk data
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int width = buffer.getInt();
        int height = buffer.getInt();
        pngHandler = new SimplePng(width, height);
        // Additional IHDR data can be handled here
    }

    private void handleIDAT(byte[] data) {
        try {
            byte[] decompressed = DeflateCompression.decompress(data);

            // Process decompressed data to remove filter bytes and store in SimplePng
            int bytesPerRow = pngHandler.getWidth() * 4; // 4 bytes per pixel (RGBA)
            byte[] imageData = new byte[pngHandler.getWidth() * pngHandler.getHeight() * 4];
            int destPos = 0;

            for (int row = 0; row < pngHandler.getHeight(); row++) {
                int srcPos = row * (bytesPerRow + 1); // +1 for the filter byte
                System.arraycopy(decompressed, srcPos + 1, imageData, destPos, bytesPerRow);
                destPos += bytesPerRow;
            }

            pngHandler.setImageData(imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTRNS(byte[] data) {
        // Handle tRNS chunk data if present
        // This chunk contains transparency information, such as a transparency palette or alpha channel
        // Here, you would extract and process the alpha channel data if it's included
        // For simplicity, assuming it's a grayscale image, the data represents alpha values directly
        byte[] alphaData = new byte[data.length];
        System.arraycopy(data, 0, alphaData, 0, data.length);
        pngHandler.setAlphaChannel(alphaData);
    }

    private void registerChunkHandler(String chunkType, ChunkHandler handler) {
        chunkHandlers.put(chunkType, handler);
    }

    public DrawableImage getImage() {
        return new DrawableImage(pngHandler, otherChunkData);
    }
}
