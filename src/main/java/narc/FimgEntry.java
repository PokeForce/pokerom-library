/*
 * Decompiled with CFR 0.150.
 */
package narc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FimgEntry {
    private ByteArrayOutputStream entryData = new ByteArrayOutputStream();

    public ByteArrayOutputStream getEntrydata() {
        return this.entryData;
    }

    public void setEntrydata(ByteArrayOutputStream entrydata) {
        this.entryData = entrydata;
    }

    public byte[] getEntryData() {
        return this.entryData.toByteArray();
    }

    public void setEntryData(byte[] data) throws IOException {
        this.entryData.reset();
        this.entryData.write(data);
    }
}

