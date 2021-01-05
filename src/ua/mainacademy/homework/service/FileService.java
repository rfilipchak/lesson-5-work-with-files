package ua.mainacademy.homework.service;

import ua.mainacademy.exeption.FileOperationException;
import ua.mainacademy.homework.Person;
import ua.mainacademy.model.ConnectionInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FileService {
    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    private static final String SEPARATOR = System.getProperty("file.separator");

    public static void writeConnectionInfoToFileToSeparateLine(ConnectionInfo connectionInfo, String filePath, String fileName) throws FileOperationException {
        String file = filePath + SEPARATOR + fileName;
        checkFilesDir(filePath);

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.append(connectionInfo.toString()).append("\n");
            writer.flush();
            LOGGER.info("New note added");
        } catch (Exception e) {
            LOGGER.warning("New note not added because of an error");
            throw new FileOperationException("Content did not found");
        }
    }

    public static void writeObjectToFile(Person person, String filePath, String fileName) {
        checkFilesDir(filePath);
        String file = filePath + SEPARATOR + fileName;

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(file));
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(person);
            objectOutputStream.close();
            fileOutputStream.close();
            LOGGER.info(String.format("New Object %s added to file %s", Person.class.getName(), fileName));
        } catch (IOException e) {
            LOGGER.warning(String.format("New Object not added to file because of %s", e.getMessage()));
            e.printStackTrace();
        }
    }

    public static Map<String, Person> readObjectFromFile(String filePath, String fileName) throws FileOperationException {
        String file = filePath + SEPARATOR + fileName;
        File fileForDecoding = new File(file);
        Map result = new HashMap();
        if (isNotExist(file)) {
            throw new FileOperationException("File pass did not found");
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileForDecoding);
             ObjectInputStream objectOutputStream = new ObjectInputStream(fileInputStream)) {
            result.put(fileName, (Person) objectOutputStream.readObject());
            fileInputStream.close();
            objectOutputStream.close();
            LOGGER.info(String.format("New Object %s decoded from file %s", Person.class.getName(), fileName));
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning(String.format("New Object not decoded to file because of %s", e.getMessage()));
            throw new FileOperationException("File not decoded");
        }
        return result;
    }

    public static void readComparedConnectionInfoToFile(String filePath, String sourceFileName, String newFileName, Long time) throws FileOperationException {
        if (time == null) time = 0L;
        List<ConnectionInfo> connectionInfoList = new ArrayList<>();
        String file = filePath + SEPARATOR + sourceFileName;
        if (isNotExist(file)) {
            throw new FileOperationException("File pass did not found");
        }

        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
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
            LOGGER.info(String.format("New %s decoded from file", ConnectionInfo.class.getName()));
        } catch (IOException e) {
            LOGGER.warning(String.format("New Object not decoded to file because of %s", e.getMessage()));
            e.printStackTrace();
        }

        for (ConnectionInfo c : connectionInfoList) {
            if (c.getTime() > time) {
                writeConnectionInfoToFileToSeparateLine(c, filePath, newFileName);
            }
        }
        LOGGER.info(String.format("New file is created file name: %s ", newFileName));
    }


    private static void checkFilesDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) file.mkdir();
    }

    private static boolean isNotExist(String filePath) {
        File file = new File(filePath);
        return !file.exists();
    }
}
