package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * A general file util class based on sample code from thinking in java 4ed, page 672.
 * Created by ble on 5/8/15.
 */
public class TextFileUtils {

    /**
     * Read a file to a string
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName).getAbsoluteFile()));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                // close ensures the buffer flush is complete
                reader.close();
            }
        } catch (IOException e) {
            // translate to runtime exception
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    /**
     * Read a file to a list of lines separated by line return
     *
     * @param fileName
     * @return
     */
    public static List<String> readLines(String fileName) {
        return readTokens(fileName, "\n");
    }

    /**
     * Read a file to a list of strings by a splitter
     *
     * @param fileName
     * @param splitter string or regular expression
     * @return list of strings
     */
    public static List<String> readTokens(String fileName, String splitter) {
        List<String> stringList = Arrays.asList(read(fileName).split(splitter));
        // Regular expression split() often leaves an empty string at the first position, remove it
        if (stringList.get(0).isEmpty()) {
            stringList.remove(0);
        }
        return stringList;
    }

    /**
     * Write text string to a file using {@link BufferedWriter}
     *
     * @param fileName
     * @param text
     * @throws IOException
     */
    public static void write(String fileName, String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName).getAbsoluteFile()));
            try {
                writer.write(text);
            } finally {
                // explicit close will ensure buffer flush is complete
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write text string to a file using {@link PrintWriter}
     *
     * @param fileName
     * @param text
     */
    public static void printWrite(String fileName, String text) {
        try {
            PrintWriter writer = new PrintWriter(new File(fileName).getAbsoluteFile());
            try {
                writer.write(text);
            } finally {
                writer.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        String text = TextFileUtils.read("src/main/java/file/TextFileUtils.java");
        System.out.println(text);

        List<String> textLines = TextFileUtils.readLines("src/main/java/file/TextFileUtils.java");
        System.out.println("number of lines: " + textLines.size());
    }

}
