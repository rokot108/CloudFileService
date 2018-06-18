package FileManager;

import Interfaces.*;

import java.io.*;

public class FileWriter implements Constants {

    private String filename;
    private File file;
    private long totalParts;
    private int part;
    private FilePart filePart;
    private FileManager fileManager;
    FileOutputStream fos;

    public FileWriter(FileManager fileManager, FilePart filePart) {
        this.fileManager = fileManager;
        this.filePart = filePart;
        this.filename = filePart.getFilename();
        this.totalParts = filePart.getTotalParts();
        init(filename);
        writeToFile(filePart);
    }

    private void init(String filename) {
        part = 1;
        File tmp = fileManager.createFile(filename);
        try {
            fos = new FileOutputStream(tmp, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(FilePart filePart) {
        try {
            fos.write(filePart.getByteArray());
            part++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getFilename() {
        return filename;
    }
}
