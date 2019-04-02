package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * Primary NIO channels:
 * <p>
 * FileChannel
 * DatagramChannel
 * SocketChannel
 * ServerSocketChannel
 * <p>
 * Here is a list of the core Buffer implementations in Java NIO:
 * <p>
 * ByteBuffer
 * CharBuffer
 * DoubleBuffer
 * FloatBuffer
 * IntBuffer
 * LongBuffer
 * ShortBuffer
 * <p>
 * Java NIO Channels are similar to streams with a few differences:
 * <p>
 * You can both read and write to a Channels. Streams are typically one-way (read or write).
 * Channels can be read and written asynchronously.
 * Channels always read to, or write from, a Buffer.
 */

public class TestNIO {


    public static void main(String[] args) throws IOException {

        RandomAccessFile aFile = new RandomAccessFile("src/main/java/nio/TestNIO.java", "r");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(128);  // in bytes

        int bytesRead = inChannel.read(buf);  // read data from channel into buffer
        while (bytesRead != -1) {
            System.out.println("\nread " + bytesRead + " bytes:");
            buf.flip();  // switch from write mode to read mode

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();  // clear buffer to make it ready for write again
            bytesRead = inChannel.read(buf);
        }

        aFile.close();
    }


}
