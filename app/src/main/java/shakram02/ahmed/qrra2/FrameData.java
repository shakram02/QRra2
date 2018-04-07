package shakram02.ahmed.qrra2;

import android.graphics.ImageFormat;

import java.nio.ByteBuffer;

public class FrameData {
    private final ByteBuffer frameData;
    private final int width;
    private final int height;
    private final int id;
    private final long timestamp;
    private final int rotation;
    private final int format = ImageFormat.NV21;

    public FrameData(ByteBuffer frameData, int width, int height, int id, long timestamp, int rotation) {

        this.frameData = frameData;
        this.width = width;
        this.height = height;
        this.id = id;
        this.timestamp = timestamp;
        this.rotation = rotation;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ByteBuffer getFrameData() {
        return frameData;
    }

    public int getRotation() {
        return rotation;
    }

    public int getFormat() {
        return format;
    }
}
