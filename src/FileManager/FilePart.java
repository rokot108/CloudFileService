package FileManager;

import java.io.Serializable;

public class FilePart implements Serializable {

    private String filename;
    private int totalParts;
    private int part;
    private byte[] byteArray;

    public FilePart(String filename, int totalParts, int part, byte[] byteArray) {
        this.filename = filename;
        this.totalParts = totalParts;
        this.part = part;
        this.byteArray = byteArray;
    }

    public String getFilename() {
        return filename;
    }

    public int getTotalParts() {
        return totalParts;
    }

    public int getPart() {
        return part;
    }

    public byte[] getByteArray() {
        return byteArray;
    }
}
