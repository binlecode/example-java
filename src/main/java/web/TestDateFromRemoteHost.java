package web;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Created by ble on 3/20/14.
 */
public class TestDateFromRemoteHost extends Date {

    static int timePort = 37;

    static final long offset = 2208988800L;

    public TestDateFromRemoteHost(String host) {
        this(host, timePort);
    }

    public TestDateFromRemoteHost(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int time = dis.readInt();
            socket.close();

            setTime((((1L << 32) + time) - offset) * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage: new TestDateFromRemoteHost(host, <port>)");
            return;
        }
        TestDateFromRemoteHost dateFromRemoteHost;

        if (args.length == 1) {
            dateFromRemoteHost = new TestDateFromRemoteHost(args[0]);
        } else {
            dateFromRemoteHost = new TestDateFromRemoteHost(args[0], Integer.parseInt(args[0]));
        }

        System.out.println("got remote host date: " + dateFromRemoteHost);
    }


}
