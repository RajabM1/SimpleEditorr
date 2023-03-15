package edu.najah.cap.ex;

import java.nio.file.NoSuchFileException;

public class EditorException extends NoSuchFileException {

    public EditorException(String message) {
        super(message);
    }
}
