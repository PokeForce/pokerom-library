/*
 * Decompiled with CFR 0.150.
 */
package narc;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class HexOutputStream {
    private FileChannel dis;

    public HexOutputStream(FileOutputStream f) {
        this.dis = f.getChannel();
    }

    public void writeByte(byte t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(t);
        buffer.flip();
        this.dis.write(buffer);
    }

    public void writeShort(short t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(t);
        buffer.flip();
        this.dis.write(buffer);
    }

    public void writeInt(int t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(t);
        buffer.flip();
        this.dis.write(buffer);
    }

    public void writeLong(long t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(t);
        buffer.flip();
        this.dis.write(buffer);
    }

    public void writeBuffer(ByteArrayOutputStream t) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(t.toByteArray());
        this.dis.write(buffer);
    }

    public void seek(long t) throws IOException {
        this.dis.position(t);
    }

    public int getPosition() throws IOException {
        return (int)this.dis.position();
    }

    public void close() throws IOException {
        this.dis.close();
    }
}

