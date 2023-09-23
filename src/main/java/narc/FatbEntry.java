/*
 * Decompiled with CFR 0.150.
 */
package narc;

public class FatbEntry {
    private int entrySize;
    private int startOffset;
    private int endOffset;

    public int getEntrySize() {
        return this.entrySize;
    }

    public void setEntrySize(int entrySize) {
        this.entrySize = entrySize;
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return this.endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }
}

