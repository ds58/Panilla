package com.ruinscraft.panilla.api.config;

import java.io.*;

public class PFiles {

    public static void saveResource(String resourceFileName, File path) throws IOException {
        File newFile = new File(path, resourceFileName);
        if (newFile.exists()) return;
        newFile.createNewFile();
        try (InputStream is = PFiles.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourceFileName);
            }
            byte[] buf = new byte[is.available()];
            is.read(buf);
            try (OutputStream os = new FileOutputStream(newFile)) {
                os.write(buf);
            }
        }
    }

    public static File getResource(String resourceFileName) {
        return new File(PFiles.class.getClassLoader().getResource(resourceFileName).getFile());
    }

}
