package FileManager;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

import Interfaces.*;

public class FileManager implements Constants {

    private String userID;
    private File userDir;
    CloudServiceConnectable connection;
    ArrayList<FileWriter> fileWriters;

    public FileManager(String userID, CloudServiceConnectable connection) {
        this.connection = connection;
        this.userID = userID;
        init();
        initServ();
    }

    public FileManager(CloudServiceConnectable connection) {
        this.connection = connection;
        initUser();
    }

    private void init() {
        fileWriters = new ArrayList<FileWriter>();
        File homePath = new File(HOME_PATH);
        if (!homePath.exists()) {
            homePath.mkdir();
        }
    }

    private void initServ() {
        File serverPath = new File(SERVER_PATH);
        if (!serverPath.exists()) {
            serverPath.mkdir();
        }
        String userPath = SERVER_PATH + "/" + userID;
        userDir = new File(userPath);
        if (!userDir.exists()) {
            System.out.println("Creating a user directory.");
            userDir.mkdir();
        }
    }

    private void initUser() {
        userDir = new File(CLIENT_PATH);
        if (!userDir.exists()) {
            userDir.mkdir();
        }
    }

    public File createFile(String filename) {
        File tmp = new File(userDir + "/" + filename);
        if (!tmp.exists()) {
            try {
                tmp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmp;
        } else return null;
    }

    public void splitAndSend(String filename) {
        File sendingFile = new File(userDir + "/" + filename);
        if (sendingFile.exists()) {
            Thread t = new Thread(() -> {
                try (FileInputStream fis = new FileInputStream(sendingFile)) {
                    int totalParts = (int) (sendingFile.length() / FILEPART_SIZE);
                    if (sendingFile.length() / FILEPART_SIZE != 0 || totalParts == 0) totalParts++;
                    for (int part = 1; part <= totalParts; part++) {
                        if (part < totalParts) {
                            byte[] byteArray = new byte[FILEPART_SIZE];
                            fis.read(byteArray);
                            FilePart filePart = new FilePart(filename, totalParts, part, byteArray);
                            connection.send(filePart);
                        } else {
                            byte[] lastByteArray = new byte[fis.available()];
                            fis.read(lastByteArray);
                            FilePart filePart = new FilePart(filename, totalParts, part, lastByteArray);
                            connection.send(filePart);
                        }
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            });
            t.start();
        } else System.out.println("File not found: " + filename);
    }

    public void writeSplitedFile(FilePart filePart) {
        for (FileWriter fw : fileWriters) {
            if (fw.getFilename().equals(filePart.getFilename())) {
                fw.writeToFile(filePart);
                return;
            }
        }
        fileWriters.add(new FileWriter(this, filePart));
    }
}
