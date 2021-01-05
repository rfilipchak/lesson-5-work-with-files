package ua.mainacademy;

import ua.mainacademy.model.ConnectionInfo;

import java.util.Date;
import java.util.logging.Logger;

import static ua.mainacademy.service.FileManagerService.readConnectionInfoToFile;
import static ua.mainacademy.service.FileManagerService.writeConnectionInfoToFile;

public class AppRunner {

    private static final Logger LOGGER = Logger.getLogger(AppRunner.class.getName());

    public static void main(String[] args) throws Exception {
        ConnectionInfo connectionInfo = new ConnectionInfo(123, new Date().getTime(), "123.123.123.124");
        ConnectionInfo connectionInfo1 = new ConnectionInfo(124, new Date().getTime(), "123.123.123.124");
        writeConnectionInfoToFile(connectionInfo, "newText.txt");
        writeConnectionInfoToFile(connectionInfo1, "newText.txt");

        LOGGER.info(String.format("FIle is %s", readConnectionInfoToFile("newText.txt").get(0)));
    }
}
