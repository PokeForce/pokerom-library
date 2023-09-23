/*
 * Decompiled with CFR 0.150.
 */
package narc;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Narc {
    private long narcSize;
    private long numEntries;
    private long fatbSize;
    private long fntbSize;
    private long fimgSize;
    private long fimgOffset;
    private Path narcPath;
    private ArrayList<FatbEntry> fatbEntry;
    private ArrayList<FimgEntry> fimgEntry;

    public ArrayList<FimgEntry> getFimgEntry() {
        return this.fimgEntry;
    }

    public void setFimgEntry(ArrayList<FimgEntry> fimgEntry) {
        this.fimgEntry = fimgEntry;
    }

    public ArrayList<FatbEntry> getFatbEntry() {
        return this.fatbEntry;
    }

    public void setFatbEntry(ArrayList<FatbEntry> fatbEntry) {
        this.fatbEntry = fatbEntry;
    }

    public Narc() {
    }

    public Narc(long narcSize, long fatbSize, long numEntries, long fntbSize) {
        this.narcSize = narcSize;
        this.fatbSize = fatbSize;
        this.numEntries = numEntries;
        this.fntbSize = fntbSize;
    }

    public Narc(String narcPath) throws IOException {
        this.fatbEntry = new ArrayList();
        this.fimgEntry = new ArrayList();
        this.narcPath = Paths.get(narcPath, new String[0]);
        try {
            HexInputStream prova = new HexInputStream(new FileInputStream(narcPath));
            prova.skip(8);
            this.narcSize = prova.readInt();
            prova.skip(8);
            this.fatbSize = prova.readInt();
            this.numEntries = prova.readInt();
            int i = 0;
            while ((long)i < this.numEntries) {
                FatbEntry temp = new FatbEntry();
                temp.setStartOffset(prova.readInt());
                temp.setEndOffset(prova.readInt());
                temp.setEntrySize(temp.getEndOffset() - temp.getStartOffset());
                this.fatbEntry.add(temp);
                ++i;
            }
            prova.skip(4);
            this.fntbSize = prova.readInt();
            prova.skip((int)this.fntbSize);
            this.fimgOffset = prova.getPosition();
            prova.skip(4);
            this.fimgSize = prova.readInt();
            i = 0;
            while ((long)i < this.numEntries) {
                FimgEntry temp2 = new FimgEntry();
                prova.seek(this.fimgOffset + (long)this.fatbEntry.get(i).getStartOffset());
                temp2.setEntryData(prova.readBuffer(this.fatbEntry.get(i).getEntrySize()));
                this.fimgEntry.add(temp2);
                ++i;
            }
            prova.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getFimgSize() {
        return this.fimgSize;
    }

    public void setFimgSize(long fimgSize) {
        this.fimgSize = fimgSize;
    }

    public Path getNarcPath() {
        return this.narcPath;
    }

    public void setNarcPath(Path narcPath) {
        this.narcPath = narcPath;
    }

    public long getNarcSize() {
        return this.narcSize;
    }

    public void setNarcSize(long narcSize) {
        this.narcSize = narcSize;
    }

    public long getFatbSize() {
        return this.fatbSize;
    }

    public void setFatbSize(long fatbSize) {
        this.fatbSize = fatbSize;
    }

    public long getnumEntries() {
        return this.numEntries;
    }

    public void setnumEntries(long numEntries) {
        this.numEntries = numEntries;
    }

    public long getFntbSize() {
        return this.fntbSize;
    }

    public void setFntbSize(long fntbSize) {
        this.fntbSize = fntbSize;
    }

}

