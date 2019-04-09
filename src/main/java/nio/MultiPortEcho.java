package nio;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;


/**
 * This class demonstrates the use of NIO selectors in a multi-port networking echo-er.
 * Unix and Unix-like operating systems have long had efficient implementations of selectors,
 * so this sort of networking program is a model of good performance for a Java-coded
 * networking program.
 * <p>
 * Compile this source, then launch it from the command-line with an invocation such as
 * java MultiPortEcho 8005 8006.
 * Once the MultiPortEchoer is running, start up a simple telnet or other terminal emulator
 * running against ports 8005 and 8006. You will see that the program echoes back characters it
 * receives -- and does it in a single Java thread!
 * <p>
 * <p>
 * A Java NIO {@link ServerSocketChannel} is a channel that can listen for incoming TCP connections,
 * just like a ServerSocket in standard Java Networking.
 */

public class MultiPortEcho {
    private int[] ports;
    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public MultiPortEcho(int[] ports) throws IOException {
        this.ports = ports;

        configure_selector();
    }

    private void configure_selector() throws IOException {
        // Create a new selector
        Selector selector = Selector.open();

        // Open a listener on each port, and register each one
        // with the selector
        for (int i = 0; i < ports.length; ++i) {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);  // set to non-blocking mode

            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);

            // register server channel to selector
            SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Going to listen on " + ports[i]);
        }

        while (true) {
            int num = selector.select();

            Set selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();

            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();

                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    // Accept the new connection
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);  // set to non-blocking mode

                    // Add the new connection to the selector
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
                    it.remove();

                    System.out.println("Got connection from " + sc);
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();

                    // Echo data
                    int bytesEchoed = 0;
                    while (true) {
                        echoBuffer.clear();

                        int number_of_bytes = sc.read(echoBuffer);

                        if (number_of_bytes <= 0) {
                            break;
                        }

                        echoBuffer.flip();

                        sc.write(echoBuffer);
                        bytesEchoed += number_of_bytes;
                    }

                    System.out.println("Echoed " + bytesEchoed + " from " + sc);

                    it.remove();
                }

            }
        }
    }

    static public void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.err.println("Usage: java MultiPortEcho port [port port ...]");
            System.exit(1);
        }

        int[] ports = new int[args.length];

        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }

        new MultiPortEcho(ports);
    }
}


