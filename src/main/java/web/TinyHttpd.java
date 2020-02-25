package web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ble on 3/20/14.
 */
public class TinyHttpd {
    private static final Logger LOGGER = Logger.getLogger(TinyHttpd.class.getName());
    public static final int DEFAULT_PORT = 9999;
    public static final int THREAD_POOL_SIZE = 3;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        LOGGER.info("running httpd server with port: " + port);
        Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            executor.execute(new TinyHttpdConnection(ss.accept()));
        }
    }
}


class TinyHttpdConnection implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(TinyHttpdConnection.class.getName());

    Socket client;

    public TinyHttpdConnection(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            // RFC for HTTP specifies that web clients and servers should use the ISO8859-1 character encoding
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "8859_1"));
            OutputStream out = client.getOutputStream();
            PrintWriter pout = new PrintWriter(new OutputStreamWriter(out, "8859_1"), true);

            String request = in.readLine();
            LOGGER.info("Request: " + request);

            Matcher get = Pattern.compile("GET /?(\\S*).*").matcher(request);
            if (get.matches()) {
                request = get.group(1);
                LOGGER.info("Matched request: " + request);

                if (request.endsWith("/") || "".equals(request)) {
                    request = "./";
                }

                try {
                    File file = new File(request);

                    // list mode or fetch mode
                    if (file.isDirectory()) {
                        pout.println(generateListPage(file));
                    } else {
                        FileInputStream fis = new FileInputStream(request);
                        byte[] data = new byte[64 * 1024];
                        for (int readLength; (readLength = fis.read(data)) > -1; ) {
                            out.write(data, 0, readLength);
                        }
                        out.flush();
                    }
                } catch (FileNotFoundException e) {
                    pout.println("404 Object Not Found");
                }
            } else {
                pout.println("400 Bad Request");
            }
            in.close();
        } catch (IOException e) {
            LOGGER.warning("I/O error: " + e);
        }

    }

    private String generateListPage(File file) {
        StringBuilder sb = new StringBuilder();
        LOGGER.info("getting list for: " + file.getPath());
        sb.append("<html><head><title>")
                .append(file.getPath())
                .append("</title></head><body>");
        sb.append("<h2>Current path: ").append(file.getPath()).append("</h2>");
        sb.append("<ol>");
        for (File cf : file.listFiles()) {
            LOGGER.info("getting child file: " + cf.getName());
            sb.append("<li>")
                    .append("<a href=\"").append(cf.getPath()).append("\">");
            if (cf.isDirectory()) {
                sb.append(cf.getName() + "/");
            } else {
                sb.append(cf.getName());

            }
            sb.append("</a></li>");
        }
        sb.append("</body>");
        return sb.toString();
    }
}