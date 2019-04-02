package nio;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by ble on 6/24/15.
 */
public class TestFileChannelTransfer {


    public static void main(String[] args) throws IOException {

        RandomAccessFile fromFile = new RandomAccessFile("src/main/java/nio/TestNIO.java", "r");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("src/main/java/nio/TestNIO.copy.java", "rw");
        FileChannel toChannel = toFile.getChannel();

        toChannel.transferFrom(fromChannel, 0, fromChannel.size());

        // or can use transferTo byy fromChannel
//        fromChannel.transferTo(0, fromChannel.size(), toChannel);

    }

}
