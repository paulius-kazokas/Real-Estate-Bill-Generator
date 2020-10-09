package utils;

import java.io.File;

public class FileUtils {

    public boolean checkFileExistence(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }
}
