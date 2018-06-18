package FileManager;

import java.io.Serializable;

public class FilePart implements Serializable {

    private String filename;
    private long totalParts;
    private int part;
    private byte[] byteArray;

    public FilePart(String filename, long totalParts, int part, byte[] bytearr) {
        this.filename = filename;
        this.totalParts = totalParts;
        this.part = part;
        this.byteArray = bytearr;
    }

    public String getFilename() {
        return filename;
    }

    public long getTotalParts() {
        return totalParts;
    }

    public int getPart() {
        return part;
    }

    public byte[] getByteArray() {
        return byteArray;
    }
}
