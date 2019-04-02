package file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ble on 5/8/15.
 */
public class BinaryFileUtils {

    /**
     * Read file to a byte array
     *
     * @param fileName string
     * @return byte array
     */
    public static byte[] read(String fileName) {
        return read(new File(fileName).getAbsoluteFile());
    }

    /**
     * Read file to a byte array
     *
     * @param file {@link File} instance
     * @return byte array
     */
    public static byte[] read(File file) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            try {
                byte[] data = new byte[bis.available()];
                bis.read(data);
                return data;
            } finally {
                bis.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
