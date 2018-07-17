package FileManager;

import Interfaces.*;

import java.io.*;

public class FileWriter implements Constants {

    private String filename;
    private FileManager fileManager;
    private FileOutputStream fos;

    public FileWriter(FileManager fileManager, FilePart filePart) {
        this.fileManager = fileManager;
        this.filename = filePart.getFilename();
        init(filename);
        writeToFile(filePart);
    }

    private void init(String filename) {
        File tmp = fileManager.createFile(filename, false);
        try {
            fos = new FileOutputStream(tmp, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(FilePart filePart) {
        try {
            fos.write(filePart.getByteArray());
            if (filePart.getPart() == filePart.getTotalParts()) {
                System.out.println("File " + filePart.getFilename() + " has been received.");
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }
}
