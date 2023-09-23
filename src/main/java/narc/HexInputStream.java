/*
 * Decompiled with CFR 0.150.
 */
package narc;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class HexInputStream {
    private FileChannel dis;

    public HexInputStream(FileInputStream f) {
        this.dis = f.getChannel();
    }

    public int readByte() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
        this.dis.read(buffer);
        buffer.flip();
        return buffer.getInt();
    }

    public int readShort() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        this.dis.read(buffer);
        buffer.flip();
        return buffer.getInt();
    }

    public int readInt() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        this.dis.read(buffer);
        buffer.flip();
        return buffer.getInt();
    }

    public long readLong() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        this.dis.read(buffer);
        buffer.flip();
        return buffer.getLong();
    }

    public byte[] readBuffer(int len) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
        this.dis.read(buffer);
        buffer.flip();
        return buffer.array();
    }

    public void seek(long l) throws IOException {
        this.dis.position(l);
    }

    public void skip(int t) throws IOException {
        this.dis.position(this.dis.position() + (long)t);
    }

    public int getPosition() throws IOException {
        return (int)this.dis.position();
    }

    public void close() throws IOException {
        this.dis.close();
    }
}

