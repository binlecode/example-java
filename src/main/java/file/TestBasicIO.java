package file;

import java.io.*;

/**
 * Created by ble on 3/17/14.
 */
public class TestBasicIO {

    public static void main(String[] args) throws IOException {

        System.out.println("current system user.dir: " + System.getProperty("user.dir"));

        InputStreamReader inRdr = new InputStreamReader(System.in);
        BufferedReader rdr = new BufferedReader(inRdr);
        String line = rdr.readLine();   // this is a thread blocking operation
        while (!line.isEmpty()) {
            System.out.printf("echo: %s \n", line);
            line = rdr.readLine();     // thread blocking here until input available
        }


    }

}
