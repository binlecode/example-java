package file;

import java.io.File;
import java.util.Scanner;

/**
 * Created by ble on 3/18/14.
 */
public class TestFileNodeWalk {


    public static void main(String[] args) {

        System.out.println("input root path:");


//        if (args.length != 1) {
//            System.out.println("usage: TestFileNodeWalk <dir name>");
//            return;
//        }
//        String rootPath = args[0];

        Scanner scanner = new Scanner(System.in);
        String rootPath = scanner.nextLine();

        if (rootPath == null || rootPath.isEmpty()) {
            rootPath = ".";
        }

        TestFileNodeWalk.fileNodeWalk(rootPath);
    }


    public static void fileNodeWalk(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("invalid file path: " + path);
            return;
        }

        if (file.isFile()) {
            System.out.println("found file: " + file.getAbsolutePath());
            return;
        } else if (file.isDirectory()) {
            System.out.println("found dir: " + file.getAbsolutePath());
            for (File f : file.listFiles()) {
                fileNodeWalk(f.getPath());
            }
        } else {
            System.out.println("invalid file: " + file);
        }

    }

}
