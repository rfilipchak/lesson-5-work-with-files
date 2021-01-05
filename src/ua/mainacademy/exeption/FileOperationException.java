package ua.mainacademy.exeption;

public class FileOperationException extends Exception{

    public FileOperationException(String message) {
        super(message);
    }
    public String getMessage() {
        return super.getMessage();
    }
}
