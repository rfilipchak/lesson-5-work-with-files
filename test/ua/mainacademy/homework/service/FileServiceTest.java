package ua.mainacademy.homework.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.mainacademy.exeption.FileOperationException;
import ua.mainacademy.homework.Person;
import ua.mainacademy.model.ConnectionInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.mainacademy.homework.service.FileService.*;

class FileServiceTest {

    private static final String TEST_DIR = "/Users/roman/IdeaProjects/lesson-5-work-with-files/test/resources";
    private final String textInfo = "textInfo.txt";
    private final String textInfoToCompare = "textInfoToCompare.txt";


    private final String expectedFile = "expectedTestInfo.txt";
    private final String expectedObject = "expectedObject.txt";
    private final String filePath = TEST_DIR + "/" + textInfo;
    private final String expectedPath = TEST_DIR + "/" + expectedFile;
    private final String expectedObjectPath = TEST_DIR + "/" + expectedObject;
    private final String textInfoToComparePath = TEST_DIR + "/" + textInfoToCompare;

    @AfterAll
    public static void tearDown() {
        File file = new File("/Users/roman/IdeaProjects/lesson-5-work-with-files/test/resources/textInfo.txt");
        file.delete();
    }

    @Test
    void writeConnectionInfoToFileToSeparateLineTest() throws FileOperationException, IOException {
        ConnectionInfo info = new ConnectionInfo(157, 1609797190524L, "123.123.123.0");
        ConnectionInfo secondInfo = new ConnectionInfo(158, 1609797190524L, "123.123.123.0");

        writeConnectionInfoToFileToSeparateLine(info, TEST_DIR, textInfo);
        writeConnectionInfoToFileToSeparateLine(secondInfo, TEST_DIR, textInfo);

        assertTrue(sameContent(new File(filePath), new File(expectedPath)));
    }

    @Test
    void shouldTrowFileOperationExceptionIfContentNull() {
        Assertions.assertThrows(FileOperationException.class, () -> {
            writeConnectionInfoToFileToSeparateLine(null, filePath, textInfo);
        });
    }

    @Test
    void writeObjectToFileTest() throws IOException {
        writeObjectToFile(new Person("Roman", 30), TEST_DIR, textInfo);

        assertTrue(sameContent(new File(filePath), new File(expectedObjectPath)));
    }

    @Test
    void shouldTrowAnException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            writeObjectToFile(new Person("Roman", 30), null, textInfo);
        });
    }

    @Test
    void readObjectFromFileTest() throws FileOperationException {
        Person expectedPerson = new Person("Roman", 30);

        Person person = readObjectFromFile(TEST_DIR, expectedObject).get(expectedObject);

        assertEquals(person.getName(), expectedPerson.getName());
        assertEquals(person.getAge(), expectedPerson.getAge());
    }

    @Test
    void shouldTrowFileOperationExceptionThenFilePathNull() {
        Assertions.assertThrows(FileOperationException.class, () -> {
            readObjectFromFile(null, expectedObject);
        });
    }

    @Test
    void shouldTrowFileOperationForEmptyFile() {
        Assertions.assertThrows(FileOperationException.class, () -> {
            readObjectFromFile(TEST_DIR, "empty.txt");
        });
    }

    @Test
    void shouldTrowFileOperationExceptionThenFileNameNull() {
        Assertions.assertThrows(FileOperationException.class, () -> {
            readObjectFromFile(TEST_DIR, null);
        });
    }

    @Test
    void readComparedConnectionInfoToFileTest() throws FileOperationException, IOException {
        readComparedConnectionInfoToFile(TEST_DIR, "sourceInfo.txt", "textInfo.txt", 1709797190522L);

        assertTrue(sameContent(new File(filePath), new File(textInfoToComparePath)));
    }

    @Test
    void readComparedConnectionInfoToFileForNullTimeTest() throws FileOperationException, IOException {
        readComparedConnectionInfoToFile(TEST_DIR, "sourceInfo.txt", "textInfo.txt", null);

        assertTrue(sameContent(new File(filePath), new File(TEST_DIR + "/" + "sourceInfo.txt")));
    }

    @Test
    void shouldTrowExceptionThenFieNameNull() {
        Assertions.assertThrows(FileOperationException.class, () -> {
            readComparedConnectionInfoToFile(null, "sourceInfo.txt", "textInfo.txt", 1709797190522L);
        });
    }

    private boolean sameContent(@NotNull File f1, @NotNull File f2) throws IOException {
        if (f1.length() != f2.length()) return false;
        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);
        try {
            int byte1;
            while ((byte1 = fis1.read()) != -1) {
                int byte2 = fis2.read();
                if (byte1 != byte2) return false;
            }
        } finally {
            fis1.close();
            fis2.close();
        }
        return true;
    }
}