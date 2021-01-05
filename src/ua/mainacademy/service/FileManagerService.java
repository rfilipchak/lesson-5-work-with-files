package ua.mainacademy.service;

import ua.mainacademy.exeption.FileOperationException;
import ua.mainacademy.model.ConnectionInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileManagerService {

    private static final String MAIN_DIR = System.getProperty("user.dir");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String FILES_DIR = MAIN_DIR + SEPARATOR + "files";

    public static void writeConnectionInfoToFile(ConnectionInfo connectionInfo, String fileName) throws FileOperationException {
        String filePath = FILES_DIR + SEPARATOR + fileName;
        checkFilesDir();

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(connectionInfo.toString() + "\n");
            writer.flush();
        } catch (Exception e) {
            throw new FileOperationException("File pass did not found");
        }
    }

    public static List<ConnectionInfo> readConnectionInfoToFile(String fileName) throws FileOperationException {
        List<ConnectionInfo> connectionInfoList = new ArrayList<>();
        String filePath = FILES_DIR + SEPARATOR + fileName;

        if (isNotExist(filePath)) {
            throw new FileOperationException("File pass did not found");
        }

        try (FileReader fileReader = new FileReader(filePath); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = line.split(" ");
                ConnectionInfo connectionInfo = new ConnectionInfo(
                        Integer.valueOf(elements[0]),
                        Long.valueOf(elements[1]),
                        elements[2]
                );
                connectionInfoList.add(connectionInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectionInfoList;
    }

    public static void writeBytesToFile(byte[] bytes, String fileName) {
        checkFilesDir();
        String filePath = FILES_DIR + SEPARATOR + fileName;

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readBytesFromFile(String fileName) throws FileOperationException {
        String filePath = FILES_DIR + SEPARATOR + fileName;
        if (isNotExist(filePath)) {
            throw new FileOperationException("File pass did not found");
        }

        File file = new File(filePath);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void copyFile(String sourceFile, String targetFile) {
        byte[] bytes = new byte[0];
        try {
            bytes = readBytesFromFile(sourceFile);
        } catch (FileOperationException e) {
            e.printStackTrace();
        }
        writeBytesToFile(bytes, targetFile);
    }

    public static void moveFile(String sourceFile, String targetFile) {
        String filePath = FILES_DIR + SEPARATOR + sourceFile;
        byte[] bytes = new byte[0];
        try {
            bytes = readBytesFromFile(sourceFile);
        } catch (FileOperationException e) {
            e.printStackTrace();
        }
        writeBytesToFile(bytes, targetFile);
        File file = new File(filePath);
        file.delete();
    }

    private static boolean isNotExist(String filePath) {
        File file = new File(filePath);
        return !file.exists();
    }

    private static void checkFilesDir() {
        File file = new File(FILES_DIR);
        if (!file.exists()) file.mkdir();
    }
}
