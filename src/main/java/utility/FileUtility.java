package utility;

import java.io.File;

public class FileUtility {

    public boolean checkFileExistence(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }
}
